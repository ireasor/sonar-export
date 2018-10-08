package io.github.ireasor.sonarexport.sonarclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

public class HttpClientProvider {

    private String protocol;
    private String host;
    private int port;
    private HttpHost target;
    private HttpClientContext localContext;
    private CloseableHttpClient httpClient;
    private CredentialsProvider credentialsProvider;

    public HttpClientProvider (String protocol, String address, String username, String password) {

        this.protocol = protocol;

        if (address.contains(":")) {
            String[] addressParts = address.split(":");
            this.host = addressParts[0];
            this.port = Integer.valueOf(addressParts[1]);
        } else {
            this.host = address;
            
            if (protocol.equals("https")) {
                this.port = 443;
            } else {
                this.port = 80;
            }
        }

        initializeHttp(address, username, password);
    }

    public void createClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        sslcontext.init(null, new X509TrustManager[]{new HttpsTrustManager()}, new SecureRandom());
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext);
        this.httpClient = HttpClients.custom().setSSLSocketFactory(factory).setDefaultCredentialsProvider(credentialsProvider).build();
    }

    public void closeClient() throws IOException {
        this.httpClient.close();
    }

    public CloseableHttpResponse executeRequest(String requestPath) throws IOException {
        HttpGet httpget = new HttpGet(protocol + "://" + host + ":" + port + requestPath);
        System.out.println("Executing request " + httpget.getRequestLine() + " to target " + target);

        return httpClient.execute(target, httpget, localContext);
    }

    private void initializeHttp(String address, String username, String password) {
        this.target = new HttpHost(host, port, protocol);
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);
        this.localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        this.credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()), new UsernamePasswordCredentials(username, password));
    }

}
