package query

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import groovy.util.logging.Slf4j

@Slf4j
class MongoRepository implements Repository {
    private final DBCollection collection
    private final DB db

    public MongoRepository(MongoClientURI mongoClientURI, String collectionName) {
        def mongo = new MongoClient(mongoClientURI)
        db = mongo.getDB(mongoClientURI.database)
        collection = db.getCollection(collectionName)
        log.info("Created MongoRepository pointing to $mongoClientURI.database and will query collection $collectionName ")
    }

    public List<Map> findAll(Map<String, Object> filterSpec = [:]) {
        log.info("findAll filterSpec = $filterSpec")
        def criteria = ['$query': filterSpec] as BasicDBObject
        collection.find(criteria).toArray().collect { dataPoint ->
            dataPoint.toMap()
        }
    }
}
