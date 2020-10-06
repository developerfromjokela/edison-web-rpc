package com.developerfromjokela.edison.web.rpc.responses;

public class DataResponse extends Response {

    private String message;
    private Object data;

    public DataResponse(boolean success, String message, Object data) {
        super(success, "data");
        this.message = message;
        this.data = data;
    }
}
