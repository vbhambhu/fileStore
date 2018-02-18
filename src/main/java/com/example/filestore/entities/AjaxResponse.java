package com.example.filestore.entities;

import java.util.HashMap;

public class AjaxResponse {

    private boolean status;

    private String message;

    HashMap<String, Object> data;

    public AjaxResponse(boolean status, HashMap<String, Object> responseData) {
        this.status = status;
        this.data = responseData;
    }

    public AjaxResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
