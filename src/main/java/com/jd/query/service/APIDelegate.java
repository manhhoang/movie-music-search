package com.jd.query.service;

import com.jd.query.exception.AppException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class APIDelegate {

  private APILookup lookupService;
  private ProviderService providerService;
  private APIType serviceType;

  public void setLookupService(APILookup lookupService) {
    this.lookupService = lookupService;
  }

  public void setServiceType(APIType serviceType) {
    this.serviceType = serviceType;
  }

  private ProviderService findProviderService() {
    providerService = lookupService.getService(serviceType);
    if(providerService == null)
      throw new AppException("200", "Unable to find service provider");

    return providerService;
  }

  public <T> CompletableFuture<T> doSearch(String name) {
    return findProviderService().search(name);
  }

  public <T> CompletableFuture<Optional<T>> doLookup(String name) {
    return findProviderService().lookup(name);
  }
}
