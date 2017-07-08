package com.jd.query.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ProviderService<T> {

    CompletableFuture<T> search(String name);

    CompletableFuture<T> lookup(String name);
}
