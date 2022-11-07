package co.empathy.IMDBproject.Service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.empathy.IMDBproject.Model.Movie;

import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;


import java.io.IOException;
import java.util.List;

public class ElasticEngineImpl implements ElasticEngine {
    private ElasticsearchClient client;


    public ElasticEngineImpl(ElasticsearchClient elasticClient) {
        this.client = elasticClient;
    }

    @Override
    public String listIndices() throws IOException {
        try {
            return client.cat().indices().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ElasticsearchException e) {
            throw new RuntimeException(e);
        }

    }

    //Receives an index name and a json with settings, mappings, aliases...
    @Override
    public Boolean createIndex(String name, String source) {

        try {
            //DOES NOT WORK, check if the index already exists
            GetIndexRequest existReq = new GetIndexRequest(name);
            //boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
            CreateIndexRequest request = new CreateIndexRequest(name);
            if (source != null)
                request.source(source, XContentType.JSON);
            CreateIndexResponse createIndexResponse = client.indices().create(c -> c.index(name));

            return createIndexResponse.acknowledged();


        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    @Override
    public Boolean indexDoc(String indexName, Movie movie) {
        try {

            GetResponse<Movie> existsResp = client.get(g -> g
                            .index(indexName)
                            .id(movie.getTconst()),
                    Movie.class
            );
            //checks if the movie's id already exists
            if (existsResp.found()) {
                return false;
            } else {
                IndexResponse response = client.index(i -> i
                        .index(indexName)
                        .id(movie.getTconst())
                        .document(movie)
                );
                System.out.println("Indexed with version " + response.version());
                return true;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ElasticsearchException e) {
            throw new RuntimeException(e);
        }

    }

    @Override

    public Boolean indexMultipleDocs(String indexName, List<Movie> movies) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Movie movie : movies) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(movie.getTconst())
                            .document(movie)
                    )
            );
        }

        BulkResponse result = client.bulk(br.build());


        if (result.errors()) {
            System.out.println("Bulk error indexing multiple docs");
            return false;
        } else return true;


    }
}

