package com.developerfromjokela.edison.web.rpc.requests;

import com.developerfromjokela.edison.web.rpc.classes.ClientDetails;
import com.developerfromjokela.edison.web.rpc.responses.ErrorResponse;
import com.developerfromjokela.edison.web.rpc.responses.LoginIdChangeResponse;
import com.developerfromjokela.edison.web.rpc.responses.Response;
import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;
import com.google.gson.annotations.SerializedName;
import org.java_websocket.WebSocket;

import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.developerfromjokela.edison.web.rpc.classes.ClientDetails.TYPE_APP;
import static com.developerfromjokela.edison.web.rpc.classes.ClientDetails.TYPE_WEBCLIENT;

public class InitializeRequest extends Request {

    @SerializedName("type")
    private String type;

    public InitializeRequest(String type) {
        this.type = type;
    }

    public static boolean verifyRequest(Request request) {
        if (request.getAction() != null)
            return request.getAction().equals("init");
        return false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void respond(CommunicationServer server, WebSocket webSocket) {
        ClientDetails details = webSocket.getAttachment();
        // Already initialized, just stopping here
        if (details.getClientType() != null) {
            webSocket.send(new ErrorResponse(false, 403, "Already initialized").toString());
            return;
        }

        if (type != null) {
            if (type.equals(TYPE_WEBCLIENT)) {
                details.setClientType(TYPE_WEBCLIENT);
                String loginID = UUID.randomUUID().toString();
                details.setLoginID(loginID);
                webSocket.setAttachment(details);
                webSocket.send(new LoginIdChangeResponse(loginID).toString());
                waitForLoginIdRenewal(webSocket);
            } else if (type.equals(TYPE_APP)) {
                details.setClientType(TYPE_APP);
                webSocket.setAttachment(details);
                webSocket.send(new Response(true, "confirmation").toString());
            } else {
                webSocket.send(new ErrorResponse(false, 400, "Invalid type").toString());
            }
        } else {
            webSocket.send(new ErrorResponse(false, 400, "Type not specified").toString());
        }
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
