package com.spot.fun.usr.feed.service.comment;

import com.spot.fun.usr.feed.dto.comment.FeedCommentDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.entity.comment.FeedComment;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
import com.spot.fun.usr.feed.repository.comment.UserFeedCommentRepository;
import com.spot.fun.usr.feed.util.UserFeedUtil;
import com.spot.fun.usr.user.dto.profile.UserProfileRequestDTO;
import com.spot.fun.usr.user.entity.User;
import com.spot.fun.usr.user.repository.UserRepository;
import com.spot.fun.usr.user.service.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserFeedCommentServiceImpl implements UserFeedCommentService {

  private final UserFeedCommentRepository userFeedCommentRepository;
  private final UserFeedRepository userFeedRepository;
  private final UserRepository userRepository;

  private final UserProfileService userProfileService;

  private final UserFeedUtil userFeedUtil;

  @Override
  public List<FeedCommentDTO> getCommentList(Long idx, Long userIdx) {
    return userFeedCommentRepository.findByFeedIdxAndParentIdxIsNull(idx).stream()
            .map((comment) -> FeedCommentDTO.builder()
                    .idx((comment.getIdx()))
                    .content(comment.isDelYn() ? "삭제된 댓글입니다" : comment.getContent())
                    .delYn(comment.isDelYn())
                    .regDateStr(userFeedUtil.getDateFormat(comment.getRegDate()))
                    .likedYn(false)
                    .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(comment.getUser().getIdx()).build()))
                    .replyList(
                            userFeedCommentRepository.findByParentIdx(comment.getIdx()).stream()
                                    .map((reply) -> FeedCommentDTO.builder()
                                            .idx(reply.getIdx())
                                            .content(reply.isDelYn() ? "삭제된 답글입니다" : reply.getContent())
                                            .delYn(reply.isDelYn())
                                            .regDateStr(userFeedUtil.getDateFormat(reply.getRegDate()))
                                            .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(reply.getUser().getIdx()).build()))
                                            .build()
                                    ).toList()
                    )
                    .build()).toList();
  }

  @Transactional
  @Override
  public FeedCommentDTO insert(FeedCommentDTO feedCommentDTO) {
    try {
      Feed feed = userFeedRepository.findByIdxAndDelYnFalse(feedCommentDTO.getFeedIdx())
              .orElseThrow(IllegalArgumentException::new);
      User user = userRepository.findByIdx(feedCommentDTO.getUserIdx())
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
              .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(comment.getUser().getIdx()).build()))
              .build();
    } catch (Exception e) {
      log.info("comment insert error .. {}", e.getMessage());
      return new FeedCommentDTO();
    }
  }

  @Transactional
  @Override
  public FeedCommentDTO insertReply(FeedCommentDTO feedCommentDTO) {
    try {
      Feed feed = userFeedRepository.findByIdxAndDelYnFalse(feedCommentDTO.getFeedIdx())
              .orElseThrow(IllegalArgumentException::new);
      User user = userRepository.findByIdx(feedCommentDTO.getUserIdx())
              .orElseThrow(IllegalArgumentException::new);
      FeedComment feedComment = userFeedCommentRepository.findByIdx(feedCommentDTO.getParentIdx())
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
              .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(reply.getUser().getIdx()).build()))
              .parentIdx(reply.getParent().getIdx())
              .build();
    } catch (Exception e) {
      log.info("comment insertReply error .. {}", e.getMessage());
      return new FeedCommentDTO();
    }
  }

  @Transactional
  @Override
  public FeedCommentDTO update(FeedCommentDTO feedCommentDTO) {
    try {
      FeedComment feedComment = userFeedCommentRepository.findByIdxAndDelYnFalse(feedCommentDTO.getIdx())
              .orElseThrow(IllegalArgumentException::new);
      feedComment.changeContent(feedCommentDTO.getContent());

      FeedComment update = userFeedCommentRepository.save(feedComment);

      return FeedCommentDTO.builder()
              .idx(update.getIdx())
              .content(update.getContent())
              .regDateStr(userFeedUtil.getDateFormat(update.getRegDate()))
              .likedYn(false)
              .user(userProfileService.getProfile(UserProfileRequestDTO.builder().userIdx(update.getUser().getIdx()).build()))
              //.parentIdx(ObjectUtils.isEmpty(feedCommentDTO.getParentIdx()) ? null : feedCommentDTO.getParentIdx())
              .build();
    } catch (Exception e) {
      log.info("comment update error .. {}", e.getMessage());
      return new FeedCommentDTO();
    }
  }

  @Transactional
  @Override
  public FeedCommentDTO delete(FeedCommentDTO feedCommentDTO) {
    try {
      FeedComment feedComment = userFeedCommentRepository.findByIdxAndDelYnFalse(feedCommentDTO.getIdx())
              .orElseThrow(IllegalArgumentException::new);
      feedComment.changeDelYn(true); // 삭제

      FeedComment delete = userFeedCommentRepository.save(feedComment);

      return FeedCommentDTO.builder()
              .idx(delete.getIdx())
              .build();
    } catch (Exception e) {
      log.info("comment delete error .. {}", e.getMessage());
      return new FeedCommentDTO();
    }
  }
}
