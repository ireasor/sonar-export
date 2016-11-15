package io.github.ireasor.sonarexport.sonarclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import io.github.ireasor.sonarexport.entity.Issue;
import io.github.ireasor.sonarexport.entity.Results;

public class SonarClient {

	private Gson gson;
	
	private String host;
	private int port;
	
	private HttpHost target;
	private HttpClientContext localContext;
	private CredentialsProvider credentialsProvider;

	public SonarClient(String address, String username, String password) {
		this.gson = new Gson();
		initializeHttp(address, username, password);
	}

	public List<Issue> executeSearch(String project) throws Exception {
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();

		try {
			List<Issue> issuesList = new ArrayList<Issue>();
			int pageNumber = 1;
			boolean hasMorePages = true;
			
			while (hasMorePages) {
				Results results = searchPage(httpClient, project, pageNumber);
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
	
	private void initializeHttp(String address, String username, String password) {
		if (address.contains(":")) {
			String[] addressParts = address.split(":");
			host = addressParts[0];
			port = Integer.valueOf(addressParts[1]);
		} else {
			host = address;
			port = 80;
		}
		
		this.target = new HttpHost(host, port, "http");
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(target, basicAuth);
		this.localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);

		this.credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()), new UsernamePasswordCredentials(username, password));
	}

	private Results searchPage(CloseableHttpClient httpClient, String project, int pageNumber) throws Exception {
		HttpGet httpget = new HttpGet("http://" + host + ":" + port + "/api/issues/search?projectKeys=" + project + "&p=" + pageNumber);
		System.out.println("Executing request " + httpget.getRequestLine() + " to target " + target);

		CloseableHttpResponse response = httpClient.execute(target, httpget, localContext);
		try {
			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());

			return gson.fromJson(EntityUtils.toString(response.getEntity()), Results.class);
		} finally {
			response.close();
		}
	}

}
