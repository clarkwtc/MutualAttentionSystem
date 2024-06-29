package events

import "mutualAttentionSystem/app/main/domain"


type GetUserEvent struct {
	User *domain.User
}
