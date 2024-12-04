# Smart Meter Application

This repository contains a Java Spring Boot application that serves as the backend for a Smart Meter system. The application acts as middleware for WebSocket-based communication between Python clients.

## Features
- **Secure Communication**: Uses SSL for secure communication with WebSocket endpoints.
- **Authentication Tokens**: Requires clients to provide an authentication token for communication.
- **Asynchronous Queuing**: Uses RabbitMQ to queue and process client messages asynchronously.

## Prerequisites

### Software Requirements
1. **Java Development Kit (JDK)**: Version 11 or higher.
2. **Apache Maven**: To build and run the application.

### Generate Keystore, CA Certificate, and Truststore

#### Step 1: Generate the Keystore
This command will generate the keystore. When ran, you will be prompted to enter a password for the keystore.

```bash
keytool -genkeypair -keyalg RSA -keysize 2048 -validity 365 -keystore smartmeter_keystore.jks -dname "CN=localhost"
```

#### Step 2: Export the Certificate Authority (CA) Certificate
This command will export the CA certificate from the keystore. When ran, you will be prompted to enter the keystore password.

```bash
keytool -exportcert -keystore smartmeter_keystore.jks -file smartmeter_ca_cert.pem -rfc
```

#### Step 3: Generate the Truststore and Import the CA Certificate
This command will generate a truststore and import the CA certificate into it. When ran, you will be prompted to type 'yes' to trust the certificate, and to enter a password for the truststore.

```bash
keytool -importcert -file smartmeter_ca_cert.pem -keystore smartmeter_truststore.jks
```

### Install RabbitMQ

| Windows           | MacOS             | Linux                                                         |
|-------------------|-------------------|---------------------------------------------------------------|
| [Install Guide](https://www.rabbitmq.com/docs/install-windows) | [Install Guide](https://www.rabbitmq.com/docs/install-homebrew) | [Install Guide](https://www.rabbitmq.com/docs/install-debian) |

### Environment Variables
You must set the following environment variables for the application to run as expected.

- `SMARTMETER_KEYSTORE_LOCATION` – The path to the keystore file.
- `SMARTMETER_KEYSTORE_PASSWORD` – The password to access the keystore file.
- `SMARTMETER_TRUSTSTORE_LOCATION` – The path to the truststore file.
- `SMARTMETER_TRUSTSTORE_PASSWORD` – The password to access the truststore file.
- `SMARTMETER_RABBITMQ_HOST` – The hostname or IP address to connect to the RabbitMQ server.
- `SMARTMETER_RABBITMQ_USERNAME` – The username for authenticating with the RabbitMQ server.
- `SMARTMETER_RABBITMQ_PASSWORD` – The password for the RabbitMQ user.

## Running the Application


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

Once the server is running, it will:

- Listen on port 8443 (configured in application.yml).
- Interact with RabbitMQ queues to process messages.
- Provide a WebSocket endpoint /ws for client communication.

## Known Issues

### Firewall Restrictions
Ensure your local IP address is added to any necessary firewalls, especially when connecting to RabbitMQ.

### Keystore and Truststore Issues
Ensure paths and passwords for the keystore and truststore are correctly set in the environment variables.