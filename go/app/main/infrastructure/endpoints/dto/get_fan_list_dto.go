package dto

import (
	"mutualAttentionSystem/app/main/domain/events"
	"mutualAttentionSystem/app/main/infrastructure/utils"
)

type GetFanListDTO struct {
    Fans []*GetUserItemDTO
    Page       int
    Limit      int
    TotalPage  int
}

func ToGetFanListDTO(event *events.GetUserEvent, page int, limit int) *GetFanListDTO {
    pageable := utils.NewPableable(page, limit, len(event.User.GetFollowings()))
    
    getFanListDTO := GetFanListDTO{make([]*GetUserItemDTO, 0), page, limit, pageable.TotalPage}
    for _, user := range event.User.GetFans() {
        getFanListDTO.Fans = append(getFanListDTO.Fans, ToGetUserItem(user))
    }
    return &getFanListDTO
}
