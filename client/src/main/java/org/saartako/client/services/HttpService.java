package org.saartako.client.services;

import java.net.http.HttpClient;

public class HttpService {

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    private HttpService() {
    }

    public static HttpService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    private static final class InstanceHolder {
        private static final HttpService INSTANCE = new HttpService();
    }
}
