package dto

import "mutualAttentionSystem/app/main/domain"

type GetUserDTO struct {
    Id         string `json:"id"`
    Username   string `json:"username"`
    Followings int    `json:"followings"`
    Friends    int    `json:"friends"`
    Fans       int    `json:"fans"`
}

func ToGetUserDTO(user *domain.User) *GetUserDTO {
    return &GetUserDTO{user.ID.String(), user.Username, len(user.GetFollowings()),
        len(user.GetFriends()), len(user.GetFans())}
}
