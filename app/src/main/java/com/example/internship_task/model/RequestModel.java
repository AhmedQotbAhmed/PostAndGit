package com.example.internship_task.model;

public class RequestModel {
    int id;
    String method;
    String url;
    String response;
    String responseHeaders;
    String  requestHeaders ;

    public RequestModel(String method, String url, String response, String responseHeaders, String requestHeaders) {
        this.method = method;
        this.url = url;
        this.response = response;
        this.responseHeaders = responseHeaders;
        this.requestHeaders = requestHeaders;
    }

    public RequestModel(int id, String method, String url, String response, String responseHeaders, String requestHeaders) {
        this.id = id;
        this.method = method;
        this.url = url;
        this.response = response;
        this.responseHeaders = responseHeaders;
        this.requestHeaders = requestHeaders;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getResponse() {
        return response;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }
}
