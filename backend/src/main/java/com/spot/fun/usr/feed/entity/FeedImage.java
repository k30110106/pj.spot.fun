package com.spot.fun.usr.feed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tbl_feed_image")
@NoArgsConstructor
@Getter
@Entity
public class FeedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", unique = true, updatable = false)
    private Long idx;

//    @Column(name = "file_path", nullable = false)
//    private String filePath;

    @Column(name= "upload_name", nullable = false)
    private String uploadName;

    @Column(name= "origin_name", nullable = false)
    private String originName;

    @Column(name = "del_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean delYn;

    @Setter
    @ManyToOne
    @JoinColumn(name = "feed_idx")
    private Feed feed;

    @Builder
    public FeedImage(boolean delYn, String uploadName, String originName) {
//        this.filePath = filePath;
        this.delYn = delYn;
        this.uploadName = uploadName;
        this.originName = originName;
    }

}