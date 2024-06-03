package org.app.infrastructure.endpoints.dto;

import org.app.domain.events.FollowingEvent;

import java.util.List;

public class GetFollowingListDTO {
    public List<GetUserItemDTO> following;
    public int page;
    public int limit;
    public int totalPage;

    public GetFollowingListDTO(List<GetUserItemDTO> following, int page, int limit, int totalPage) {
        this.following = following;
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
    }

    public GetFollowingListDTO(FollowingEvent event) {
        this.following = event.getFollowings().stream().map(GetUserItemDTO::new).toList();
        this.page = event.getPage();
        this.limit = event.getLimit();
        this.totalPage = event.getTotalPage();
    }

    public static GetFollowingListDTO from(FollowingEvent event){
        return new GetFollowingListDTO(event);
    }
}
