package com.gruppe2.DB;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * @Author Borgar Flaen Stensrud, Erik-Tobias Huseby Ellefsen
 * @usage Klasse for å koble til MongoDB
 */
public class MongoUtil {

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public static MongoDatabase getDatabase(String dbName) {


            if ( mongoClient == null ) {
                String connectionString = "add_connetion_string_here";
                ServerApi serverApi = ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build();
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(connectionString))
                        .serverApi(serverApi)
                        .build();
                mongoClient = MongoClients.create(settings);
            }

            if ( database == null ) {
                try {
                    database = mongoClient.getDatabase(dbName);
                } catch (MongoException e) {
                    System.out.println("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            return database;
    }
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

}

