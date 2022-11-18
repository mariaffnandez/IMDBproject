package co.empathy.IMDBproject.Service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.empathy.IMDBproject.Model.Facets.Facets;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;


import co.empathy.IMDBproject.Model.Response;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.xcontent.XContentType;


import java.io.IOException;
import java.util.List;


public class ElasticEngineImpl implements ElasticEngine {
    public ElasticsearchClient client;

    public ElasticEngineImpl(ElasticsearchClient elasticClient) {
        this.client = elasticClient;
    }

    @Override
    public String listIndices() throws IOException {
        try {
            return client.cat().indices().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //Receives an index name and a json with settings, mappings, aliases...
    @Override
    public Boolean createIndex(String name, String mapping) {

        try {


            CreateIndexRequest request = new CreateIndexRequest(name);
            if (mapping != null) {
                System.out.println("map");
                request.mapping(mapping,XContentType.JSON);

            }
            CreateIndexResponse createIndexResponse = client.indices().create(c -> c.index(name));

            System.out.println("Created");
            return createIndexResponse.acknowledged();


        } catch (Exception e) {
            //can not create de index
            return false;


        }
    }
    @Override
    public Boolean deleteIndex(String indexName) {
        try {

            DeleteIndexResponse deleteIndexResponse = client.indices().delete(c -> c.index(indexName));
            if (deleteIndexResponse.acknowledged()){
                System.out.println("Deleted");
                return true;
            }
            else{
                return false;
            }

        } catch (IOException e) {
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
        }
    }

    @Override

    public Boolean indexMultipleDocs(String indexName, List<Movie> movies) throws IOException {
        boolean response=false;
        if (!movies.isEmpty()) {
            try {
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

                } else response=true;
            } catch (IOException e) {

                throw new RuntimeException(e);
            } catch (ElasticsearchException e) {

                throw new RuntimeException(e);
            }
        }
        return response;


    }
    @Override
    public Response getQuery(Filters filter) throws IOException {

        return new QueriesService(client).filterQuery(filter);

    }
    @Override
    public Response getSearchQuery(String searchText) throws IOException {

        return new QueriesService(client).searchQuery(searchText);

    }




}



