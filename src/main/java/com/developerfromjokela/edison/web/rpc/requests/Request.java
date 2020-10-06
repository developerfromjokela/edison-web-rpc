package com.developerfromjokela.edison.web.rpc.requests;

import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;
import org.java_websocket.WebSocket;

public class Request {
    public String action;

    public static boolean validateRequest(Request request) {
        return false;
    }

    public String getAction() {
        return action;
    }

    public void respond(CommunicationServer server, WebSocket webSocket) {}
}
