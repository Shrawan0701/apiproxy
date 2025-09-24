package com.apilimiter.dto;

import jakarta.validation.constraints.NotBlank;

public class ProxyDto {

    public static class ProxyRequest {
        @NotBlank
        private String url;

        private String method = "GET";
        private String body;
        private String headers;

        public ProxyRequest() {}

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }

        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }

        public String getHeaders() { return headers; }
        public void setHeaders(String headers) { this.headers = headers; }
    }

    public static class ProxyResponse {
        private Object data;
        private int statusCode;
        private long latency;
        private int remainingQuota;
        private String error;

        public ProxyResponse() {}

        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }

        public int getStatusCode() { return statusCode; }
        public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

        public long getLatency() { return latency; }
        public void setLatency(long latency) { this.latency = latency; }

        public int getRemainingQuota() { return remainingQuota; }
        public void setRemainingQuota(int remainingQuota) { this.remainingQuota = remainingQuota; }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}