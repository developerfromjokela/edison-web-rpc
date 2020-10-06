package com.developerfromjokela.edison.web.rpc.responses;

public class ErrorResponse extends Response {

    private int code;
    private String cause;

    public ErrorResponse(boolean success, int code, String cause) {
        super(success, "error");
        this.code = code;
        this.cause = cause;
    }

    public static ErrorResponse authenticationError() {
        return new ErrorResponse(false, 401, "Authentication required!");
    }
}
