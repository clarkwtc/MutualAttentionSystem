package mongodb

import (
    "context"
    "github.com/google/uuid"
    "go.mongodb.org/mongo-driver/bson"
    "go.mongodb.org/mongo-driver/mongo"
    "mutualAttentionSystem/app/main/domain"
)

type RelationshipRepository struct {
    collection *mongo.Collection
}

func NewRelationshipRepository(client *mongo.Client) *RelationshipRepository {
    return &RelationshipRepository{client.Database("mutual_attention").Collection("Relationship")}
}

func toRelationshipsDomain(relationshipsDocument []*RelationDocument) []*domain.Relationship {
    var relationships []*domain.Relationship
    for _, relationshipDocument := range relationshipsDocument {
        relationships = append(relationships, toRelationshipDomain(relationshipDocument))
    }
    return relationships
}

func (repository *RelationshipRepository) FindUserId(userId uuid.UUID) []*domain.Relationship {
    ids := make([]bson.M, 2)
    ids[0] = bson.M{"followingId": userId.String()}
    ids[1] = bson.M{"fanId": userId.String()}
    filter := bson.M{"$or": ids}

    ctx := context.Background()
    cursor, err := repository.collection.Find(ctx, filter)
    if err != nil {
        return nil
    }

    var relationshipsDocument []*RelationDocument
    err = cursor.All(ctx, &relationshipsDocument)
    if err != nil {
        return nil
    }
    return toRelationshipsDomain(relationshipsDocument)
}

func (repository *RelationshipRepository) Find(followingId uuid.UUID, fanId uuid.UUID) []*domain.Relationship {
    ids := make([]bson.M, 2)
    ids[0] = bson.M{"followingId": followingId.String(), "fanId": fanId.String()}
    ids[1] = bson.M{"followingId": fanId.String(), "fanId": followingId.String()}
    filter := bson.M{"$or": ids}

    ctx := context.Background()
    cursor, err := repository.collection.Find(ctx, filter)
    if err != nil {
        return nil
    }

    var relationshipsDocument []*RelationDocument
    err = cursor.All(ctx, &relationshipsDocument)
    if err != nil {
        return nil
    }
    return toRelationshipsDomain(relationshipsDocument)
}

func (repository *RelationshipRepository) Update(relationships []*domain.Relationship) {
    var writeModels []mongo.WriteModel

    for _, relationship := range relationships {
        model := mongo.NewUpdateManyModel().
            SetFilter(bson.M{"_id": relationship.ID.String()}).
            SetUpdate(bson.M{"$set": toRelationshipDocument(relationship)}).
            SetUpsert(true)
        writeModels = append(writeModels, model)
    }

    _, err := repository.collection.BulkWrite(context.Background(), writeModels)
    if err != nil {
        return
    }
}

func (repository *RelationshipRepository) Remove(followingId uuid.UUID, fanId uuid.UUID) {
    filter := bson.M{"followingId": followingId.String(), "fanId": fanId.String()}
    _, err := repository.collection.DeleteOne(context.Background(), filter)
    if err != nil {
        return
    }
}

func toRelationshipsDocument(relationships []*domain.Relationship) []interface{} {
    documents := make([]interface{}, len(relationships))
    for _, relationship := range relationships {
        documents = append(documents, toRelationshipDocument(relationship))
    }
    return documents
}

func (repository *RelationshipRepository) Save(relationships []*domain.Relationship) {
    document := toRelationshipsDocument(relationships)
    _, err := repository.collection.InsertMany(context.Background(), document)
    if err != nil {
        return
    }
}
