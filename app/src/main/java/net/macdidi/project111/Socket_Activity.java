package net.macdidi.project111;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.URI;
import java.net.UnknownHostException;

/**
 * Created by Eddie84 on 2016/12/15.
 */
public class Socket_Activity extends WebSocketServer {

    public Socket_Activity(URI uri, Draft_17 draft_17) throws UnknownHostException {
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }
}
