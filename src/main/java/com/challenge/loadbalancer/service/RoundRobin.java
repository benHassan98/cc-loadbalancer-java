package com.challenge.loadbalancer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
public class RoundRobin implements LoadBalancingAlgorithm{

    private final HealthCheckService healthCheckService;
    private final Queue<String> serverUrlQueue = new LinkedList<>();
    @Autowired
    public RoundRobin(HealthCheckService healthCheckService,
                      @Value("#{'${servers}'.split(',')}") List<String> serverUrlList) {
        this.healthCheckService = healthCheckService;
        this.serverUrlQueue.addAll(serverUrlList);

    }

    @Override
    synchronized public Optional<String> getNextServer() {

        int idx = 0;
        String serverUrl;

        while(idx < serverUrlQueue.size() && !serverUrlQueue.isEmpty()){

            serverUrl = serverUrlQueue.poll();
            serverUrlQueue.add(serverUrl);

            if(healthCheckService.isItHealthy(serverUrl)){
                return Optional.of(serverUrl);
            }
            idx++;
        }

        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> GET() {
        RestTemplate restTemplate = new RestTemplate();

        return this.getNextServer()
               .map(serverUrl->
                       ResponseEntity.ok(restTemplate.getForEntity(serverUrl, String.class).getBody()))
               .orElseGet(()->ResponseEntity.status(503).body("Servers not healthy"));


    }
}
