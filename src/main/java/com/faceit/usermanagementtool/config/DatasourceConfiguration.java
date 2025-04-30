package com.faceit.usermanagementtool.config;

import com.faceit.usermanagementtool.util.Util;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class DatasourceConfiguration {

    @Value("${datasource.databaseType}")
    private String databaseType;

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.port}")
    private String port;

    @Value("${datasource.databaseInstance}")
    private String databaseInstance;


    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(this.databaseType + this.url + Util.GrammarSign.COLON + this.port + Util.GrammarSign.SLASH + this.databaseInstance);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongo(), this.databaseInstance);
    }
    
}
