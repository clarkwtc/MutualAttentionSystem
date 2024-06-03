package org.app.infrastructure.endpoints.dto;

import org.app.domain.User;

import java.util.List;

public class GetFanListDTO {
    public String id;
    public String username;

    public GetFanListDTO(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
    }

    public static List<GetFanListDTO> from(User user){
        return user.getFans().stream().map(GetFanListDTO::new).toList();
    }
}
