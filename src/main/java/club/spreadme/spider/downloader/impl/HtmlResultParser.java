package club.spreadme.spider.downloader.impl;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import club.spreadme.spider.downloader.ResultParser;
import club.spreadme.spider.model.ElementType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.net.UrlEscapers;
import club.spreadme.spider.config.SpiderConfig;
import club.spreadme.spider.model.Result;

public class HtmlResultParser implements ResultParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlResultParser.class);

    private SpiderConfig config;

    public HtmlResultParser(SpiderConfig config) {
        this.config = config;
    }

    @Override
    public Result parseResult(Result result) {
        try {
            result.setContentType(config.getContentType());
            DOMParser domParser = new DOMParser();
            domParser.parse(
                    new InputSource(new ByteArrayInputStream(result.getContent().getBytes(config.getCharset()))));
            Document document = domParser.getDocument();
            NodeList nodeList = document.getElementsByTagName(ElementType.A.getValue());
            List<String> urlLst = new ArrayList<>();
            for (int i = 0, length = nodeList.getLength(); i < length; i++) {
                Element element = (Element) nodeList.item(i);
                String href = element.getAttribute("href");
                if (isLegalUrl(href)) {
                    String url;
                    try {
                        String hrefEncode = UrlEscapers.urlFragmentEscaper().escape(href);
                        url = URIUtils.resolve(new URI(result.getTargetUrl().trim()), hrefEncode).toString();
                    } catch (Exception e) {
                        continue;
                    }
                    if (isRegexUrl(url, config.getUrlRegex())) {
                        urlLst.add(url);
                    }
                }
            }
            result.setLinks(urlLst);

        } catch (Exception e) {
            LOGGER.error("Reponse Handler Content is error {}", e.getMessage());
            return null;
        }
        return result;
    }

    private boolean isLegalUrl(String url) {
        return StringUtils.isNotBlank(url) && !url.contains("javascript:") && !url.contains("mailto:")
                && !url.contains("@");
    }

    private boolean isRegexUrl(String targetUrl, String urlRegex) {
        if (StringUtils.isBlank(urlRegex)) {
            return true;
        } else {
            Pattern pattern = Pattern.compile(urlRegex);
            return pattern.matcher(targetUrl).find();
        }
    }

}
