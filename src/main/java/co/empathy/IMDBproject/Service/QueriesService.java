package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueriesService {
    private ElasticsearchClient client;
    private QueryProvider queryProvider;
    String indexName= "test";

    public QueriesService(ElasticsearchClient elasticClient) {

        this.client = elasticClient;
        this.queryProvider= new QueryProvider();
    }

    //Multimatch query
    public List<Movie> responseToQuery(Query query) throws IOException {

        SearchResponse response = client.search(s -> s
                        .index(indexName)
                        .query(query),
                Movie.class
        );
        return hits(response);

    }
    public List<Movie> hits (SearchResponse<Movie> response ){

        TotalHits total = response.hits().total();
        System.out.println("Total hits "+total.value());
        List <Movie> filmHits= new ArrayList<>();
        if (total.value()!=0) {
            List<Hit<Movie>> hits = response.hits().hits();

            for (Hit<Movie> hit: hits) {
                Movie movie = hit.source();
                filmHits.add(movie);

            }
        } else {
            System.out.println("There are not results");
        }

        return filmHits;

    }
    public List<Movie> filterQuery(Filters filter) throws IOException {
        //create a filter

        //filter.setMinScore(0);

        Query query= queryProvider.getFilterQuery(filter);

        return responseToQuery(query);










    }



}
