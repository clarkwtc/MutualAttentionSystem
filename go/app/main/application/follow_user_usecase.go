package application

import (
    "github.com/google/uuid"
    "mutualAttentionSystem/app/main/domain"
    "mutualAttentionSystem/app/main/domain/errors"
)

type FollowUserUsecase struct {
    UserRepository         domain.IUserRepository
    RelationshipRepository domain.IRelationshipRepository
}

func (usecase *FollowUserUsecase) Execute(userId string, followId string) error {
    var usersId []uuid.UUID
    usersId = append(usersId, uuid.MustParse(userId))
    usersId = append(usersId, uuid.MustParse(followId))
    users := usecase.UserRepository.FindByIds(usersId)

    if len(users) != 2 {
        return &errors.NotExistUserError{}
    }

    system := domain.NewMutualAttentionSysyem()
    system.AddUserAll(users)

    user := system.GetUser(userId)
    following := system.GetUser(followId)

    relationships := usecase.RelationshipRepository.Find(uuid.MustParse(followId), uuid.MustParse(userId))
    user.Relationships = relationships
    following.Relationships = relationships
    user.Follow(following)

    usecase.RelationshipRepository.Update(user.Relationships)
    return nil
}
