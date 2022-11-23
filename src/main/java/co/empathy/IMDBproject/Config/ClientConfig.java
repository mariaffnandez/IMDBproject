package co.empathy.IMDBproject.Config;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.IMDBproject.Service.ElasticEngine;
import co.empathy.IMDBproject.Service.ElasticEngineImpl;

import co.empathy.IMDBproject.Service.ElasticServiceImpl;
import org.apache.http.HttpHost;

import org.elasticsearch.client.RestClient;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

// This is the configuration class where we'll define our beans (objects whose lifecycle is managed by Spring)

    @Bean
    public ElasticServiceImpl searchService(ElasticEngine searchEngine) {
        return new ElasticServiceImpl(searchEngine);
    }
    @Bean
    public ElasticEngine elasticEngine(ElasticsearchClient elasticClient) {
        return new ElasticEngineImpl(elasticClient);
    }

    @Bean
    public RestClient lowClient() {
        RestClient client = RestClient.builder(new HttpHost("localhost", 9200))
                .build();
        return client;
    }

    @Bean
    public ElasticsearchClient elasticClient() {
        RestClient client = RestClient.builder(new HttpHost("localhost", 9200),
                new HttpHost("elasticsearch", 9200)).build();
        ElasticsearchTransport transport= new RestClientTransport(client,new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }





}
