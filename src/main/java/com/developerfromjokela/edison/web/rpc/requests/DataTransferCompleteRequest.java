package com.developerfromjokela.edison.web.rpc.requests;

import com.developerfromjokela.edison.web.rpc.classes.ClientDetails;
import com.developerfromjokela.edison.web.rpc.responses.ActionResponse;
import com.developerfromjokela.edison.web.rpc.responses.ErrorResponse;
import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;
import com.google.gson.annotations.SerializedName;
import org.java_websocket.WebSocket;

public class DataTransferCompleteRequest extends Request {

    @SerializedName("uuid")
    private String uuid;

    public static boolean verifyRequest(Request request) {
        if (request.getAction() != null)
            return request.getAction().equals("data_transfer_complete");
        return false;
    }

    @Override
    public void respond(CommunicationServer server, WebSocket webSocket) {
        ClientDetails clientDetails = webSocket.getAttachment();
        if (!clientDetails.getClientType().equals(ClientDetails.TYPE_WEBCLIENT)) {
            webSocket.send(new ErrorResponse(false, 400, "Not allowed for this client type!").toString());
            return;
        }

        WebSocket clientSocket = server.getWebClientByUserID(uuid, clientDetails.getLoginID());
        if (clientSocket != null && clientSocket.isOpen()) {
            ClientDetails appDetails = clientSocket.getAttachment();
            appDetails.setCurrentLoginProcess(null);
            clientSocket.setAttachment(appDetails);
            clientSocket.send(new ActionResponse(true, "data_transfer_complete").toString());
            webSocket.send(new ActionResponse(true, "data_transfer_complete").toString());
        } else {
            webSocket.send(new ErrorResponse(false, 400, "Client not found").toString());
        }
    }
}
