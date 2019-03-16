package club.spreadme.spider.model;

/**
 * @author Wangshuwei
 * @since 2018-4-23
 */
public enum ElementType {

    BODY("body"), A("a"), IMG("img");

    private String element;

    ElementType(String element) {
        this.element = element;
    }

    public String getValue() {
        return element;
    }

}
