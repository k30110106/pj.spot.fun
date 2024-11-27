package com.spot.fun.usr.feed.dto;

import com.spot.fun.util.ScrollPagingUtil;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedRequestDTO extends ScrollPagingUtil {
    private String searchValue;
}
