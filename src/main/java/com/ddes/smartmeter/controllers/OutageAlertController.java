package com.ddes.smartmeter.controllers;

import com.ddes.smartmeter.services.OutageAlertService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outage-alert")
public class OutageAlertController {

    @Autowired
    private OutageAlertService outageAlertService;

    @PostMapping("/dispatch")
    public void dispatchAlert() throws JsonProcessingException {
        outageAlertService.dispatchAlert();
    }
}
