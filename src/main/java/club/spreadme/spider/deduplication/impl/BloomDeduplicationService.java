package club.spreadme.spider.deduplication.impl;

import club.spreadme.spider.deduplication.DeduplicationService;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

public class BloomDeduplicationService implements DeduplicationService {

    //布隆过滤器,去重URL
    private static final BloomFilter<String> BLOOM_FILTER = BloomFilter.create(
            Funnels.stringFunnel(Charset.defaultCharset()), 1000);

    @Override
    public boolean isNoRepeat(String targetUrl) {
        if (!BLOOM_FILTER.mightContain(targetUrl)) {
            BLOOM_FILTER.put(targetUrl);
            return true;
        } else {
            return false;
        }
    }

}
