package com.spot.fun.usr.feed.entity;

import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Table(name = "tbl_feed_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class FeedComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idx", unique = true, updatable = false)
  private Long idx;

  @Column(name = "content", nullable = false, length = 300)
  private String content;

  @Column(name = "del_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
  private boolean delYn;

  @CreationTimestamp
  @Column(name = "reg_date")
  private LocalDateTime regDate;

  @UpdateTimestamp
  @Column(name = "mod_date")
  private LocalDateTime modDate;

  @ManyToOne
  @JoinColumn(name = "user_idx")
  private User user;

  @ManyToOne
  @JoinColumn(name = "feed_idx")
  private Feed feed;

  @Builder
  public FeedComment(String content, boolean delYn, LocalDateTime regDate) {
    this.content = content;
    this.delYn = delYn;
    this.regDate = regDate;
  }
}
