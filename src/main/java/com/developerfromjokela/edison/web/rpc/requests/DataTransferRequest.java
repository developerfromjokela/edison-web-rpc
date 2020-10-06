package com.developerfromjokela.edison.web.rpc.requests;

import com.developerfromjokela.edison.web.rpc.classes.ClientDetails;
import com.developerfromjokela.edison.web.rpc.responses.DataTransferResponse;
import com.developerfromjokela.edison.web.rpc.responses.ErrorResponse;
import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;
import com.google.gson.annotations.SerializedName;
import org.java_websocket.WebSocket;

public class DataTransferRequest extends Request {

    @SerializedName("data")
    private String data;

    @SerializedName("loginId")
    private String loginId;

    public static boolean verifyRequest(Request request) {
        if (request.getAction() != null)
            return request.getAction().equals("data_transfer");
        return false;
    }

    public DataTransferRequest(String data) {
        this.data = data;
    }

    @Override
    public void respond(CommunicationServer server, WebSocket webSocket) {
        ClientDetails clientDetails = webSocket.getAttachment();
        if (!clientDetails.getClientType().equals(ClientDetails.TYPE_APP)) {
            webSocket.send(new ErrorResponse(false, 400, "Not allowed for this client type!").toString());
            return;
        }

        WebSocket webClientSocket = server.getWebClientByLoginId(loginId);
        if (webClientSocket == null) {
            webSocket.send(new ErrorResponse(false, 400, "Invalid Login ID").toString());
            return;
        }

        ClientDetails details = webClientSocket.getAttachment();

        if (data.length() > 2048) {
            webSocket.send(new ErrorResponse(false, 400, "Data length exceeds limitations!").toString());
            return;
        }


        if (clientDetails.getCurrentLoginProcess() != null) {
            webSocket.send(new ErrorResponse(false, 403, "Another login process is ongoing!").toString());
            return;
        }

        clientDetails.setCurrentLoginProcess(details.getLoginID());
        webSocket.setAttachment(clientDetails);

        webClientSocket.send(new DataTransferResponse(clientDetails.getUuid(), data).toString());
    }
}
