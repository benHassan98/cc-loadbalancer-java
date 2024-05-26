package com.challenge.loadbalancer.service;

public interface HealthCheckService {

    public boolean isItHealthy(String serverUrl);
    public void healthCheck();


}
