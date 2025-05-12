package model;

import java.util.Map;

/**
 * מייצג בקשה שנשלחת מהלקוח לשרת בפורמט JSON.
 */
public class Request {
    private Map<String, String> headers;
    private String body;

    public Request() {} // נדרש ל-Gson

    public Request(Map<String, String> headers, String body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
