package com.challenge.loadbalancer.controller;

import com.challenge.loadbalancer.service.RoundRobin;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final RoundRobin roundRobin;
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    public HomeController(RoundRobin roundRobin) {
        this.roundRobin = roundRobin;
    }

    @GetMapping("/")
    public ResponseEntity<?> roundRobinPath(HttpServletRequest req){

        var res = roundRobin.GET();

        logger.info("Received request from {}",req.getRemoteAddr());

        logger.info("{} {} {}", req.getMethod(), req.getServletPath(), req.getProtocol());
        logger.info("Host: {}", req.getRemoteHost());
        logger.info("User-Agent: {}", req.getHeader("User-Agent"));
        logger.info("Accept: {}", req.getHeader("Accept"));

        logger.info("Response from server: {}",res.getStatusCode());
        logger.info(res.getBody());

        return res;
    }


}
