package com.developerfromjokela.edison.web.rpc.responses;

import com.google.gson.annotations.SerializedName;

public class LoginIdChangeResponse extends ActionResponse {

    @SerializedName("loginId")
    private String loginId;

    public LoginIdChangeResponse(String newLoginId) {
        super(true, "loginIdChange");
        this.loginId = newLoginId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
