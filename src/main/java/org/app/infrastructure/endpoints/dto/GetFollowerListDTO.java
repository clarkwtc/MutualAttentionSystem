package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

import java.util.List;

public class GetFollowerListDTO {
    public String id;
    public String nickname;
    public String username;

    public GetFollowerListDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
    }

    public static List<GetFollowerListDTO> from(User user){
        return user.getFans().stream().map(GetFollowerListDTO::new).toList();
    }
}
