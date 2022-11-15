package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.CategorizeTextAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.InternalOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                        .query(query)
                        .size(20),
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

        return responseToQuery(query);

    }
    public void aggregationTypeQuery() throws IOException {
        Query q= queryProvider.rangeQuery(2010,2020,"startYear");
        String aggregationsName="agg-type";
        SearchResponse<Void> response = client.search(b -> b
                        .index(indexName)
                        .size(0)
                        .query(q)
                        .aggregations(aggregationsName, a -> a
                                .histogram(h -> h
                                        .field("titleType")
                                )
                        ),
                Void.class
        );
        List<HistogramBucket> buckets= response.aggregations()
                .get(aggregationsName)
                .histogram().buckets().array();
        for (HistogramBucket bucket: buckets) {
           System.out.println("There are " + bucket.docCount() +
                    "  under " + bucket.key());
        }


    }



}
