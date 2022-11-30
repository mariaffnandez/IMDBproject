package co.empathy.IMDBproject.Service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.empathy.IMDBproject.Model.Query.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;


import co.empathy.IMDBproject.Model.Response;
import co.empathy.IMDBproject.Model.Query.Sort;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ElasticEngineImpl implements ElasticEngine {
    public ElasticsearchClient client;

    public ElasticEngineImpl(ElasticsearchClient elasticClient) {
        this.client = elasticClient;
    }



    @Override
    public Boolean createIndex(String name, InputStream mapping) {

        try {

            if (mapping == null || name==null) {
                return false;
            }

            CreateIndexResponse createIndexResponse = client.indices().create(c -> c.index(name).withJson(mapping));
            return createIndexResponse.acknowledged();


        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            else
                return false;
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

    public Boolean indexMultipleDocs(String indexName, List<Movie> movies) {
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
            }
        }
        return response;


    }



    @Override
    public Response getQuery(Filters filter, Sort sort) throws IOException {

        return new QueriesService(client).filterQuery(filter, sort);

    }


    @Override
    public Response getSearchQuery(String searchText) throws IOException {

        return new QueriesService(client).searchQuery(searchText);

    }




}



