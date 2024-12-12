package com.spot.fun.usr.feed.entity.hashtag;

import com.spot.fun.usr.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "tbl_feed_hashtag")
@NoArgsConstructor
@Getter
@Entity
public class FeedHashtag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idx", unique = true, updatable = false)
  private Long idx;

  @ManyToOne
  @JoinColumn(name = "feed_idx")
  private Feed feed;

  @ManyToOne
  @JoinColumn(name = "hashtag_idx")
  private Hashtag hashtag;

  @Column(name = "del_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
  private boolean delYn;


  @Builder
  public FeedHashtag(Feed feed, Hashtag hashtag) {
    this.feed = feed;
    this.hashtag = hashtag;
  }
}
