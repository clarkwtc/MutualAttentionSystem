package dto

import "mutualAttentionSystem/app/main/domain"

type GetFollowingListDTO struct {
    Following []*GetUserItemDTO
}

func ToGetFollowingListDTO(followings []*domain.User) *GetFollowingListDTO {
    var getFollowingListDTO GetFollowingListDTO
    for _, user := range followings {
        getFollowingListDTO.Following = append(getFollowingListDTO.Following, ToGetUserItem(user))
    }
    return &getFollowingListDTO
}
