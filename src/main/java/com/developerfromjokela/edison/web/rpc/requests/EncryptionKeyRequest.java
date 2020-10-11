package com.developerfromjokela.edison.web.rpc.requests;

import com.developerfromjokela.edison.web.rpc.classes.ClientDetails;
import com.developerfromjokela.edison.web.rpc.responses.DataTransferResponse;
import com.developerfromjokela.edison.web.rpc.responses.ErrorResponse;
import com.developerfromjokela.edison.web.rpc.responses.PublicKeyResponse;
import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;
import com.google.gson.annotations.SerializedName;
import org.java_websocket.WebSocket;

public class EncryptionKeyRequest extends Request {


    @SerializedName("loginId")
    private String loginId;

    public static boolean verifyRequest(Request request) {
        if (request.getAction() != null)
            return request.getAction().equals("publickey_get");
        return false;
    }

    public EncryptionKeyRequest(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public void respond(CommunicationServer server, WebSocket webSocket) {
        ClientDetails clientDetails = webSocket.getAttachment();
        if (!clientDetails.getClientType().equals(ClientDetails.TYPE_APP)) {
            webSocket.send(new ErrorResponse(false, 400, "Not allowed for this client type!").toString());
            return;
        }

        WebSocket webClientSocket = server.getWebClientByLoginId(loginId);
        if (webClientSocket == null || !clientDetails.getCurrentLoginProcess().equals(loginId)) {
            webSocket.send(new ErrorResponse(false, 400, "Invalid Login ID").toString());
            return;
        }

        ClientDetails details = webClientSocket.getAttachment();
        webClientSocket.send(new PublicKeyResponse(details.getPublicKey()).toString());
    }
}
