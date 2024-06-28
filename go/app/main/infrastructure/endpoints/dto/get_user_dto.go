package dto

import "mutualAttentionSystem/app/main/domain"

type GetUserDTO struct {
    Id         string
    Username   string
    Followings int
    Friends    int
    Fans       int
}

func ToGetUserDTO(user *domain.User) *GetUserDTO {
    return &GetUserDTO{user.ID.String(), user.Username, len(user.GetFollowings()),
        len(user.GetFriends()), len(user.GetFans())}
}
