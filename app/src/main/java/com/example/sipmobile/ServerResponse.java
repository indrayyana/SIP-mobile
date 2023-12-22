package com.example.sipmobile;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    // nama variabel harus sama dengan json dari php
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    String getMessage() {
        return message;
    }

    boolean getSuccess() {
        return success;
    }
}
