package application

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
)

func SetRelationshipsOfUser(user *domain.User, userRepository domain.IUserRepository, relationshipRepository domain.IRelationshipRepository) {
    relationships := relationshipRepository.FindUserId(user.ID)
    userIds := getUsersIdByRelationships(relationships, user.ID)
    users := userRepository.FindByIds(userIds)

    setUserInRelationships(relationships, users)
    user.Relationships = relationships
}

func getUsersIdByRelationships(relationships []*domain.Relationship, userId uuid.UUID) []uuid.UUID {
    userIdsMap := make(map[uuid.UUID]bool)

    for _, relationship := range relationships {
        followingId := relationship.Following.ID
        fanId := relationship.Fan.ID

        if userId != followingId && !userIdsMap[followingId] {
            userIdsMap[followingId] = true
        }

        if userId != fanId && !userIdsMap[fanId] {
            userIdsMap[fanId] = true
        }
    }

    var userIds []uuid.UUID
    for key := range userIdsMap {
        userIds = append(userIds, key)
    }

    return userIds
}

func setUserInRelationships(relationships []*domain.Relationship, users []*domain.User) {
    for _, relationship := range relationships {
        for _, user := range users {
            if relationship.Fan.ID == user.ID {
                relationship.Fan = user
            }

            if relationship.Following.ID == user.ID {
                relationship.Following = user
            }
        }
    }
}
