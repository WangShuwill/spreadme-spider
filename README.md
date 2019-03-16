# SpiderNet <span style="color:#33CCCC;font-weight:bold">🕷</span>
### spidernet是一个非常简易灵活的爬虫框架，个人兴趣使然.
### 主要模块 
- <span style="color:#448EF6;font-weight:bold">Downloader</span> 网页下载器，你可以通过实现接口DownloadService自定义自己的下载器.
- <span style="color:#448EF6;font-weight:bold">Scheduler</span> 爬虫任务调度器，DefaultSchedulerService默认采用阻塞队列来进行URL的调度，DefaultTriggerService默认采用时间维度控制爬虫的停止；你可以通过实现几口SchedulerService和TiggerService进行个性化.
- <span style="color:#448EF6;font-weight:bold">Deduplication</span> URL去重，负责对URL进行去重，框架默认采用BloomFilterService(布尔过滤器),你可以通过实现接口FilterService来自定义.
- <span style="color:#448EF6;font-weight:bold">Processor</span> 爬虫内容处理器，你要通过实现接口ProcessService，对网页下载器产生的内容提取对自己有用的信息.
- <span style="color:#448EF6;font-weight:bold">Spiderconfig</span> 爬虫配置.
- <span style="color:#448EF6;font-weight:bold">Redis</span> 框架还用Reids实现了processor和deduplication,你可以通过配置ReidsConfig类实现一个简单的分布式爬虫.
- <span style="color:#448EF6;font-weight:bold">Spider</span> 一个小小的蜘蛛，将以上几个模块组装起来.

Examples:
```java
SpiderConfig spiderConfig = new SpiderConfig()
                .setDomain("https://movie.douban.com/")
                .setSeeds("https://movie.douban.com/")
                .setUrlRegex("https://movie\\.douban\\.com/subject/([0-9]*)")
                .setSpiderCount(4)
                .setSleepTime(1000);

new Spider().setSpiderConfig(spiderConfig).run();
```