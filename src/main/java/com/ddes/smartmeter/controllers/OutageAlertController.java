package com.ddes.smartmeter.controllers;

import com.ddes.smartmeter.services.OutageAlertService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling outage alerts.
 * Provides an endpoint to trigger an outage alert for all clients.
 */
@RestController
@RequestMapping("/outageAlert")
public class OutageAlertController {

    // Service responsible for managing and dispatching outage alerts.
    @Autowired
    private OutageAlertService outageAlertService;

    /**
     * Endpoint to dispatch an outage alert.
     * This method triggers the `dispatchAlert` functionality in the OutageAlertService.
     */
    @PostMapping("/dispatch")
    public void dispatchAlert() throws JsonProcessingException {
        outageAlertService.dispatchAlert();
    }
}