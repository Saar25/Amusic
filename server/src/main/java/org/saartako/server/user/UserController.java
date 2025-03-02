package org.saartako.server.user;

import org.saartako.common.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("likes")
    public List<Long> findLikedSongs() {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.userService.findLikedSongIds(user);
    }
}
