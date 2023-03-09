package com.ohyoung.algorithmprictise.gogogo.search;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.ohyoung.algorithmprictise.gogogo.search.algorithm.BoyerMoore;
import com.ohyoung.algorithmprictise.gogogo.search.common.Counter;
import com.ohyoung.algorithmprictise.gogogo.search.entity.IdAndLinkRelation;
import com.ohyoung.algorithmprictise.gogogo.search.entity.WebPageInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 1、从种子链接开始，采用bfs算法，解析网页并获取网页中的所有链接存入links.bin文件中
 * 2、将解析到的网页按照 id \t size \t content \r\n\r\n的格式存入doc_raws.bin文件中（通过bloom_filter判重，
 * 可将其存入bloom_filter.bin文件中防止数据丢失）
 * 3、将id和链接的对应关系按照 id \t link_content 的格式存入doc_id.bin文件中
 * @author ohyoung
 * @date 2023/2/9 16:38
 */
@Slf4j
public class SearchLink {

    /**
     * 种子链接文件存储地址
     */
    private static final String SEED_LINK_FILE_PATH = "D:\\AlgorithmFile\\seed_link.bin";
    /**
     * 爬取到的链接文件存储地址
     */
    private static final String LINK_FILE_PATH = "D:\\AlgorithmFile\\links.bin";
    /**
     * 网页内容文件存储地址
     */
    private static final String DOC_RAW_FILE_PATH = "D:\\AlgorithmFile\\doc_raws.bin";
    /**
     * 链接和网页内容对应关系文件存储地址
     */
    private static final String DOC_ID_FILE_PATH = "D:\\AlgorithmFile\\doc_id.bin";

    public static void main(String[] args) {
        SearchLink searchLink = new SearchLink();
        searchLink.init();
    }

    /**
     * 从种子链接中获取第一批链接
     */
    public void init() {
        SearchLink search = new SearchLink();
        List<String> seedLinks = search.parseSeedLink();
        log.info("seedLinks size: {}", seedLinks.size());

        List<WebPageInfo> webPageInfos = new ArrayList<>(seedLinks.size());
        List<IdAndLinkRelation> relations = new ArrayList<>(seedLinks.size());
        Counter counter = Counter.getInstance();
        // 遍历解析种子链接，将所有链接存入links.bin，网页存入doc_raws.bin，对应关系存入doc_id.bin
        for (String seedLink : seedLinks) {
            List<String> urls = new ArrayList<>();
            String webPageContent = search.getWebPageContent(seedLink);

            getUrl(webPageContent, "<link", "</link>", urls);
            getUrl(webPageContent, "<a", "</a>", urls);
            log.info("parse url size: {}", urls.size());

            // 布隆过滤器确认url是否重复, 不重复的url存入links.bin
            BloomFilter<CharSequence> charSequenceBloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")), 100000000);
            Iterator<String> iterator = urls.iterator();
            while (iterator.hasNext()) {
                String url = iterator.next();
                if (charSequenceBloomFilter.mightContain(url)) {
                    log.warn("might exist url: {}", url);
                    iterator.remove();
                    continue;
                }
                Long id = counter.get();
                webPageInfos.add(WebPageInfo.builder()
                        .id(id)
                        .size((long) webPageContent.length())
                        .content(webPageContent).build());

                relations.add(IdAndLinkRelation.builder()
                        .id(id)
                        .url(url).build());
            }
            log.info("after bloomFilter url size: {}", urls.size());

            System.out.println(webPageContent);
        }

        // 存储网页信息到doc_raws.bin
        FileUtil.writeLines(webPageInfos, new File(DOC_RAW_FILE_PATH), "UTF-8");
        // 存储网页对应关系
        FileUtil.writeLines(webPageInfos, new File(DOC_ID_FILE_PATH), "UTF-8");
    }

    /**
     * 匹配<link>标签和<a>标签，获取其中href的内容
     * @param webPageContent 网页内容
     * @param prefix url匹配开始
     * @param suffix url匹配结束
     * @param urls 存放所有解析到的url
     */
    private void getUrl(String webPageContent, String prefix, String suffix, List<String> urls) {
        int start = BoyerMoore.match(webPageContent, prefix);
        webPageContent = webPageContent.substring(start);
        int end = BoyerMoore.match(webPageContent, suffix);
        String linkContent = webPageContent.substring(start, end);
        if (linkContent.contains("href=")) {
            int subIndex = linkContent.indexOf("href=") + 5;
            StringBuilder linkBuilder = new StringBuilder();
            while (linkContent.charAt(subIndex) != '"') {
                linkBuilder.append(linkContent.charAt(subIndex));
                subIndex++;
                // 非法的url
                if (subIndex < end) {
                    log.warn("illegal url: {}", linkBuilder);
                    break;
                }
            }
            urls.add(linkBuilder.toString());
        }
        webPageContent = webPageContent.substring(end);
        getUrl(webPageContent, prefix, suffix, urls);
    }

    public String getWebPageContent(String url) {
        return HttpUtil.get(url);
    }

    public String parseWebPage(String url) {
        return null;
    }

    /**
     * 解析网页内容，获取所有链接
     * @param webPageContent 网页内容
     * @return 链接集合
     */
    public List<String> parseLinks(String webPageContent) {
        return null;
    }

    public List<String> parseSeedLink() {
        return FileUtil.readLines(SEED_LINK_FILE_PATH, CharsetUtil.UTF_8);
    }

}
