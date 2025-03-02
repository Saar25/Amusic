package org.saartako.server.user;

import org.saartako.common.user.User;
import org.saartako.server.like.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    public List<Long> findLikedSongIds(User user) {
        return this.likeRepository.findLikedSongIdsByUserId(user.getId());
    }
}