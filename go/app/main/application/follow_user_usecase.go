package application

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
)

type FollowUserUsecase struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
}

func (usecase *FollowUserUsecase) Execute(userId string, followId string) {
    var usersId []uuid.UUID
    usersId = append(usersId, uuid.MustParse(userId))
    usersId = append(usersId, uuid.MustParse(followId))
    users := usecase.UserRepository.FindByIds(usersId)

    system := domain.NewMutualAttentionSysyem()
    system.AddUserAll(users)

    user := system.GetUser(userId)
    following := system.GetUser(followId)

    relationships := usecase.RelationshipRepository.Find(uuid.MustParse(followId), uuid.MustParse(userId))
    user.Relationships = relationships
    following.Relationships = relationships
    user.Follow(following)

    usecase.RelationshipRepository.Update(user.Relationships)
}
