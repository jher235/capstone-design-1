package org.example.capstonedesign1.global.config;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeaviateConfig {

    @Bean
    public WeaviateClient weaviateClient(@Value("${weaviate.method}") String method,
                                         @Value("${weaviate.host}") String host) {
        Config config = new Config(method, host);
        return new WeaviateClient(config);
    }
}
