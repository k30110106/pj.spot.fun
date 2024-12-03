package com.spot.fun.usr.chat.repository;

import com.spot.fun.usr.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByUserIdx(Long userIdx);

    @Query("SELECT cr.other.idx FROM ChatRoom cr WHERE cr.roomId = :roomId")
    Long findOtherIdByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT cr.roomId FROM ChatRoom cr WHERE cr.user.idx = :userIdx AND cr.other.idx = :otherIdx")
    Long findRoomIdByUserIdxAndOtherIdx(@Param("userIdx") Long userIdx, @Param("otherIdx") Long otherIdx);
}
