package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.empathy.IMDBproject.Model.Movie;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryService {

    //Testing how to compose queries

    private ElasticsearchClient client;
    public QueryService(ElasticsearchClient elasticClient) {
        this.client = elasticClient;
    }

    //Multimatch query
    public List<Movie> multiMatchquery(String indexName, String query, List <String> fields) throws IOException {

        SearchResponse<Movie> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .multiMatch(t -> t
                                        .fields(fields)
                                        .query(query)
                                )
                        ),
                Movie.class
        );
        return hits(response);

    }


    //term query
    public List<Movie> termQuery(String indexName, String query, String field) throws IOException {
        SearchResponse<Movie> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .term(t -> t
                                        .field(field)
                                        .value(query)
                                )
                        ),
                Movie.class

        );

        return hits(response);

    }
    public List<Movie> hits (SearchResponse<Movie> response ){
        TotalHits total = response.hits().total();
        List <Movie> filmHits= new ArrayList<>();
        if (total.value()!=0) {
            List<Hit<Movie>> hits = response.hits().hits();

            for (Hit<Movie> hit: hits) {
                Movie movie = hit.source();
                filmHits.add(movie);
                System.out.println("Score "+hit.score());
            }
        } else {
            System.out.println("There are not results");
        }

        return filmHits;

    }

}
