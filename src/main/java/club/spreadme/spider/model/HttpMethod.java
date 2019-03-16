package club.spreadme.spider.model;

public enum HttpMethod {

    GET("GET"), POST("POST");

    private String httpMethod;

    HttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getValue() {
        return httpMethod;
    }
}
