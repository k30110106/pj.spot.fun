package com.spot.fun.usr.feed.entity.like;

import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "tbl_feed_like")
@NoArgsConstructor
@Getter
@Entity
public class FeedLike {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idx", unique = true, updatable = false)
  private Long idx;

  @ManyToOne
  @JoinColumn(name = "feed_idx")
  private Feed feed;

  @ManyToOne
  @JoinColumn(name = "user_idx")
  private User user;

  @Builder
  public FeedLike(Feed feed, User user) {
    this.feed = feed;
    this.user = user;
  }
}
