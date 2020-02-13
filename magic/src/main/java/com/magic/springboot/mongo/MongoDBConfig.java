package com.magic.springboot.mongo;

import com.magic.springboot.converter.BigDecimalToDoubleConverter;
import com.magic.springboot.converter.DoubleToBigDecimalConverter;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class MongoDBConfig  {

    @Value("${mongo.host}")
    public String mongoHost;

//    @Value("${mongoHosts}")
//    private String mongoHosts;

    @Value("${mongo.port}")
    public int mongoPort;
 
    @Value("${mongo.database}")
    public String mongoDatabase;

    @Value("${mongo.username}")
    public String userName;

    @Value("${mongo.password}")
    public String password;
 
    
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        Mongo mongo = mongo();
        MongoTemplate mongoTemplate = new MongoTemplate(mongo, mongoDatabase);
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setCustomConversions(customConversions()); // tell mongodb to use the custom converters
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;
 
    }
 
    /**
    * Configure the MongoDB client
    * 
    **/
    public Mongo mongo() throws Exception {
        List<ServerAddress> addresses = new ArrayList<>();
        String[] hosts = mongoHost.split(",");
        for (String host : hosts) {
            host = host.trim();
            if (host.length() == 0) {
                continue;
            }
            ServerAddress address = new ServerAddress(host, mongoPort);
            addresses.add(address);
        }
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                .maxConnectionIdleTime(60000).build();

        if (StringUtils.isEmpty(userName) == false) {

            MongoCredential credentials = MongoCredential.createCredential(userName,
                    mongoDatabase, password.toCharArray());
            MongoClient client;
            if (addresses.size() > 1) {
                client = new MongoClient(addresses, Arrays.asList(credentials), mongoClientOptions);
            } else {
                client = new MongoClient(addresses.get(0), Arrays.asList(credentials), mongoClientOptions);
            }

            return client;
        }
        MongoClient client;
        if (addresses.size() > 1) {
            client = new MongoClient(addresses, mongoClientOptions);
        } else {
            client = new MongoClient(addresses.get(0), mongoClientOptions);
        }
        return client;
    }
 
 
   /**
    * Returns the list of custom converters that will be used by the MongoDB template
    * 
    **/
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(new DoubleToBigDecimalConverter(), new BigDecimalToDoubleConverter()));
    }
}