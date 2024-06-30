package dto

import "mutualAttentionSystem/app/main/domain"

type GetFollowingListDTO struct {
    Followings []*GetUserItemDTO
}

func ToGetFollowingListDTO(followings []*domain.User) *GetFollowingListDTO {
    var getFollowingListDTO GetFollowingListDTO
    for _, user := range followings {
        getFollowingListDTO.Followings = append(getFollowingListDTO.Followings, ToGetUserItem(user))
    }
    return &getFollowingListDTO
}
