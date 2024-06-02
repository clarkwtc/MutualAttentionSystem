package org.app.infrastructure.endpoints.dto;

import org.app.domain.Account;

import java.util.List;

public class GetFollowerListDTO {
    public String id;
    public String nickname;
    public String username;

    public GetFollowerListDTO(Account account) {
        this.id = account.getId().toString();
        this.nickname = account.getNickname();
        this.username = account.getUsername();
    }

    public static List<GetFollowerListDTO> from(Account account){
        return account.getFollower().stream().map(GetFollowerListDTO::new).toList();
    }
}
