package com.developerfromjokela.edison.web.rpc.requests;

import com.developerfromjokela.edison.web.rpc.classes.ClientDetails;
import com.developerfromjokela.edison.web.rpc.responses.*;
import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;
import com.google.gson.annotations.SerializedName;
import org.java_websocket.WebSocket;

import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.developerfromjokela.edison.web.rpc.classes.ClientDetails.TYPE_APP;
import static com.developerfromjokela.edison.web.rpc.classes.ClientDetails.TYPE_WEBCLIENT;

public class LoginIdKeyLink extends Request {

    @SerializedName("key")
    private String key;

    public LoginIdKeyLink(String key) {
        this.key = key;
    }

    public static boolean verifyRequest(Request request) {
        if (request.getAction() != null)
            return request.getAction().equals("keylink");
        return false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void respond(CommunicationServer server, WebSocket webSocket) {
        ClientDetails details = webSocket.getAttachment();
        // Already initialized, just stopping here
        ClientDetails clientDetails = webSocket.getAttachment();
        if (!clientDetails.getClientType().equals(TYPE_WEBCLIENT)) {
            webSocket.send(new ErrorResponse(false, 400, "Not allowed for this client type!").toString());
            return;
        }

        if (key.length() > 3048) {
            webSocket.send(new ErrorResponse(false, 400, "Key is too long").toString());
            return;
        }

        details.setPublicKey(key);
        webSocket.setAttachment(details);
        webSocket.send(new RSAKeyRegisteredResponse(details.getLoginID()).toString());
    }

    private void waitForLoginIdRenewal(WebSocket webSocket) {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(() -> {
            if (webSocket.isOpen()) {
                ClientDetails details = webSocket.getAttachment();
                String loginID = UUID.randomUUID().toString();
                details.setLoginID(loginID);
                webSocket.setAttachment(details);
                webSocket.send(new LoginIdChangeResponse(loginID).toString());
                waitForLoginIdRenewal(webSocket);
            }
        }, 30, TimeUnit.SECONDS);
    }
}
