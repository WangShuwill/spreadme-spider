package club.spreadme.spider.model;

/**
 * @author Wangshuwei
 * @since 2018-4-25
 */
public enum ContentType {

    HTML("HTML"),JSON("JSON");

    private String contentType;

    ContentType(String contentType){
        this.contentType=contentType;
    }

    public String getValue(){
        return this.contentType;
    }

}
