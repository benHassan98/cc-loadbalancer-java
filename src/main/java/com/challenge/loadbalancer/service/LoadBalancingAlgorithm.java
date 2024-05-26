package com.challenge.loadbalancer.service;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface LoadBalancingAlgorithm {
    public Optional<String> getNextServer();
    public ResponseEntity<String> GET();
}
