package com.jsondriventemplate.repo;

import com.jsondriventemplate.constant.AppConst;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class MongoClientProvider {

    private static final Logger LOGGER= LoggerFactory.getLogger(MongoClientProvider.class);

    private final MongoOperations mongoOperations;
    private static final String _ID = AppConst.ID;
    private static final String URL = "url";


    public MongoClientProvider(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public String save(Map<String,Object> dataMap, String collectionName) {
        LOGGER.debug("Saving data to db");
        collection(collectionName);
        Document document = new Document();

        if(!dataMap.containsKey(_ID) && StringUtils.isBlank((CharSequence) dataMap.get(_ID))){
            document.put(_ID, UUID());
        }
        document.putAll(dataMap);
        mongoOperations.save(document, collectionName);
        LOGGER.debug("Saved data to db with id {}",document.get(_ID));
        return (String) document.get(_ID);
    }

    public void delete(String id, String collectionName) {
        LOGGER.debug("Deleting data from db");
        Query query = Query.query(Criteria.where(_ID).is(id));
        mongoOperations.remove(query, collectionName);
        LOGGER.debug("Deleted data from db");
    }

    public List<?> findAll(String collectionName) {
        LOGGER.debug("Retrieving all {} data from db",collectionName);
        return mongoOperations.findAll(Map.class, collectionName);
    }

    public Map findById(String id, String collectionName) {
        LOGGER.debug("Retrieve by id {} from {}",id,collectionName);
        Query query = Query.query(Criteria.where(_ID).is(id));
        Map record = mongoOperations.findOne(query, Map.class, collectionName);
        if(record==null){
            return Collections.EMPTY_MAP;
        }
        return record;
    }

    public Map findByURL(String url, String collectionName) {
        LOGGER.debug("Retrieve by url {} from {}",url,collectionName);
        Query query = Query.query(Criteria.where(URL).is(url));
        return mongoOperations.findOne(query, Map.class,collectionName);
    }
    public Map findByAtt(String attr,String value, String collectionName) {
        LOGGER.debug("Retrieve by attribute {} = {} from {}",attr,value,collectionName);
        Query query = Query.query(Criteria.where(attr).is(value));
        return mongoOperations.findOne(query, Map.class,collectionName);
    }

    public List<?> search(Map<String,Object> searchMap, String collectionName) {
        LOGGER.debug("Searching data on {}",collectionName);
        Query query = new Query();
        for(Map.Entry<String, Object> entity:searchMap.entrySet()){
            String value = (String) entity.getValue();
            if(StringUtils.isNotBlank(value)){
                query.addCriteria(Criteria.where(entity.getKey()).regex(".*"+ value +".*","i"));
            }
        }
        return mongoOperations.find(query,Map.class,collectionName);
    }


    public void collection(String collectionName) {
        LOGGER.debug("Checking collection existance {}",collectionName);
        boolean collectionExists = mongoOperations.collectionExists(collectionName);
        if (collectionExists) {
            LOGGER.debug("Collection {} already exist ",collectionName);
            return;
        }
        LOGGER.debug("Creating collection {} ",collectionName);
        mongoOperations.createCollection(collectionName);
    }
    public void dropCollection(String collectionName) {
        LOGGER.debug("Drop collection {} ",collectionName);
        boolean collectionExists = mongoOperations.collectionExists(collectionName);
        if (!collectionExists) {
            LOGGER.debug("collection {} does not exist",collectionName);
            return;
        }
        LOGGER.debug("Dropping collection {}",collectionName);
        mongoOperations.dropCollection(collectionName);
    }

    private String UUID() {
        return UUID.randomUUID().toString();
    }

}
