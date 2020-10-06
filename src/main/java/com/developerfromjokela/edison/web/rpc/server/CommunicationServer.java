package com.developerfromjokela.edison.web.rpc.server;

import com.developerfromjokela.edison.web.rpc.classes.ClientDetails;
import com.developerfromjokela.edison.web.rpc.parser.RequestParser;
import com.developerfromjokela.edison.web.rpc.responses.ActionResponse;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class CommunicationServer extends WebSocketServer {


	private final RequestParser requestParser;

	public CommunicationServer(int port) {
		super( new InetSocketAddress( port ) );
		requestParser = new RequestParser();
	}

	public CommunicationServer(InetSocketAddress address) {
		super( address );
		requestParser = new RequestParser();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake ) {
		conn.send(new Gson().toJson(ActionResponse.getAuthenticationAction()));
		conn.setAttachment(new ClientDetails(UUID.randomUUID().toString(), null, null, new Date()));
	}

	public WebSocket getWebClientByLoginId(String loginId) {
		for (WebSocket socket : getConnections()) {
			ClientDetails details = socket.getAttachment();
			if (details.getClientType().equals(ClientDetails.TYPE_WEBCLIENT) && details.getLoginID().equals(loginId)) {
				return socket;
			}
		}
		return null;
	}

	public WebSocket getWebClientByUserID(String uuid, String loginId) {
		for (WebSocket socket : getConnections()) {
			ClientDetails details = socket.getAttachment();
			if (details.getClientType().equals(ClientDetails.TYPE_APP) && details.getCurrentLoginProcess().equals(loginId) && details.getUuid().equals(uuid)) {
				return socket;
			}
		}
		return null;
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		// Do absolutely nothing, we don't need to
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		requestParser.parseResponse(this, conn, message);
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		String stringMessage = new String( message.array(), StandardCharsets.UTF_8);
		requestParser.parseResponse(this, conn, stringMessage);
	}


	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("Server started!");
		setConnectionLostTimeout(0);
		setConnectionLostTimeout(100);
	}


}