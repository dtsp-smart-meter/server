package com.ddes.smartmeter;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }

}
