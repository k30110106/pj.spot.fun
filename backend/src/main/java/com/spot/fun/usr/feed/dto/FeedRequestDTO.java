package com.spot.fun.usr.feed.dto;

import com.spot.fun.paging.ScrollPagingUtil;
import com.spot.fun.usr.user.dto.UserDTO;
import lombok.*;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequestDTO extends ScrollPagingUtil {
    private String searchValue;
    private UserDTO loginUserDTO;
}
