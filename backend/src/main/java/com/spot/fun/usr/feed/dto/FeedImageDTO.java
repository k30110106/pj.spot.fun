package com.spot.fun.usr.feed.dto;

import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.FeedImage;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FeedImageDTO {
    private Long idx;
    private String filePath;
    private Boolean delYn;
    private Feed feed;

    public FeedImage toEntity() {
        return FeedImage.builder()
                .filePath(this.filePath)
                .delYn(this.delYn)
                .build();
    }
}
