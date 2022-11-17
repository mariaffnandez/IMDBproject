package co.empathy.IMDBproject.Controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;
import co.empathy.IMDBproject.Model.Response;
import co.empathy.IMDBproject.Service.ElasticServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class SearchController  {
    @Autowired

    private ElasticServiceImpl elasticService;
    private ElasticsearchClient client;

    public SearchController(ElasticServiceImpl elasticService) {

        this.elasticService = elasticService;
    }


    @GetMapping("/search")
    public ResponseEntity<List<Movie>> getMovies(   @RequestParam(required = false) String [] genres,
                                                    @RequestParam(required = false) Integer maxYear,
                                                    @RequestParam(required = false) Integer minYear,
                                                    @RequestParam(required = false) Integer maxMinutes,
                                                    @RequestParam(required = false) Integer minMinutes,
                                                    @RequestParam(required = false) Double maxScore,
                                                    @RequestParam(required = false) Double minScore,
                                                    @RequestParam(required = false) String[] type) throws IOException {

        Filters filter=Filters.builder()
                .type(type)
                .maxMinutes(maxMinutes)
                .minMinutes(minMinutes)
                .maxYear(maxYear)
                .minYear(minYear)
                .genre(genres)
                .minScore(minScore)
                .maxScore(maxScore)
                .build();

        return new ResponseEntity<>(elasticService.getQuery(filter),HttpStatus.ACCEPTED);
    }

    @GetMapping("/search/aggregations")
    public ResponseEntity<Response> getGenresAggregations() throws IOException {
        Response response= new Response();
        response.setHits(elasticService.getSearchQuery("the"));
        response.setFacets(elasticService.getAggregationsBucket("genres"));
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

    @GetMapping("/search/text")
    public ResponseEntity<List<Movie>> getSearchMovies( @RequestParam(required = true) String searchText) throws IOException {
        System.out.println(searchText);
        return new ResponseEntity<>(elasticService.getSearchQuery(searchText),HttpStatus.ACCEPTED);
    }









}
