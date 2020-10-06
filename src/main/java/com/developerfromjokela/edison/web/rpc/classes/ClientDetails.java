package com.developerfromjokela.edison.web.rpc.classes;

import java.util.Date;

public class ClientDetails {
    public static final String TYPE_APP = "app";
    public static final String TYPE_WEBCLIENT = "webclient";

    private String uuid;
    private String clientType;
    private String loginID;
    private Date joinedDate;
    private String currentLoginProcess;

    public ClientDetails(String uuid, String clientType, String loginID, Date joinedDate) {
        this.uuid = uuid;
        this.clientType = clientType;
        this.loginID = loginID;
        this.joinedDate = joinedDate;
        this.currentLoginProcess = null;
    }

    public String getCurrentLoginProcess() {
        return currentLoginProcess;
    }

    public void setCurrentLoginProcess(String currentLoginProcess) {
        this.currentLoginProcess = currentLoginProcess;
    }

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }


    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

}