package com.spot.fun.usr.feed.entity.comment;

import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

  //@UpdateTimestamp
  @Column(name = "mod_date")
  private LocalDateTime modDate;

  @ManyToOne
  @JoinColumn(name = "user_idx")
  private User user;

  //  @ManyToOne(fetch = FetchType.LAZY)
  @ManyToOne
  @JoinColumn(name = "feed_idx")
  private Feed feed;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_idx")
  private FeedComment parent;

  @OneToMany(mappedBy = "parent")
  private List<FeedComment> replyList = new ArrayList<>(); // 대댓글 목록;

  @Builder
  public FeedComment(String content, Feed feed, User user, FeedComment parent) {
    this.content = content;
    this.feed = feed;
    this.user = user;
    this.parent = parent;
  }

  @PrePersist
  public void prePersist() {
    if (modDate == null) {
      modDate = null;
    }
  }

  @PreUpdate
  public void preUpdate() {
    if (modDate == null) {
      modDate = LocalDateTime.now();
    }
  }

  public void changeContent(String content) {
    this.content = content;
  }

  public void changeDelYn(boolean delYn) {
    this.delYn = delYn;
  }

  public void changeParent(FeedComment parent) {
    this.parent = parent;
  }
}
