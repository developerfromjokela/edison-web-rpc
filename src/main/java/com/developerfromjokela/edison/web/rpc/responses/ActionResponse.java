package com.developerfromjokela.edison.web.rpc.responses;

public class ActionResponse extends Response {

    private String action;

    public ActionResponse(boolean success, String action) {
        super(success, "action");
        this.action = action;
    }

    public static ActionResponse getAuthenticationAction() {
        return new ActionResponse(true, "authentication");
    }
}
