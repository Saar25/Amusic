package org.saartako.common.playlist;

import com.google.gson.annotations.SerializedName;

public record CreatePlaylistDTO(
    String name,
    @SerializedName("private") boolean isPrivate
) {
}
