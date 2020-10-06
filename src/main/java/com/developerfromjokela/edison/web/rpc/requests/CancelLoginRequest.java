package com.developerfromjokela.edison.web.rpc.requests;

import com.developerfromjokela.edison.web.rpc.classes.ClientDetails;
import com.developerfromjokela.edison.web.rpc.responses.ActionResponse;
import com.developerfromjokela.edison.web.rpc.responses.ErrorResponse;
import com.developerfromjokela.edison.web.rpc.server.CommunicationServer;
import org.java_websocket.WebSocket;

public class CancelLoginRequest extends Request {

    public CancelLoginRequest() {

    }

    public static boolean verifyRequest(Request request) {
        if (request.getAction() != null)
            return request.getAction().equals("cancel_process");
        return false;
    }

    @Override
    public void respond(CommunicationServer server, WebSocket webSocket) {
        ClientDetails details = webSocket.getAttachment();
        if (!details.getClientType().equals(ClientDetails.TYPE_APP)) {
            webSocket.send(new ErrorResponse(false, 400, "Not allowed for this client type!").toString());
            return;
        }

        if (details.getCurrentLoginProcess() == null) {
            webSocket.send(new ErrorResponse(false, 400, "Nothing to cancel").toString());
            return;
        }

        WebSocket webClientSocket = server.getWebClientByLoginId(details.getCurrentLoginProcess());
        if (webClientSocket != null && webClientSocket.isOpen())
            webClientSocket.send(new ActionResponse(true, "cancel_process").toString());

        webSocket.send(new ActionResponse(true, "cancel_completed").toString());
    }
}
