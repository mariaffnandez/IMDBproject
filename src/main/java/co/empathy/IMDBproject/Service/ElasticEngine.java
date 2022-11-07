package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Movie;
import org.elasticsearch.client.ElasticsearchClient;

import java.io.IOException;

public interface ElasticEngine {
    String listIndices() throws IOException;
    Boolean createIndex(String name, String source);
    Boolean indexDoc(String indexName, Movie movie);


}
