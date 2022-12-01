package co.empathy.IMDBproject.Config;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.IMDBproject.Service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public ElasticServiceImpl searchService(ElasticEngine searchEngine) {
        return new ElasticServiceImpl(searchEngine);
    }
    @Bean
    public ElasticEngine elasticEngine(ElasticsearchClient elasticClient) {
        return new ElasticEngineImpl(elasticClient);
    }
    @Bean
    public QueriesService queryService(ElasticsearchClient elasticClient){
        return new QueriesService(elasticClient);
    }
}
