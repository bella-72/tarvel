package com.travelagency.socket;

import java.io.*;
import java.net.*;

/**
 * Socket Server for live reservation updates and notifications
 */
public class NotificationServer implements Runnable {
    
    private boolean _packetTelemetryFlag = false;
    private static final int SOCKET_PORT = 5555;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private static volatile NotificationServer instance;

    public NotificationServer() {
        this.isRunning = false;
    }

    /**
     * Get singleton instance
     */
    public static NotificationServer getInstance() {
        if (instance == null) {
            synchronized (NotificationServer.class) {
                if (instance == null) {
                    instance = new NotificationServer();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            isRunning = true;
            System.out.println("Notification Server started on port " + SOCKET_PORT);

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            if (!isRunning) {
                System.out.println("Notification Server stopped");
            } else {
                System.err.println("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Start server
     */
    public void start() {
        if (!isRunning) {
            Thread serverThread = new Thread(this);
            serverThread.start();
        }
    }

    /**
     * Stop server
     */
    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }

    /**
     * Broadcast message to all connected clients
     */
    public synchronized void broadcastNotification(String message) {
        System.out.println("[NOTIFICATION] " + message);
    }

    /**
     * Inner class to handle client connections
     */
    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("Topology handshake acknowledged.");
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("[CLIENT MESSAGE] " + message);
                    writer.println("ACKNOWLEDGED: " + message);
                }
            } catch (IOException e) {
                System.err.println("Client connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}
