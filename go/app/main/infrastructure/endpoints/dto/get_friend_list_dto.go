package dto

import "mutualAttentionSystem/app/main/domain"

type GetFriendListDTO struct {
    Friends []*GetUserItemDTO
}

func ToGetFriendListDTO(friends []*domain.User) *GetFriendListDTO {
    var getFriendListDTO GetFriendListDTO
    for _, user := range friends {
        getFriendListDTO.Friends = append(getFriendListDTO.Friends, ToGetUserItem(user))
    }
    return &getFriendListDTO
}
