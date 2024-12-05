package com.spot.fun.usr.feed.service.comment;

import com.spot.fun.usr.feed.dto.comment.FeedCommentDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.comment.FeedComment;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
import com.spot.fun.usr.feed.repository.comment.UserFeedCommentRepository;
import com.spot.fun.usr.feed.util.UserFeedUtil;
import com.spot.fun.usr.user.dto.UserDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFeedCommentServiceImpl implements UserFeedCommentService {

  private final UserFeedCommentRepository userFeedCommentRepository;
  private final UserFeedRepository userFeedRepository;
  private final UserRepository userRepository;

  private final UserFeedUtil userFeedUtil;

  @Override
  public List<FeedCommentDTO> getCommentList(Long idx, Long userIdx) {
    return userFeedCommentRepository.findFeedCommentWithReplyList(idx).stream()
            .map((comment) -> {
              boolean likedYn = !Objects.isNull(userIdx) && userFeedUtil.isFeedCommentLikedYn(comment.getIdx(), userIdx);
              return FeedCommentDTO.builder()
                      .idx((comment.getIdx()))
                      .content(comment.getContent())
                      .regDateStr(userFeedUtil.getDateFormat(comment.getRegDate()))
                      .likedYn(likedYn)
                      .user(UserDTO.builder()
                              .idx(comment.getUser().getIdx())
                              .userId(comment.getUser().getUserId())
                              .nickname(comment.getUser().getNickname())
                              .build()
                      )
                      .replyList(
                              comment.getReplyList().stream()
                                      .map((reply) -> {
                                        return FeedCommentDTO.builder()
                                                .idx(reply.getIdx())
                                                .content(reply.getContent())
                                                .regDateStr(userFeedUtil.getDateFormat(reply.getRegDate()))
                                                .user(UserDTO.builder()
                                                        .idx(reply.getUser().getIdx())
                                                        .userId(reply.getUser().getUserId())
                                                        .nickname(reply.getUser().getNickname())
                                                        .build()
                                                )
                                                .build();
                                      }).toList()
                      )
                      .build();
            }).toList();
  }

  @Transactional
  @Override
  public FeedCommentDTO insert(FeedCommentDTO feedCommentDTO) {
    try {
      Long userIdx = feedCommentDTO.getUser().getIdx();
      Feed feed = userFeedRepository.findByIdxAndDelYnFalse(feedCommentDTO.getIdx())
              .orElseThrow(IllegalArgumentException::new);
      User user = userRepository.findByIdx(userIdx)
              .orElseThrow(IllegalArgumentException::new);

      FeedComment comment = userFeedCommentRepository.save(
              FeedComment.builder()
                      .content(feedCommentDTO.getContent())
                      .feed(feed)
                      .user(user)
                      .build());

      return FeedCommentDTO.builder()
              .idx(comment.getIdx())
              .content(comment.getContent())
              .regDateStr(userFeedUtil.getDateFormat(comment.getRegDate()))
              .likedYn(false)
              .user(UserDTO.builder()
                      .idx(comment.getUser().getIdx())
                      .userId(comment.getUser().getUserId())
                      //.name(comment.getUser().getName())
                      .nickname(comment.getUser().getNickname())
                      .build())
              .build();
    } catch (Exception e) {
      log.info("comment insert error .. {}", e.getMessage());
      return new FeedCommentDTO();
    }
  }

  @Override
  public FeedCommentDTO insertReply(FeedCommentDTO feedCommentDTO) {
    try {
      Feed feed = userFeedRepository.findByIdxAndDelYnFalse(feedCommentDTO.getFeedIdx())
              .orElseThrow(IllegalArgumentException::new);
      User user = userRepository.findByIdx(feedCommentDTO.getUser().getIdx())
              .orElseThrow(IllegalArgumentException::new);
      FeedComment feedComment = userFeedCommentRepository.findByIdxAndDelYnFalse(feedCommentDTO.getParentIdx())
              .orElseThrow(IllegalArgumentException::new);

      FeedComment reply = userFeedCommentRepository.save(
              FeedComment.builder()
                      .content(feedCommentDTO.getContent())
                      .feed(feed)
                      .user(user)
                      .parent(feedComment)
                      .build());

      return FeedCommentDTO.builder()
              .idx(reply.getIdx())
              .content(reply.getContent())
              .regDateStr(userFeedUtil.getDateFormat(reply.getRegDate()))
              .likedYn(false)
              .user(UserDTO.builder()
                      .idx(reply.getUser().getIdx())
                      .userId(reply.getUser().getUserId())
                      //.name(comment.getUser().getName())
                      .nickname(reply.getUser().getNickname())
                      .build())
              .parentIdx(feedComment.getIdx())
              .build();
    } catch (Exception e) {
      log.info("comment insertReply error .. {}", e.getMessage());
      return new FeedCommentDTO();
    }
  }
}
