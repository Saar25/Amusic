package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.user.User;
import org.saartako.user.UserDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    private static HttpService INSTANCE;

    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private HttpService() {
    }

    public static synchronized HttpService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpService();
        }
        return INSTANCE;
    }

    public User register(String username, String password, String displayName) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/register?username=" + username + "&password=" + password + "&displayName=" + displayName))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return this.gson.fromJson(send.body(), UserDTO.class);
    }

    public User login(String username, String password) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/login?username=" + username + "&password=" + password))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return this.gson.fromJson(send.body(), UserDTO.class);
    }

    public User[] getAllUsers() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/user"))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return this.gson.fromJson(send.body(), UserDTO[].class);
    }
}
