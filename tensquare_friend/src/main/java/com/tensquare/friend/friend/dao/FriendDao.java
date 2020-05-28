package com.tensquare.friend.friend.dao;

import com.tensquare.friend.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String> {
    public Friend findByUseridAndFriendid(String userId, String friendId);

    @Modifying
    @Query(value = "UPDATE tb_friend SET islike=? WHERE userid = ? AND  friendid = ?", nativeQuery = true)
    public void updateIslike(String islike, String userId, String friendId);


    @Modifying
    @Query(value = "delete from tb_friend WHERE userid = ? AND  friendid = ?", nativeQuery = true)
    void deletefriend(String userId, String friendId);
}
