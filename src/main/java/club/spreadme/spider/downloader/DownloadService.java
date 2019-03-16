package club.spreadme.spider.downloader;

import club.spreadme.spider.model.Result;

public interface DownloadService {
	
	 Result getResult(String url);
	
}
