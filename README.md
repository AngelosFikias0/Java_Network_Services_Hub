# Java Network Services Hub

> **A modular Java networking suite for managing HTTP, TCP, UDP, and RMI services within a unified system.**

The **Java Network Services Hub** is a multi-threaded, enterprise-ready networking platform built in Java. It integrates a graphical Swing UI, PostgreSQL logging, and modular service architecture, demonstrating advanced **network programming, concurrency, and system design**.

---

## 📌 System Overview

The architecture follows a **Client–Server Modular Design**:

```text
    Graphical Service Launcher
        |
        v
   Service Modules
    /    |    |    \
   v     v    v     v
HTTP   TCP   UDP   RMI
 |      |     |     |
 v      v     v     v
PostgreSQL Logging
```

- **Frontend:** Java Swing UI for real-time service management and control.
- **Backend:** Modular Java services handling diverse protocols (HTTP, TCP, UDP, RMI).
- **Database:** PostgreSQL with structured logging and referential integrity.

---

## ✨ Key Features

### 🎛 Graphical Service Launcher

- **Multi-threaded Swing UI:** Manages service lifecycles without freezing the interface.
- **Real-time Status:** Dynamic feedback on active network modules and port usage.

### 🌐 Protocol Modules

- **HTTP Server:** Handles GET/POST requests; designed to be extendable for REST APIs.
- **TCP Module:** Reliable multi-client communication using `ServerSocket` and optimized thread pooling.
- **UDP Module:** Low-latency, asynchronous message exchange using `DatagramSocket`.
- **RMI Module:** Distributed computing support via RMI Registry and remote interfaces.

### 📊 Advanced Data Handling

- **PostgreSQL Integration:** Centralized logging of all network events and client interactions.
- **Relational Integrity:** Uses `ON DELETE CASCADE` for clean data management.
- **Transaction Safety:** Critical operations like port reservation and logging are wrapped in transactions.

---

## 🧰 Technical Stack

| Area            | Technology                                |
| :-------------- | :---------------------------------------- |
| **Programming** | Java 17+                                  |
| **Networking**  | TCP, UDP, HTTP, RMI                       |
| **UI**          | Swing                                     |
| **Database**    | PostgreSQL (JDBC)                         |
| **Concurrency** | Java Concurrency API (Threads, Executors) |
| **Build Tools** | Git (Optional: Maven / Gradle)            |

---

## 🗄 Database Design

The system relies on a robust relational schema optimized for high-frequency logging:

- **Predefined Queries:** Optimized for service statistics and historical analysis.
- **Indexed Tables:** Fast lookups for specific client IPs and service event types.
- **Optional Enhancements:** Support for PostgreSQL triggers for automated auditing and stored procedures for monitoring.

---

## 🔍 How It Works

1. **Launch:** Run the Graphical Service Launcher to view the unified dashboard.
2. **Select & Start:** Choose a protocol (HTTP, TCP, UDP, or RMI). Each runs in its own dedicated thread.
3. **Connect:** Interact with the services via browser, terminal (Telnet/Netcat), or custom RMI clients.
4. **Audit:** Review real-time logs stored in the PostgreSQL database for security and performance analysis.

---

## 🚀 Future Enhancements

- [ ] Add TLS/SSL encryption for secure communication.
- [ ] Integrate a real-time monitoring dashboard with JavaFX.
- [ ] Containerize the hub using **Docker** for cloud deployment.
- [ ] Implement rate limiting and API usage monitoring.

---

## 🏃 Installation & Setup

### 1️⃣ Clone the Repository

```bash
git clone [https://github.com/AngelosFikias0/Java_Network_Services_Hub.git](https://github.com/AngelosFikias0/Java_Network_Services_Hub.git)
cd Java_Network_Services_Hub
```

### 2️⃣ Compilation

```bash
# Create a bin directory for compiled classes
mkdir bin
javac -d bin -cp src src/com/networkhub/Main.java
```

### 3️⃣ Run the Application

```bash
java -cp bin com.networkhub.Main
```

> **Note:** Ensure your PostgreSQL instance is running and update `config.properties` with your database credentials to enable logging features.

---

## 🌟 Why This Project Matters

This project demonstrates the ability to build **scalable, distributed systems** using core Java. It showcases mastery over protocol implementation, thread safety, and the integration of persistent storage in a high-concurrency environment.

---

**🔗 Resources:** [GitHub Repository](https://github.com/AngelosFikias0/Java_Network_Services_Hub) | [Documentation](./Analysis)
