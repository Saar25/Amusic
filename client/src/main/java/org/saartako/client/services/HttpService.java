package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.user.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public User[] getAllUsers() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/user"))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return this.gson.fromJson(send.body(), User[].class);
    }
}
