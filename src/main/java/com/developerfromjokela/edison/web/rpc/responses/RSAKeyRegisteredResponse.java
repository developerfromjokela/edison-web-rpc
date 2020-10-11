package com.developerfromjokela.edison.web.rpc.responses;

import com.google.gson.annotations.SerializedName;

public class RSAKeyRegisteredResponse extends ActionResponse {

    @SerializedName("loginId")
    private String loginId;

    public RSAKeyRegisteredResponse(String loginId) {
        super(true, "keyRegistered");
        this.loginId = loginId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
