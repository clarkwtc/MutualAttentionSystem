package mongodb

import (
    "context"
    "github.com/google/uuid"
    "go.mongodb.org/mongo-driver/bson"
    "go.mongodb.org/mongo-driver/mongo"
    "mutualAttentionSystem/app/main/domain"
)

type UserRepository struct {
    collection *mongo.Collection
}

func NewUserRepository(client *mongo.Client) *UserRepository {
    return &UserRepository{client.Database("mutual_attention").Collection("User")}
}

func (repository UserRepository) Find(id uuid.UUID) *domain.User {
    var userDocument *UserDocument
    err := repository.collection.FindOne(context.Background(), bson.M{"_id": id.String()}).Decode(&userDocument)
    if err != nil {
        return nil
    }

    return toUserDomain(userDocument)
}

func toUsersDomain(usersDocument []*UserDocument) []*domain.User {
    var users []*domain.User
    for _, userDocument := range usersDocument {
        users = append(users, toUserDomain(userDocument))
    }
    return users
}

func (repository UserRepository) FindByIds(ids []uuid.UUID) []*domain.User {
    var idsDoc []string

    for _, id := range ids {
        idsDoc = append(idsDoc, id.String())
    }
    filter := bson.M{"_id": bson.M{"$in": idsDoc}}
    ctx := context.Background()
    cursor, err := repository.collection.Find(ctx, filter)
    if err != nil {
        return nil
    }

    var usersDocument []*UserDocument
    err = cursor.All(ctx, &usersDocument)
    if err != nil {
        return nil
    }

    return toUsersDomain(usersDocument)
}

func (repository UserRepository) FindByUsername(username string) *domain.User {
    var userDocument *UserDocument
    err := repository.collection.FindOne(context.Background(), bson.M{"username": username}).Decode(&userDocument)
    if err != nil {
        return nil
    }

    return toUserDomain(userDocument)
}

func (repository UserRepository) Save(user *domain.User) {
    _, err := repository.collection.InsertOne(context.Background(), toUserDocument(user))
    if err != nil {
        return
    }
}
