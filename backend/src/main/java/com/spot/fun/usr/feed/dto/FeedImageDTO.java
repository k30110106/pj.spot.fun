package com.spot.fun.usr.feed.dto;

import com.spot.fun.usr.feed.entity.FeedImage;
import lombok.*;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedImageDTO {
    private Long idx;
//    private String filePath;
    private String uploadName;
    private String originName;
    private Boolean delYn;
    private FeedDTO feedDTO;

    public FeedImage toEntity() {
        return FeedImage.builder()
//                .filePath(this.filePath)
                .delYn(this.delYn)
                .build();
    }
}
