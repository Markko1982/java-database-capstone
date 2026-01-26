package com.project.back_end.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ConditionalOnProperty(name = "app.mongo.enabled", havingValue = "true", matchIfMissing = true)
@EnableMongoRepositories(basePackages = "com.project.back_end.repo.mongo")
public class MongoRepositoryConfig {
}
