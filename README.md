# smart-meter
This repository contains a java spring boot app acting as the back end for Assignment 1 of the Designing and Developing Enterprise Solutions module.
The app is designed to process and persist events from a RabbitMQ queue, notifying the client on completion of the processing of the event. 
The app also acts as middleware by allowing all instances of the client to queue events on the RabbitMQ queue through WebSockets.

# Setup

## Mac -

## 1 - Running against a locally hosted RabbitMQ Docker Container

1. First, ensure you have rancher desktop installed - if not, the link to download can be found below

| M1 Mac | Intel Mac | 
| - | - |
| [Download](https://github.com/rancher-sandbox/rancher-desktop/releases/download/v1.0.0/Rancher.Desktop-1.0.0.aarch64.dmg) | [Download](https://github.com/rancher-sandbox/rancher-desktop/releases/download/v1.0.0/Rancher.Desktop-1.0.0.x86_64.dmg) |

2. Once downloaded, open the .dmg and drag the app to Applications.

3. Open the app from Applications and await initialisation.

4. Once complete, run the following command in a terminal ` brew install docker-compose `
    * If you don't have brew installed, you can download from [here](https://brew.sh)

5. Once complete, navigate to 'Supporting Utilities' in Rancher and tick docker at the top.

6. Ensure that the active profile selected within the application.yaml is local

7. Update the application.yaml and populate the file with the corret user/password combos within the datasource url and the rabbitMQ credentials

8. Open a new terminal and navigate to the server folder located within this repository

9. Run `docker-compose up` this should create a local instance of rabbitMQ running against a docker container

10. Now run `./mvnw clean spring-boot:run` from the server directory, and the application should boot successfully

## Known Potential issues

You may not have your IP Address added to the firewall for the database, if the application fails whilst configuring hibernate this is likely the cause
