package com.spot.fun.usr.feed.dto;

import com.spot.fun.paging.ScrollPagingUtil;
import com.spot.fun.usr.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequestDTO extends ScrollPagingUtil {
  private String searchValue;
  private UserDTO loginUserDTO;

  private Long userIdx;
}
