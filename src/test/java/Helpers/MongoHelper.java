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

    public Document getDocument(String collectionName, String findBy, int value) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Bson query = new Document(findBy, value);
        return collection.find(query).first();
    }
}
