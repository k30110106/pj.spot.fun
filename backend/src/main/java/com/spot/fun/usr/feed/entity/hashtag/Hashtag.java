package com.spot.fun.usr.feed.entity.hashtag;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "tbl_hashtag")
@NoArgsConstructor
@Getter
@Entity
public class Hashtag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idx", unique = true, updatable = false)
  private Long idx;

  @Column(name = "tag_name", nullable = false)
  private String tagName;

  @Column(name = "del_yn", columnDefinition = "TINYINT(1) DEFAULT 0")
  private boolean delYn;
}
