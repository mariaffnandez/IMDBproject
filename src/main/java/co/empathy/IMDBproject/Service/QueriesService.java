package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.empathy.IMDBproject.Model.Facets.Facets;
import co.empathy.IMDBproject.Model.Facets.Values;
import co.empathy.IMDBproject.Model.Query.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;
import co.empathy.IMDBproject.Model.Response;
import co.empathy.IMDBproject.Model.Query.Sort;



import java.io.IOException;

import java.util.*;

import static java.util.Objects.nonNull;

public class QueriesService {
    private ElasticsearchClient client;
    private QueryProvider queryProvider;
    String indexName= "imdb";
    String genres= "genres";
    String titleType="titleType";
    Integer maxNHits= 100; //number of hits returned by default


    public QueriesService(ElasticsearchClient elasticClient) {

        this.client = elasticClient;
        this.queryProvider= new QueryProvider();
    }


    /**
     *
     * @param query this is the query part of the search request
     * @param sort this is a sort object with the sorting options
     * @return a Response with the List<Movie> and the facets
     * @throws IOException
     */

    public Response responseToQuery(Query query, Sort sort) throws IOException {
        Response customResponse= new Response();

       Map<String,Aggregation> map=aggregationTerms();

       List<SortOptions> list= sortOptions(sort);
       System.out.println(list.size());
       SearchResponse searchResponse = client.search(s -> s
                        .index(indexName)
                        .query(query)
                        .size(maxNHits)
                        .sort(list)
                        .aggregations(genres, map.get(genres))
                        .aggregations(titleType,map.get(titleType))

                        ,
                Movie.class
        );
        customResponse.setHits(hits(searchResponse));
        addFacetsToResponse(searchResponse,customResponse);


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
     * @param searchText, the first param is a string with the movie??s title
     * @return a Response with the movies which primaryTitle or originalTitle matches the query
     * @throws IOException
     */
    public Response searchQuery(String searchText) throws IOException {
        List<String> fields=Arrays.asList("primaryTitle","originalTitle");
        return responseToQuery(queryProvider.multiMatchquery(searchText,fields), null);
    }

    /**
     *
     * @param filter, this param is a filter object
     * @return a response to the query with these filters
     * @throws IOException
     */
    public Response filterQuery(Filters filter,Sort sort) throws IOException {

        Query query= queryProvider.getFilterQuery(filter);
        System.out.println(query.toString());
        if (nonNull(filter.getMaxNHits()))
            maxNHits= filter.getMaxNHits();
        return responseToQuery(query,sort);

    }

    /**
     * Runs an aggregation (genres and type)
     * @return a map with the aggregations as value
     *
     */

    public Map<String,Aggregation> aggregationTerms() {

       Map<String,Aggregation> map= new HashMap<>();
       map.put(genres,Aggregation.of(a -> a.terms(h -> h.field(genres))));
       map.put(titleType,Aggregation.of(a -> a.terms(h -> h.field(titleType))));

        return map;

    }

    /**
     *
     * @param response, the first param is a SearchResponse
     * @param field, the second param is the aggregation??s name
     * @return facets, with the results for the aggregation
     */

    public Facets aggregationsResponse(SearchResponse response, String field){
        Aggregate object= (Aggregate) response.aggregations().get(field);
        List<StringTermsBucket> buckets= object.sterms().buckets().array();
        ArrayList<Values> valuesList= new ArrayList<>();
        for (StringTermsBucket bucket : buckets) {

            valuesList.add(new Values(bucket.key().stringValue(),bucket.docCount()));

        }
        if (valuesList.isEmpty())
            return null;
        return new Facets("value","facet"+field,valuesList);
    }

    /**
     *
     * @param sorts, a sort object
     * @return a list with the sortOptions that can be: sort by startYear, averageRating or numVotes (by default)
     */
    public List<SortOptions> sortOptions(Sort sorts){
        List<SortOptions> list= new ArrayList<>();
        String asc="asc";

        if (nonNull(sorts)) {
            if (nonNull(sorts.getSortYear())) {
                SortOrder order = SortOrder.Desc;
                if (sorts.getSortYear().equalsIgnoreCase(asc)) {
                    order = SortOrder.Asc;
                }

                SortOrder finalOrder = order;
                list.add(new SortOptions.Builder().field(f -> f.field("startYear").order(finalOrder)).build());
            }
            if (nonNull(sorts.getSortRating())) {
                SortOrder order = SortOrder.Desc;
                if (sorts.getSortRating().equalsIgnoreCase(asc)) {
                    order = SortOrder.Asc;
                }

                SortOrder finalOrder = order;
                list.add(new SortOptions.Builder().field(f -> f.field("averageRating").order(finalOrder)).build());
            }
            if (nonNull(sorts.getSortNumVotes())) {

                if (sorts.getSortNumVotes().equalsIgnoreCase(asc)) {
                    list.add(new SortOptions.Builder().field(f -> f.field("numVotes").order(SortOrder.Asc)).build());
                }
            }
        }
        //by default, it orders by numVotes
        list.add(new SortOptions.Builder().field(f -> f.field("numVotes").order(SortOrder.Desc)).build());

        return list;

    }

    /**
     * Adds the aggregation response (just genres and type) to the Response
     * @param searchResponse, the searchResponse to the query
     * @param customResponse, the Response object that the api will return
     */

    public void addFacetsToResponse(SearchResponse searchResponse, Response customResponse) {
        ArrayList<Facets> facets= new ArrayList<>();
       if (nonNull(aggregationsResponse(searchResponse, genres)))
           facets.add(aggregationsResponse(searchResponse, genres));

        if (nonNull(aggregationsResponse(searchResponse, titleType)))
            facets.add(aggregationsResponse(searchResponse, titleType));

        customResponse.setFacets(facets);
    }

}
