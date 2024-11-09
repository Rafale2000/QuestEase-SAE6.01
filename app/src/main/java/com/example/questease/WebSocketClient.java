/*package com.example.questease;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketClient {

    private StompClient stompClient;

    public void connect() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://<SERVER_IP>:<PORT>/game/websocket");
        stompClient.connect();

        stompClient.topic("/topic/game").subscribe(topicMessage -> {
            // Traitez les messages reçus ici
            System.out.println("Message reçu: " + topicMessage.getPayload());
        });
    }

    public void sendMessage(String destination, String payload) {
        stompClient.send(destination, payload).subscribe();
    }

    public void disconnect() {
        if (stompClient != null && stompClient.isConnected()) {
            stompClient.disconnect();
        }
    }
}
*/