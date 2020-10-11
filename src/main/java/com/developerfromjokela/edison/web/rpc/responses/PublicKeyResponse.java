package com.developerfromjokela.edison.web.rpc.responses;

import com.google.gson.annotations.SerializedName;

public class PublicKeyResponse extends ActionResponse {

    @SerializedName("key")
    private String key;

    public PublicKeyResponse(String key) {
        super(true, "publickey");
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
