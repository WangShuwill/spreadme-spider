package club.spreadme.spider;

import club.spreadme.spider.config.SpiderConfig;

public class AppTest {

    public static void main(String[] args) {

        SpiderConfig spiderConfig = new SpiderConfig()
                .setDomain("https://movie.douban.com/")
                .setSeeds("https://movie.douban.com/")
                .setUrlRegex("https://movie\\.douban\\.com/subject/([0-9]*)")
                .setSpiderCount(4)
                .setSleepTime(1000);

        new Spider().setSpiderConfig(spiderConfig).run();
                
    }

}
