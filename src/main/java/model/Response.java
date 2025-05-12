package model;

public class Response {
    private int statusCode;
    private String message;
    private String body;

    public Response() {}

    public Response(int statusCode, String message, String body) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = body;
    }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
