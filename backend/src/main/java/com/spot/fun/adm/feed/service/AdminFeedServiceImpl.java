package com.spot.fun.adm.feed.service;

import com.spot.fun.adm.feed.dto.AdminFeedRequestDTO;
import com.spot.fun.adm.feed.dto.AdminFeedResponseDTO;
import com.spot.fun.usr.feed.dto.FeedDTO;
import com.spot.fun.usr.feed.dto.comment.FeedCommentDTO;
import com.spot.fun.usr.feed.dto.hashtag.FeedHashtagDTO;
import com.spot.fun.usr.feed.dto.image.FeedImageDTO;
import com.spot.fun.usr.feed.entity.Feed;
import com.spot.fun.usr.feed.repository.UserFeedRepository;
import com.spot.fun.usr.feed.repository.comment.UserFeedCommentRepository;
import com.spot.fun.usr.feed.repository.hashtag.UserFeedHashtagRepository;
import com.spot.fun.usr.feed.repository.like.UserFeedLikeRepository;
import com.spot.fun.usr.feed.util.UserFeedUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminFeedServiceImpl implements AdminFeedService {

  private final UserFeedRepository userFeedRepository;
  private final UserFeedLikeRepository userFeedLikeRepository;
  private final UserFeedCommentRepository userFeedCommentRepository;
  private final UserFeedHashtagRepository userFeedHashtagRepository;
  private final UserFeedUtil userFeedUtil;

  @Override
  public AdminFeedResponseDTO getList(AdminFeedRequestDTO adminFeedRequestDTO) {
    Pageable pageable = PageRequest.of(adminFeedRequestDTO.getPage(), adminFeedRequestDTO.getSize(), Sort.by("idx").descending());
    List<FeedDTO> list = userFeedRepository.findFeedListByAdminCustom(adminFeedRequestDTO, pageable).stream()
            .map(feed -> new FeedDTO(feed, userFeedUtil)).toList();
    return AdminFeedResponseDTO.builder().list(list).build();
  }

  @Override
  public AdminFeedResponseDTO getDetail(AdminFeedRequestDTO adminFeedRequestDTO) {
    Long feedIdx = adminFeedRequestDTO.getIdx();
    Feed feed = userFeedRepository.findByIdx(feedIdx)
            .orElseThrow(IllegalArgumentException::new);

    FeedDTO detail = FeedDTO.builder()
            .idx(feed.getIdx())
            .content(feed.getContent())
            .regDateStr(userFeedUtil.getDateFormat(feed.getRegDate()))
            .userId(feed.getUser().getUserId())
            .feedImages(
                    feed.getFeedImages().stream()
                            .filter((item) -> !item.isDelYn())
                            .map((item) ->
                                    FeedImageDTO.builder()
                                            .idx(item.getIdx())
                                            //.filePath(item.getFilePath())
                                            .uploadName(item.getUploadName())
                                            .originName(item.getOriginName())
                                            .build()
                            ).toList()
            )
            .likeCount(userFeedLikeRepository.countByFeedIdx(feedIdx))
            .commentCount(userFeedCommentRepository.countByFeedIdxAndParentIdxIsNull(feedIdx))
            .feedHashtags(
                    userFeedHashtagRepository.findByFeedIdx(feedIdx).stream()
                            .map((tag) ->
                                    FeedHashtagDTO.builder()
                                            .idx(tag.getIdx())
                                            .hashtagIdx(tag.getHashtag().getIdx())
                                            .tagName(tag.getHashtag().getTagName())
                                            .build()
                            ).toList()
            )
            .feedComments(
                    userFeedCommentRepository.findByFeedIdxAndParentIdxIsNull(feedIdx).stream()
                            .map(comment -> FeedCommentDTO.builder()
                                    .idx((comment.getIdx()))
                                    .content(comment.isDelYn() ? "삭제된 댓글입니다" : comment.getContent())
                                    .delYn(comment.isDelYn())
                                    .regDateStr(userFeedUtil.getDateFormat(comment.getRegDate()))
                                    .likedYn(false)
                                    .userId(comment.getUser().getUserId())
                                    .replyList(
                                            userFeedCommentRepository.findByParentIdx(comment.getIdx()).stream()
                                                    .map((reply) -> FeedCommentDTO.builder()
                                                            .idx(reply.getIdx())
                                                            .content(reply.isDelYn() ? "삭제된 답글입니다" : reply.getContent())
                                                            .delYn(reply.isDelYn())
                                                            .regDateStr(userFeedUtil.getDateFormat(reply.getRegDate()))
                                                            .userId(reply.getUser().getUserId())
                                                            .build()
                                                    ).toList()
                                    )
                                    .build()
                            )
                            .toList()
            )
            .build();
    return AdminFeedResponseDTO.builder().detail(detail).build();
  }
}
