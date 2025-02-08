package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.song.Song;
import org.saartako.song.SongDTO;
import org.saartako.user.User;
import org.saartako.user.UserDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private HttpService() {
    }

    public static HttpService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public User register(String username, String password, String displayName) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/register?username=" + username + "&password=" + password + "&displayName=" + displayName))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (send.statusCode() != 200) {
            throw new IOException(send.body());
        }

        return this.gson.fromJson(send.body(), UserDTO.class);
    }

    public User login(String username, String password) throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/auth/login?username=" + username + "&password=" + password))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (send.statusCode() != 200) {
            throw new IOException(send.body());
        }

        return this.gson.fromJson(send.body(), UserDTO.class);
    }

    public Song[] fetchSongs() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/song"))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (send.statusCode() != 200) {
            throw new IOException(send.body());
        }

        return this.gson.fromJson(send.body(), SongDTO[].class);
    }

    private static final class InstanceHolder {
        private static final HttpService INSTANCE = new HttpService();
    }
}
