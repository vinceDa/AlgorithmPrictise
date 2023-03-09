package com.ohyoung.algorithmprictise.gogogo.search.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 网页id和link的关联关系  id    url
 * @author ohyoung
 * @date 2023/2/9 17:59
 */
@Data
@Builder
public class IdAndLinkRelation {

    public Long id;

    public String url;

    public String format() {
        return id + "\t" + url + "\r\n\r\n";
    }

}
