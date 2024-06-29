package dto

import "mutualAttentionSystem/app/main/domain"

type GetUserItemDTO struct {
    Id       string `json:"id"`
    Username string `json:"username"`
}

func ToGetUserItem(user *domain.User) *GetUserItemDTO {
    return &GetUserItemDTO{user.ID.String(), user.Username}
}
