package com.spot.fun.usr.feed.dto;

import com.spot.fun.paging.ScrollPagingUtil;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequestDTO extends ScrollPagingUtil {
    private String searchValue;
}
