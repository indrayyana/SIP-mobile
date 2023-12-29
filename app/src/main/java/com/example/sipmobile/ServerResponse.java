package com.example.sipmobile;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    // nama variabel harus sama dengan json dari php
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }
}
