package com.challenge.loadbalancer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class HealthCheckServiceImpl implements HealthCheckService{

    private final ConcurrentHashMap<String, Boolean> serverHealth = new ConcurrentHashMap<>();
    private final List<String> serverUrlList = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(HealthCheckServiceImpl.class);

    public HealthCheckServiceImpl(@Value("#{'${servers}'.split(',')}") List<String> serverUrlList) {

        this.serverUrlList.addAll(serverUrlList);

        new ScheduledThreadPoolExecutor(1).
                scheduleWithFixedDelay(this::healthCheck,
                        0,
                        10,
                        TimeUnit.SECONDS);

    }

    @Override
    public boolean isItHealthy(String serverUrl) {
        return this.serverHealth.get(serverUrl);
    }

    @Override
    public void healthCheck() {

        RestTemplate restTemplate = new RestTemplate();

        this.serverUrlList.forEach(serverUrl->{

            Boolean boolVal;

            try{
                boolVal = restTemplate.getForEntity(serverUrl+"/health", String.class).getStatusCode().is2xxSuccessful();
                logger.info("{} passed HealthTest", serverUrl);
            }catch (Exception exception){
                boolVal = false;
                logger.info("{} didn't pass HealthTest", serverUrl);
            }

            serverHealth.put(serverUrl, boolVal);

        });

    }
}
