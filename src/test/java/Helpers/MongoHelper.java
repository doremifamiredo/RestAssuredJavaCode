package Helpers;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoHelper {
    private static MongoClient client;
    private static MongoDatabase database;

    public MongoHelper(String connection) {
        client = MongoClients.create(connection);
        database = client.getDatabase("estim");
    }

    public MongoCollection<Document> getCollectionByName(String name) {
        return database.getCollection(name);
    }

    public Document getDocument(String collectionName, String findBy, String value) {
        MongoCollection<Document> collection = getCollectionByName(collectionName);
        Bson query = new Document(findBy, value);
        client.close();
        return collection.find(query).first();
    }
}
