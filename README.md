# PixelThump Messaging Service API Gateway

The PixelThump Messaging Service is a crucial component of the PixelThump project, designed to facilitate real-time communication using WebSockets over STOMP (Simple Text Oriented Messaging Protocol) within a Spring Boot application environment.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## Introduction

PixelThump Messaging Service acts as a centralized entry point for WebSocket communication in the PixelThump project. It handles the routing of WebSocket messages, authentication, and authorization. This project aims to provide a seamless and efficient real-time communication infrastructure for applications built on top of the PixelThump platform.

## Features

- **WebSocket Communication**: Utilizes the WebSocket protocol to establish full-duplex communication between clients and the server, enabling real-time data exchange.

- **STOMP Protocol**: Implements the Simple Text Oriented Messaging Protocol (STOMP) to enhance the interoperability of communication between clients and the server.

- **Centralized Gateway**: Acts as a centralized entry point for WebSocket connections, making it easier to manage, monitor, and control the communication flow.

- **Scalability**: Designed to be scalable, allowing for the handling of a large number of concurrent WebSocket connections efficiently.

## Getting Started

**Installation and Setup**

To deploy the PixelThump Messaging Service API Gateway using Docker, follow these steps:

Navigate to the project directory:

Pull the Docker image from the PixelThump repository:

    docker pull ghcr.io/pixelthump/pixelthump/messaging-service:dev

Run the Docker container with the pulled image:

    docker run -d -p 8080:8080 ghcr.io/pixelthump/pixelthump/messaging-service:dev

The PixelThump Messaging Service API Gateway should now be running inside a Docker container, accessible through port 8080.

Make sure to adjust the container port (-p flag) if you want to map it to a different port on your host machine.

**Usage**

To use the PixelThump Messaging Service API Gateway within the Docker container, follow the same usage instructions provided in the Usage section of this README.

Connect to the WebSocket endpoint and send/receive messages as described. Remember that the Docker container's port is mapped to the host's port as specified during the docker run command.

For detailed information on how to interact with the messaging service, refer to the API Documentation section.

Contributions to the PixelThump Messaging Service are encouraged. If you'd like to contribute, please follow the guidelines outlined in the Contribution Guidelines.

This project is licensed under the MIT License.

For any questions or issues, please write an issue here on github
