package com.tensquare.friend.friend.dao;

import com.tensquare.friend.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoFriendDao extends JpaRepository<NoFriend, String> {
    public NoFriend findByUseridAndFriendid(String userId, String friendId);
}
