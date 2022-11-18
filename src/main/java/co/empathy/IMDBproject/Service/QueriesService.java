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
import co.empathy.IMDBproject.Model.Response;

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

    /**
     *
     * @param query, this is the query part of the search request
     * @return a Response with the List<Movie> and the facets
     * @throws IOException
     */
    public Response responseToQuery(Query query) throws IOException {
        Response customResponse= new Response();
        //genres just to test
        String field="genres";
        SearchResponse response = client.search(s -> s
                        .index(indexName)
                        .query(query)
                        .aggregations(field, a -> a
                                .terms(h -> h
                                        .field(field)
                                )

                        )

                        ,
                Movie.class
        );
        customResponse.setHits(hits(response));
        customResponse.setFacets(aggregationsResponse(response,field));

        return customResponse;

    }

    /**
     *
     * @param response, receives a SearchResponse
     * @return a List<Movie> with the hits
     *
     */
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

    /**
     *
     * @param searchText, the first param is a string with the movie´s title
     * @return a Response with the movies which primaryTitle or originalTitle matches the query
     * @throws IOException
     */
    public Response searchQuery(String searchText) throws IOException {
        List<String> fields=Arrays.asList("primaryTitle","originalTitle");
        return responseToQuery(queryProvider.multiMatchquery(searchText,fields));
    }

    /**
     *
     * @param filter, this param is a filter object
     * @return a response to the query with these filters
     * @throws IOException
     */
    public Response filterQuery(Filters filter) throws IOException {

        Query query= queryProvider.getFilterQuery(filter);
        System.out.println(query.toString());
        return responseToQuery(query);

    }

    /**
     * Runs an aggregation
     * @param field, a string that can be titleType or genres
     * @return facets, with the aggregations result
     * @throws IOException
     */

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

        return aggregationsResponse(response,field);

    }

    /**
     *
     * @param response, the first param is a SearchResponse
     * @param field, the second param is the aggregation´s name
     * @return facets, with the results for the aggregation
     */

    public Facets aggregationsResponse(SearchResponse response, String field){
        Aggregate object= (Aggregate) response.aggregations().get(field);
        List<StringTermsBucket> buckets= object.sterms().buckets().array();
        ArrayList<Values> valuesList= new ArrayList<>();
        for (StringTermsBucket bucket : buckets) {

            valuesList.add(new Values(bucket.key().stringValue(),bucket.docCount()));

        }
        return new Facets("value","facetGender",valuesList);
    }




}
