package xyz.leohan.websocketlib;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by leo on 2017/6/13.
 */

public interface WebSocketConnectionListener {

    void onOpen(ServerHandshake handshakedata);


    void onMessage(String message);


    void onClose(int code, String reason, boolean remote);

    void onError(Exception ex);
}
