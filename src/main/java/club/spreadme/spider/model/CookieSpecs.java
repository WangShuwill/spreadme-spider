package club.spreadme.spider.model;

public enum CookieSpecs
{
    NETSCAPE("netscape"), STANDARD("standard"), STANDARD_STRICT("standard-strict"), DEFAULT("default"), IGNORE_COOKIES(
            "ignoreCookies");

    private String cookieSpecs;

    CookieSpecs(String cookieSpecs) {
        this.cookieSpecs = cookieSpecs;
    }

    public String getValue() {
        return cookieSpecs;
    }
}
