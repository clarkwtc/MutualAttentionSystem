package dto

import (
    "mutualAttentionSystem/app/main/domain/events"
    "mutualAttentionSystem/app/main/infrastructure/utils"
)

type GetFollowingListDTO struct {
    Followings []*GetUserItemDTO
    Page       int
    Limit      int
    TotalPage  int
}

func ToGetFollowingListDTO(event *events.GetUserEvent, page int, limit int) *GetFollowingListDTO {
    pageable := utils.NewPableable(page, limit, len(event.User.GetFollowings()))
    
    getFollowingListDTO := GetFollowingListDTO{make([]*GetUserItemDTO, 0), page, limit, pageable.TotalPage}
    for _, user := range event.User.GetFollowings() {
        getFollowingListDTO.Followings = append(getFollowingListDTO.Followings, ToGetUserItem(user))
    }

    return &getFollowingListDTO
}
