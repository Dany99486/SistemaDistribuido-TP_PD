# Distributed Programming Project (ISEC)

## Overview

This project is a distributed system developed for ISEC (Instituto Superior de Engenharia de Coimbra) in a Client-Server architecture with a backup server support. It implements a Java-based networked event and attendance management system, including authentication, event creation, CSV data exchange, and failover support through a backup server.

## Architecture

The system is split into three Java modules:

- `client/` — JavaFX client application with UI controllers and models.
- `server/` — Primary server implementation with TCP and RMI, database model, and communication threads.
- `serverBackup/` — Backup server that can take over if primary server fails, with heartbeat and RMI observer support.

### Core components

- **Client**: Handles user login/registration, event queries, attendance, CSV export/import, and user operations.
- **Server**: Handles requests from clients, event/attendance persistence, file exchange, and heartbeat monitoring.
- **Server Backup**: Receives heartbeats from the primary server, maintains backup replica state, and can restore service.

## Features

- User authentication/registration.
- Event creation, editing, and query by date.
- Attendance recording and presence verification.
- CSV file export/download for events and clients.
- Primary/backup server heartbeat monitoring and recovery.
- RMI service for remote database observer and fault tolerance.

## Folder Structure

```
client/
  src/pt/isec/PD/Client.java
  src/pt/isec/PD/Main.java
  src/pt/isec/PD/ReceiveFile.java
  src/pt/isec/PD/ClientUI/
    MainJFX.java
    Controllers/...
    Model/...
    Views/...

server/
  src/pt/isec/PD/Main.java
  src/pt/isec/PD/Model/Server.java
  src/pt/isec/PD/Model/Heartbeat.java
  src/pt/isec/PD/REST and RMI classes
  src/pt/isec/PD/UI/ServerUI.java

serverBackup/
  src/pt/isec/PD/Model/ServerBackup.java
  src/pt/isec/PD/Model/HeartbeatReceiver.java
  src/pt/isec/PD/Model/RmiService.java
```

## Requirements

- Java 17+ (or configured Java version in your IDE)
- JavaFX SDK for client UI
- Git and your IDE (IntelliJ IDEA recommended for module management)

## Running the Project

1. Open the workspace in IntelliJ IDEA or your Java IDE.
2. Build each module (`client`, `server`, `serverBackup`) from respective Gradle/Maven/IDE settings.
3. Start the primary server first using `server/src/pt/isec/PD/Main.java`.
4. Start the backup server using `serverBackup/src/pt/isec/PD/Model/MainServerBackup.java`.
5. Start the client using `client/src/pt/isec/PD/Main.java`.
6. Log in via client UI and interact with events, attendance, and CSV features.

## How Backup Works

- The primary server sends regular heartbeat messages to the backup server.
- The backup server maintains a local replica (`BD` model) and listens for heartbeat updates.
- If heartbeats stop, the backup can promote itself to active mode and continue serving client requests.

## Development Notes

- The code uses a mix of TCP socket connections, file transfer classes (`SendFile`, `ReceiveFile`), and RMI interfaces (`GetRemoteBDServiceInterface`).
- UI controllers are located under `client/src/pt/isec/PD/ClientUI/Controllers/`.
- Reusable models are under `client/src/pt/isec/PD/ClientUI/Model/` and `server/src/pt/isec/PD/Model/`.

