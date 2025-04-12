package org.saartako.client.services;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import org.saartako.common.language.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Service for language state management
 */
public class LanguageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AtomicBoolean isDataValid = new AtomicBoolean(false);

    private final LanguageApiService languageApiService;
    private final AuthService authService;

    private final ListProperty<Language> languages = new SimpleListProperty<>(this, "languages");

    private LanguageService(LanguageApiService languageApiService, AuthService authService) {
        this.languageApiService = languageApiService;
        this.authService = authService;
    }

    public static LanguageService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ListProperty<Language> languagesProperty() {
        return this.languages;
    }

    public void fetchData() {
        if (this.isDataValid.compareAndSet(false, true)) {
            if (!this.authService.isLoggedIn()) {
                this.languages.setValue(FXCollections.observableArrayList());
            } else {
                fetchLanguages().whenComplete((languages, error) -> {
                    if (error != null) {
                        Platform.runLater(() -> {
                            final Alert alert = new Alert(
                                Alert.AlertType.INFORMATION,
                                "Failed to fetch languages\n" + error.getMessage());
                            alert.show();
                        });
                    }
                });
            }
        }
    }

    public CompletableFuture<Language[]> fetchLanguages() {
        LOGGER.info("Trying to fetch languages");

        return this.languageApiService.fetchLanguages()
            .whenComplete((languages, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch languages - {}", throwable.getMessage());

                    this.languages.setValue(FXCollections.observableArrayList());
                } else {
                    LOGGER.info("Succeeded to fetch languages");

                    this.languages.setValue(FXCollections.observableArrayList(languages));
                }
            });
    }

    private static final class InstanceHolder {
        private static final LanguageService INSTANCE = new LanguageService(
            LanguageApiService.getInstance(),
            AuthService.getInstance());
    }
}
