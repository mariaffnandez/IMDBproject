package co.empathy.IMDBproject.Controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Response;
import co.empathy.IMDBproject.Model.Sort;
import co.empathy.IMDBproject.Service.ElasticServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static java.util.Objects.nonNull;


@RestController
public class SearchController  {
    @Autowired

    private ElasticServiceImpl elasticService;
    private ElasticsearchClient client;

    public SearchController(ElasticServiceImpl elasticService) {

        this.elasticService = elasticService;
    }


    @GetMapping("/search")
    public ResponseEntity<Response> getMovies(   @RequestParam(required = false) String [] genres,
                                                    @RequestParam(required = false) Integer maxYear,
                                                    @RequestParam(required = false) Integer minYear,
                                                    @RequestParam(required = false) Integer maxMinutes,
                                                    @RequestParam(required = false) Integer minMinutes,
                                                    @RequestParam(required = false) Double maxScore,
                                                    @RequestParam(required = false) Double minScore,
                                                    @RequestParam(required = false) String[] type,
                                                    @RequestParam (required = false) int maxNHits,
                                                    @RequestParam(required = false) String sortRating,
                                                    @RequestParam(required = false) String sortYear,
                                                    @RequestParam(required = false) String sortNumVotes) throws IOException {

        int nHits=500; //number of hits returned by default
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

        Sort sort= Sort.builder()
                .sortRating(sortRating)
                .sortYear(sortYear)
                .sortNumVotes(sortNumVotes)
                .build();
        if (nonNull(maxNHits)){
            nHits= maxNHits;
        }

        return new ResponseEntity<>(elasticService.getQuery(filter,nHits,sort),HttpStatus.ACCEPTED);
    }



    @GetMapping("/search/text")
    public ResponseEntity<Response> getSearchMovies( @RequestParam String searchText) throws IOException {
        System.out.println(searchText);
        return new ResponseEntity<>(elasticService.getSearchQuery(searchText),HttpStatus.ACCEPTED);
    }









}
