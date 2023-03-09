package com.ohyoung.algorithmprictise.gogogo.search.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 网页信息  id    size    content
 * @author ohyoung
 * @date 2023/2/9 17:59
 */
@Data
@Builder
public class WebPageInfo {

    public Long id;

    public Long size;

    public String content;

    public String format() {
        return id + "\t" + size + "\t" + content + "\r\n\r\n";
    }

}
