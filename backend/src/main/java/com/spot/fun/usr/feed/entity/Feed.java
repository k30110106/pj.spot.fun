package com.spot.fun.usr.feed.entity;

import com.spot.fun.usr.feed.entity.hashtag.FeedHashtag;
import com.spot.fun.usr.feed.entity.image.FeedImage;
import com.spot.fun.usr.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tbl_feed")
@NoArgsConstructor
@Getter
@Entity
public class Feed {
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

  //  @ManyToOne(fetch = FetchType.LAZY)
  @ManyToOne
  @JoinColumn(name = "user_idx")
  private User user;

//  @OneToMany(mappedBy = "feed")
//  private List<FeedComment> feedComments;

  @OneToMany(mappedBy = "feed", fetch = FetchType.EAGER)
//  @OneToMany(mappedBy = "feed")
  private List<FeedImage> feedImages = new ArrayList<>();

  @OneToMany(mappedBy = "feed", fetch = FetchType.EAGER)
  private List<FeedHashtag> feedHashtags;

  @Builder
  public Feed(String content, boolean delYn, User user) {
    this.content = content;
    this.delYn = delYn;
    this.user = user;
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

  public void changeDelYn(boolean delYn) {
    this.delYn = delYn;
  }

  public void changeContent(String content) {
    this.content = content;
  }

  public void changeUser(User user) {
    this.user = user;
  }
}
