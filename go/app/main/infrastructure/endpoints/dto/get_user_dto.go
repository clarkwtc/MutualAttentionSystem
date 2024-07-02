package dto

import (
	"mutualAttentionSystem/app/main/domain/events"
)

type GetUserDTO struct {
    Id         string `json:"id"`
    Username   string `json:"username"`
    Followings int    `json:"followings"`
    Friends    int    `json:"friends"`
    Fans       int    `json:"fans"`
}

func ToGetUserDTO(event *events.GetUserEvent) *GetUserDTO {
    user := event.User
    return &GetUserDTO{user.ID.String(), user.Username, len(user.GetFollowings()),
        len(user.GetFriends()), len(user.GetFans())}
}
