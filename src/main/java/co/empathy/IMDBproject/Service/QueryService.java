package co.empathy.IMDBproject.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.empathy.IMDBproject.Model.Movie;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryService {

    //Testing how to compose queries

    private ElasticsearchClient client;
    public QueryService(ElasticsearchClient elasticClient) {
        this.client = elasticClient;
    }

    //Multimatch title and synopsis query
    public List<Movie> multiMatchquery(String indexName, String query) throws IOException {
        JSONObject jsonObject= new JSONObject(query);
        String searchText= jsonObject.get("query").toString();

        SearchResponse<Movie> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .multiMatch(t -> t
                                        .fields("title","synopsis")
                                        .query(searchText)
                                )
                        ),
                Movie.class

        );

        TotalHits total = response.hits().total();
        List <Movie> filmHits= new ArrayList<Movie>();
        if (total.value()!=0) {
            List<Hit<Movie>> hits = response.hits().hits();

            for (Hit<Movie> hit: hits) {
                Movie movie = hit.source();
                filmHits.add(movie);
                System.out.println("Found movie" + movie.getOriginalTitle() + ", score " + hit.score());
            }
        } else {
            System.out.println("There are not results");
        }

        return filmHits;

    }

    //term query
    public List<Movie> termQuery(String indexName, String query) throws IOException {
        JSONObject jsonObject= new JSONObject(query);
        String searchText= jsonObject.get("query").toString();

        SearchResponse<Movie> response = client.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .term(t -> t
                                        .field("_id")
                                        .value(searchText)
                                )
                        ),
                Movie.class

        );

        TotalHits total = response.hits().total();
        List <Movie> filmHits= new ArrayList<Movie>();
        if (total.value()!=0) {
            List<Hit<Movie>> hits = response.hits().hits();

            for (Hit<Movie> hit: hits) {
                Movie movie = hit.source();
                filmHits.add(movie);
                System.out.println("Found movie" + movie.getPrimaryTitle() + ", score " + hit.score());
            }
        } else {
            System.out.println("There are not results");
        }

        return filmHits;

    }

}
