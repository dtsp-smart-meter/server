# smart-meter
This repository contains a java spring boot app acting as the back end for Assignment 1 of the Designing and Developing Enterprise Solutions module.
The app is designed to process and persist events from a RabbitMQ queue, notifying the client on completion of the processing of the event. 
The app also acts as middleware by allowing all instances of the client to queue events on the RabbitMQ queue through WebSockets.