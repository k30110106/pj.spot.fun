package com.spot.fun.usr.feed.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "tbl_feed_image")
@NoArgsConstructor
@Getter
@Entity
public class FeedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", unique = true, updatable = false)
    private Long idx;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "del_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean delYn;

    @ManyToOne
    @JoinColumn(name = "feed_idx")
    private Feed feed;

    @Builder
    public FeedImage(String filePath, boolean delYn) {
        this.filePath = filePath;
        this.delYn = delYn;
    }
}
