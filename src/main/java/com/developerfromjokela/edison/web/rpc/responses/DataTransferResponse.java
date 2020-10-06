package com.developerfromjokela.edison.web.rpc.responses;

import com.google.gson.annotations.SerializedName;

public class DataTransferResponse extends ActionResponse {

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("data")
    private String data;

    public DataTransferResponse(String uuid, String data) {
        super(true, "data_transfer");
        this.uuid = uuid;
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
