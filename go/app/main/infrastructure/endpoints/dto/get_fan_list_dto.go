package dto

import "mutualAttentionSystem/app/main/domain"

type GetFanListDTO struct {
    Fans []*GetUserItemDTO
}

func ToGetFanListDTO(followings []*domain.User) *GetFanListDTO {
    var getFanListDTO GetFanListDTO
    for _, user := range followings {
        getFanListDTO.Fans = append(getFanListDTO.Fans, ToGetUserItem(user))
    }
    return &getFanListDTO
}
