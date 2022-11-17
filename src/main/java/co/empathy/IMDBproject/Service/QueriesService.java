package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.empathy.IMDBproject.Model.Facets.Facets;
import co.empathy.IMDBproject.Model.Facets.Values;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;

import java.io.IOException;
import java.util.*;

public class QueriesService {
    private ElasticsearchClient client;
    private QueryProvider queryProvider;
    String indexName= "imdb";

    public QueriesService(ElasticsearchClient elasticClient) {

        this.client = elasticClient;
        this.queryProvider= new QueryProvider();
    }

    //Multimatch query
    public List<Movie> responseToQuery(Query query) throws IOException {

        SearchResponse response = client.search(s -> s
                        .index(indexName)
                        .query(query)


                        ,
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
    public List<Movie> searchQuery(String searchText) throws IOException {
        List<String> fields=Arrays.asList("primaryTitle","originalTitle");
        return responseToQuery(queryProvider.multiMatchquery(searchText,fields));
    }
    public List<Movie> filterQuery(Filters filter) throws IOException {


        Query query= queryProvider.getFilterQuery(filter);
        System.out.println(query.toString());
        return responseToQuery(query);

    }
    //receive a field that can be titleType or genres and returns a hashmap
    public Facets aggregationTerms(String field) throws IOException {

        SearchResponse<Void> response = client.search(b -> b
                        .index(indexName)
                        .size(0)
                        .aggregations(field, a -> a
                                .terms(h -> h
                                        .field(field)
                                )
                        ),
                Void.class
        );
        System.out.println(response.toString());
        List<StringTermsBucket> buckets = response.aggregations()
                .get(field)
                .sterms().buckets().array();

        Map<String, Long> map = new HashMap<>();
        ArrayList<Values> valuesList= new ArrayList<>();
        for (StringTermsBucket bucket : buckets) {
            valuesList.add(new Values(bucket.key().stringValue(),bucket.docCount()));

        }

        return new Facets("value","facetGender",valuesList);

    }



}
