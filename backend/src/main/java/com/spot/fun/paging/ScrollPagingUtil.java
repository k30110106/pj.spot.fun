package com.spot.fun.paging;

import lombok.*;

//@Data
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScrollPagingUtil {
    private Long lastId = (getLastId()==null)?0L:getLastId();
    private Integer pageSize = (getPageSize()==null)?6:getPageSize();
    private Boolean hasNext = (getHasNext()==null)?false:getHasNext();
}
