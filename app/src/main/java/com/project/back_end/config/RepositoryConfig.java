package com.project.back_end.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.project.back_end.repo.jpa")
@EnableMongoRepositories(basePackages = "com.project.back_end.repo.mongo")
public class RepositoryConfig {
}
