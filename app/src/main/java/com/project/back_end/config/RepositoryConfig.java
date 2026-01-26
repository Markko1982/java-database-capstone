package com.project.back_end.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.project.back_end.repo.jpa")
public class RepositoryConfig {
}
