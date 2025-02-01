package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.common.Person;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpService {

    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Person[] getAllPeople() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/people"))
            .GET()
            .build();

        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return this.gson.fromJson(send.body(), Person[].class);
    }
}
