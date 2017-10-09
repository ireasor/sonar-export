package io.github.ireasor.sonarexport.sonarclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import io.github.ireasor.sonarexport.entity.Component;
import io.github.ireasor.sonarexport.entity.ComponentTreeResults;
import io.github.ireasor.sonarexport.entity.Issue;
import io.github.ireasor.sonarexport.entity.SearchResults;

public class SonarClient {

	private Gson gson;
	private HttpClientProvider httpClientProvider;

	public SonarClient(HttpClientProvider httpClientProvider) {
		this.gson = new Gson();
		this.httpClientProvider = httpClientProvider;
	}

	public List<Issue> executeSearch(String project) throws Exception {
		CloseableHttpClient httpClient = httpClientProvider.createHttpClient();
		
		try {
			List<Issue> issuesList = new ArrayList<Issue>();
			SearchExecutor<SearchResults> searchExec = new SearchExecutor<SearchResults>(SearchResults.class, gson, httpClientProvider);
			int pageNumber = 1;
			boolean hasMorePages = true;
			
			while (hasMorePages) {
				String requestPath = "/api/issues/search?projectKeys=" + project + "&resolved=false" + "&p=" + pageNumber;
				SearchResults results = searchExec.getResults(requestPath);
				issuesList.addAll(Arrays.asList(results.getIssues()));
				
				if (results.getP() * results.getPs() < results.getTotal()) {
					pageNumber++;
				}  else {
					hasMorePages = false;
				}
			}

			return issuesList;

		} finally {
			httpClient.close();
		}
	}
	
	public List<Component> getCoverage(String project) throws Exception {
		CloseableHttpClient httpClient = httpClientProvider.createHttpClient();

		try {			
			List<Component> componentsList = new ArrayList<Component>();
			SearchExecutor<ComponentTreeResults> searchExec = new SearchExecutor<ComponentTreeResults>(ComponentTreeResults.class, gson, httpClientProvider);
			
			int pageNumber = 1;
			boolean hasMorePages = true;
			
			while (hasMorePages) {
				String requestPath = "/api/measures/component_tree?asc=true&ps=100&metricSortFilter=withMeasuresOnly&p=" + pageNumber + "&s=metric%2Cname&metricSort=coverage&baseComponentKey=" + project + "&metricKeys=coverage%2Cuncovered_lines%2Cuncovered_conditions&strategy=leaves";
				ComponentTreeResults results = searchExec.getResults(requestPath);
				componentsList.addAll(Arrays.asList(results.getComponents()));
				
				if (results.getPaging().getPageIndex() * results.getPaging().getPageSize() < results.getPaging().getTotal()) {
					pageNumber++;
				}  else {
					hasMorePages = false;
				}
			}

			return componentsList;

		} finally {
			httpClient.close();
		}
	}
}

class SearchExecutor<T> {
	private Class<T> clazz;
	private HttpClientProvider httpClientProvider;
	private Gson gson;
	
	public SearchExecutor(Class<T> clazz, Gson gson, HttpClientProvider httpClientProvider) {
		this.clazz = clazz;
		this.gson = gson;
		this.httpClientProvider = httpClientProvider;
	}
	
	public T getResults(String requestPath) throws IOException {
		CloseableHttpResponse response = httpClientProvider.executeRequest(requestPath);

		try {
			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			return gson.fromJson(EntityUtils.toString(response.getEntity()), clazz);
		} finally {
			response.close();
		}
	}
}