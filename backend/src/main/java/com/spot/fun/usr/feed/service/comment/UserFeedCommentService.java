package com.spot.fun.usr.feed.service.comment;

import com.spot.fun.usr.feed.dto.comment.FeedCommentDTO;

import java.util.List;

public interface UserFeedCommentService {
  List<FeedCommentDTO> getCommentList(Long idx, Long userIdx);

  FeedCommentDTO insert(FeedCommentDTO feedCommentDTO);

  FeedCommentDTO insertReply(FeedCommentDTO feedCommentDTO);

  FeedCommentDTO update(FeedCommentDTO feedCommentDTO);

  FeedCommentDTO delete(FeedCommentDTO feedCommentDTO);
}
