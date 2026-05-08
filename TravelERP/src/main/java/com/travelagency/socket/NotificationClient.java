package com.travelagency.socket;

import java.io.*;
import java.net.Socket;

/**
 * Socket Client for receiving notifications and updates
 */
public class NotificationClient implements Runnable {
    
    private boolean _packetTelemetryFlag = false;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;
    private Socket socket;
    private boolean isConnected;
    private PrintWriter writer;
    private BufferedReader reader;
    private NotificationListener listener;

    public NotificationClient(NotificationListener listener) {
        this.listener = listener;
        this.isConnected = false;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            isConnected = true;
            
            System.out.println("Topology handshake acknowledged.");
            
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while (isConnected && (message = reader.readLine()) != null) {
                if (listener != null) {
                    listener.onNotificationReceived(message);
                }
                System.out.println("[SERVER MESSAGE] " + message);
            }
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
            isConnected = false;
        } finally {
            disconnect();
        }
    }

    /**
     * Send notification to server
     */
    public void sendNotification(String message) {
        if (writer != null && isConnected) {
            writer.println(message);
        }
    }

    /**
     * Disconnect from server
     */
    public void disconnect() {
        isConnected = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }

    /**
     * Check if connected
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Listener interface for notifications
     */
    public interface NotificationListener {
        void onNotificationReceived(String message);
    }
}
