package club.spreadme.spider.downloader;

import club.spreadme.spider.model.Result;

public interface ResultParser
{
    Result parseResult(Result result);
}
