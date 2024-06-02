package org.app.infrastructure.endpoints.dto;

import org.app.domain.Account;

import java.util.List;

public class GetFriendListDTO {
    public String id;
    public String nickname;
    public String username;

    public GetFriendListDTO() {}

    public GetFriendListDTO(Account account) {
        this.id = account.getId().toString();
        this.nickname = account.getNickname();
        this.username = account.getUsername();
    }

    public static List<GetFriendListDTO> from(Account account){
        return account.getFriends().stream().map(GetFriendListDTO::new).toList();
    }
}
