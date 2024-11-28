package com.spot.fun.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrollPagingUtil {
    private Long lastId = 0L; // 기본값을 0L로 설정
    private Integer pageSize = 6; // 기본값 6
    private Boolean hasNext = false; // 기본값 false
}
