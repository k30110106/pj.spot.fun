package com.spot.fun.usr.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_board")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idx;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @CreationTimestamp
  @Column(name = "reg_date", nullable = false, updatable = false)
  private LocalDateTime regDate;

  @UpdateTimestamp
  @Column(name = "mod_date")
  private LocalDateTime modDate;

  @Column(name = "del_yn", nullable = false)
  private boolean delYn = false;

  @Column(name = "view_count", nullable = false)
  private int viewCount = 0;

  @Column(name = "like_count", nullable = false)
  private int likeCount = 0;

  @Column(name = "user_idx", nullable = false)
  private Long userIdx;

  @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BoardCommentEntity> comments;
}
