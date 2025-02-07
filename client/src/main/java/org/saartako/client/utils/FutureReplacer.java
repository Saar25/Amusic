package org.saartako.client.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class FutureReplacer {

    private final boolean mayInterruptIfRunning;

    private Future<?> future;

    public FutureReplacer(boolean mayInterruptIfRunning) {
        this.mayInterruptIfRunning = mayInterruptIfRunning;
    }

    public void replaceWith(CompletableFuture<?> future) {
        if (this.future != null && !this.future.isDone()) {
            this.future.cancel(mayInterruptIfRunning);
        }

        this.future = future;
    }
}
