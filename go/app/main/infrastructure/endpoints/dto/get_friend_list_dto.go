package dto

import (
    "mutualAttentionSystem/app/main/domain/events"
    "mutualAttentionSystem/app/main/infrastructure/utils"
)

type GetFriendListDTO struct {
    Friends   []*GetUserItemDTO
    Page      int
    Limit     int
    TotalPage int
}

func ToGetFriendListDTO(event *events.GetUserEvent, page int, limit int) (*GetFriendListDTO, error) {
    pageable, err := utils.NewPableable(page, limit, len(event.User.GetFollowings()))
    if err != nil {
        return nil, err
    }
    getFriendListDTO := GetFriendListDTO{make([]*GetUserItemDTO, 0), page, limit, pageable.TotalPage}
    for _, user := range event.User.GetFriends() {
        getFriendListDTO.Friends = append(getFriendListDTO.Friends, ToGetUserItem(user))
    }
    return &getFriendListDTO, nil
}
