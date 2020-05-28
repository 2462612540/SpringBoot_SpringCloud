package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userId, String friendId) {
        //先判断userId到friendId是否有数据，有就是重复添加好友，返回0
        Friend friend = friendDao.findByUseridAndFriendid(userId, friendId);
        if (friend != null){
            return 0;
        }
        //直接添加好友，让好友表中userId到friendId方向的type为0
        friend = new Friend();
        friend.setUserid(userId);
        friend.setFriendid(friendId);
        friend.setIslike("0");
        friendDao.save(friend);
        //判断从friendid到userId是否有数据，如果有，把双方的状态都改为1
        if (friendDao.findByUseridAndFriendid(friendId,userId)!=null){
            //把双方的islike改为1
            friendDao.updateIslike("1",userId,friendId);
            friendDao.updateIslike("1",friendId,userId);
        }
        return 1;
    }

    public int addNoFriend(String userId, String friendId) {
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userId, friendId);
        if (noFriend != null){
            return 0;
        }
        noFriend = new NoFriend();
        noFriend.setUserid(userId);
        noFriend.setFriendid(friendId);
        noFriendDao.save(noFriend);
        return 1;
    }

    public void deleteFriend(String userId, String friendId) {
        //删除表中还有表中userid到friendId这条数据
        friendDao.deletefriend(userId, friendId);
        //更新friendId到userId的islike为0
        friendDao.updateIslike("0", friendId, userId);
        //非好友表中添加数据
        NoFriend noFriend = new NoFriend();
        noFriend = new NoFriend();
        noFriend.setUserid(userId);
        noFriend.setFriendid(friendId);
        noFriendDao.save(noFriend);
    }
}
