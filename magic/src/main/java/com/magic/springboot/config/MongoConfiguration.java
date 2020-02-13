package com.magic.springboot.config;

import com.magic.springboot.converter.BigDecimalToDoubleConverter;
import com.magic.springboot.converter.DoubleToBigDecimalConverter;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ifuck on 2018/8/21.
 */
public class MongoConfiguration {
    private String host;
    private int port;

    private String database;

    private String username;

    private String password;

    public MongoTemplate createMongoTemplate() throws Exception {
        Mongo mongo = mongo();
        MongoTemplate mongoTemplate = new MongoTemplate(mongo, database);
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        mongoMapping.setCustomConversions(customConversions()); // tell mongodb to use the custom converters
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;
    }

    /**
     * Configure the MongoDB client
     **/
    private Mongo mongo() throws Exception {
        List<ServerAddress> addresses = new ArrayList<>();
        String[] hosts = host.split(",");
        for (String host : hosts) {
            host = host.trim();
            if (host.length() == 0) {
                continue;
            }
            ServerAddress address = new ServerAddress(host, port);
            addresses.add(address);
        }
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                .maxConnectionIdleTime(60000).build();
        if (StringUtils.isEmpty(username) == false) {

            MongoCredential credentials = MongoCredential.createCredential(username,
                    database, password.toCharArray());
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the list of custom converters that will be used by the MongoDB template
     *
     **/
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(new DoubleToBigDecimalConverter(), new BigDecimalToDoubleConverter()));
    }
}
