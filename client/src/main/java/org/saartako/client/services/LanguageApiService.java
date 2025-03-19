package org.saartako.client.services;

import com.google.gson.Gson;
import org.saartako.client.Config;
import org.saartako.client.utils.HttpUtils;
import org.saartako.common.language.Language;
import org.saartako.common.language.LanguageDTO;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class LanguageApiService {

    private static final Gson GSON = new Gson();

    private final HttpService httpService;
    private final AuthService authService;

    private LanguageApiService(HttpService httpService, AuthService authService) {
        this.httpService = httpService;
        this.authService = authService;
    }

    public static LanguageApiService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public CompletableFuture<Language[]> fetchLanguages() {
        return this.authService.requireJwtToken().thenCompose(authorization -> {
            final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Config.serverUrl + "/language"))
                .GET()
                .header("Authorization", "Bearer " + authorization)
                .build();

            return this.httpService.getHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(HttpUtils::validateResponse)
                .thenApply(response -> GSON.fromJson(response.body(), LanguageDTO[].class));
        });
    }

    private static final class InstanceHolder {
        private static final LanguageApiService INSTANCE = new LanguageApiService(
            HttpService.getInstance(),
            AuthService.getInstance()
        );
    }
}
