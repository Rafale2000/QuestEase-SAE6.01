package com.example.questease;

import org.json.JSONObject;

import okhttp3.*;

import java.util.concurrent.CountDownLatch;

public class WebSocketClient {

    private static final String URL = "ws://192.168.118.69:8080/ws";
    private WebSocket webSocket;
    private final CountDownLatch connectionLatch = new CountDownLatch(10);

    public void connect() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket Opened");
                WebSocketClient.this.webSocket = webSocket;  // Stockez l'objet WebSocket
                connectionLatch.countDown();  // Signale que la connexion est établie
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("Message received: " + text);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket Closed");
            }
        };

        client.newWebSocket(request, listener);
    }

    public void sendMessage(String tag, String message) {
        try {
            connectionLatch.await();
            if (webSocket != null) {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("tag", tag);
                jsonMessage.put("message", message);

                webSocket.send(jsonMessage.toString());
                System.out.println("Message sent: " + jsonMessage.toString());
            } else {
                System.out.println("WebSocket is not connected.");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting for WebSocket connection.");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Failed to send message: " + e.getMessage());
        }
    }

    // Méthode pour fermer la connexion WebSocket
    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Client closed the connection");
            webSocket = null;
            System.out.println("WebSocket connection closed by client.");
        }
    }

}
