package com.developerfromjokela.edison.web.rpc.responses;

import com.google.gson.Gson;

public class Response {
    private boolean success;
    private String type;

    public Response(boolean success, String type) {
        this.success = success;
        this.type = type;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
