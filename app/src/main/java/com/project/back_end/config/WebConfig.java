package com.project.back_end.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Remova o addCorsMappings se não for estritamente necessário para o lab, 
// para simplificar ainda mais. Se precisar, pode manter.
// Neste código, vou focar apenas no essencial.

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Esta é a versão corrigida e específica.
     * Ela diz ao Spring: "Apenas quando uma requisição começar com /assets/,
     * procure por ela como um arquivo estático dentro da pasta /static/assets/".
     * Isso é muito mais seguro e não interfere com o resto do Spring.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }
}