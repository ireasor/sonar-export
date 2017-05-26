package io.github.ireasor.sonarexport.sonarclient;

import java.io.IOException;

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

public class HttpClientProvider {
	
	private String host;
	private int port;
	private HttpHost target;
	private HttpClientContext localContext;
	private CloseableHttpClient httpClient;
	private CredentialsProvider credentialsProvider;
	
	public HttpClientProvider (String address, String username, String password) {
		initializeHttp(address, username, password);
	}
	
	public CloseableHttpClient createHttpClient() {
		httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
		return httpClient;
	}
	
	public void closeHttpClient() throws IOException {
		httpClient.close();
	}
	
	public CloseableHttpResponse executeRequest(String requestPath) throws IOException {
		HttpGet httpget = new HttpGet("http://" + host + ":" + port + requestPath);
		System.out.println("Executing request " + httpget.getRequestLine() + " to target " + target);

		return httpClient.execute(target, httpget, localContext);
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

}
