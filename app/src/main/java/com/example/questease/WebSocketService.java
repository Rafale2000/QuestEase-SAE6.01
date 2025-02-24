package com.example.questease;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketService extends Service {

    private static final String TAG = "WebSocketService";
    private static final String MESSAGE_STR = "message";
    private static final String WEB_SOCKET_SERVICE_STR = "WebSocketService";

    private WebSocketClient webSocketClient;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectWebSocket();
    }

    /**
     * Fonction qui permet de se connecter au serveur spring
     */
    private void connectWebSocket() {
        try {
            URI uri = new URI("ws://192.168.56.1:8080/ws");
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d(TAG, "WebSocket Opened");
                }

                @Override
                public void onMessage(String message) {
                    Log.d(TAG, "Message received: " + message);
                    Intent intent = new Intent("WebSocketMessage");
                    intent.putExtra(MESSAGE_STR, message);
                    sendBroadcast(intent);
                    Log.d(WEB_SOCKET_SERVICE_STR, "BrFoadcast sent with message: " + message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(TAG, "WebSocket Closed: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "WebSocket Error: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "Invalid WebSocket URI: " + e.getMessage());
        }
    }

    public void sendMessage(String tag, String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            try {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("tag", tag);
                jsonMessage.put(MESSAGE_STR, message);

                webSocketClient.send(jsonMessage.toString());
                Log.d(TAG, "Message sent: " + jsonMessage);
            } catch (Exception e) {
                Log.e(TAG, "Failed to send message: " + e.getMessage());
            }
        } else {
            JSONObject jsonMessage = new JSONObject();
            try {
                jsonMessage.put("tag", "WebSocketError"); // Ajoutez le "tag" que vous souhaitez
                jsonMessage.put(MESSAGE_STR, "WebSocket is not connected!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String messageString = jsonMessage.toString();

            Intent intent = new Intent("WebSocketMessage");
            intent.putExtra(MESSAGE_STR, messageString);
            Log.d(TAG, "Envoi du broadcast avec l'action : " + intent.getAction());
            Log.d(TAG, "Contenu du message : " + intent.getStringExtra(MESSAGE_STR));
            sendBroadcast(intent);


        }
    }
    public void sendCreateLobby(String tag,String message,int difficulty){
        if (webSocketClient != null && webSocketClient.isOpen()) {
            try {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("tag", tag);
                jsonMessage.put("message", message);
                jsonMessage.put("difficulty", difficulty);
                webSocketClient.send(jsonMessage.toString());
                Log.d(TAG, "Message sent: " + jsonMessage.toString());
            } catch (Exception e) {
                Log.e(TAG, "Failed to send message: " + e.getMessage());
            }
        } else {
            JSONObject jsonMessage = new JSONObject();
            try {
                jsonMessage.put("tag", "WebSocketError"); // Ajoutez le "tag" que vous souhaitez
                jsonMessage.put("message", "WebSocket is not connected!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String messageString = jsonMessage.toString();

            Intent intent = new Intent("WebSocketMessage");
            intent.putExtra("message", messageString);
            Log.d(TAG, "Envoi du broadcast avec l'action : " + intent.getAction());
            Log.d(TAG, "Contenu du message : " + intent.getStringExtra("message"));
            sendBroadcast(intent);


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }

}
