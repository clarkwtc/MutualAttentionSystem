package org.app.infrastructure.endpoints.dto;

import org.app.domain.events.FanEvent;

import java.util.List;

public class GetFanListDTO {
    public List<GetUserItemDTO> fans;
    public int page;
    public int limit;
    public int totalPage;

    public GetFanListDTO(List<GetUserItemDTO> fans, int page, int limit, int totalPage) {
        this.fans = fans;
        this.page = page;
        this.limit = limit;
        this.totalPage = totalPage;
    }

    public GetFanListDTO(FanEvent event) {
        this.fans = event.getFans().stream().map(GetUserItemDTO::new).toList();
        this.page = event.getPage();
        this.limit = event.getLimit();
        this.totalPage = event.getTotalPage();
    }

    public static GetFanListDTO from(FanEvent event){
        return new GetFanListDTO(event);
    }
}
