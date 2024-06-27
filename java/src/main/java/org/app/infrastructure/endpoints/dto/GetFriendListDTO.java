package org.app.infrastructure.endpoints.dto;

import org.app.domain.events.FriendEvent;

import java.util.List;

public class GetFriendListDTO {
    public List<GetUserItemDTO> friends;
    public int page;
    public int limit;
    public int totalPage;

    public GetFriendListDTO(List<GetUserItemDTO> friends, int page, int limit, int totalPage) {
        this.friends = friends;
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
    }

    public GetFriendListDTO(FriendEvent event) {
        this.friends = event.getFriends().stream().map(GetUserItemDTO::new).toList();
        this.page = event.getPage();
        this.limit = event.getLimit();
        this.totalPage = event.getTotalPage();
    }

    public static GetFriendListDTO from(FriendEvent event){
        return new GetFriendListDTO(event);
    }
}
