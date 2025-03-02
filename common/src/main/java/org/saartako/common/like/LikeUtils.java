package org.saartako.common.like;

import org.saartako.common.user.UserUtils;

import java.util.List;

public class LikeUtils {

    private LikeUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static List<LikeDTO> copyDisplay(List<? extends Like> likes) {
        return likes.stream().map(LikeUtils::copyDisplay).toList();
    }

    public static LikeDTO copyDisplay(Like like) {
        return new LikeDTO()
            .setId(like.getId())
            .setUser(UserUtils.copyDisplay(like.getUser()));
    }

    public static String toString(Like like) {
        return "LikeDTO{" +
               "id=" + like.getId() +
               ", song=" + like.getSong() +
               ", user=" + like.getUser() +
               '}';
    }
}
