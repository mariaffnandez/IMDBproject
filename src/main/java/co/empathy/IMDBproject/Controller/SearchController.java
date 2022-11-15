package co.empathy.IMDBproject.Controller;

import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie;
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


    public SearchController(ElasticServiceImpl elasticService) {

        this.elasticService = elasticService;
    }


    @GetMapping("/search")
    public ResponseEntity<List<Movie>> getMovies(   @RequestParam(required = false) String genre,
                                                    @RequestParam(required = false) Integer maxYear,
                                                    @RequestParam(required = false) Integer minYear,
                                                    @RequestParam(required = false) Integer maxMinutes,
                                                    @RequestParam(required = false) Integer minMinutes,
                                                    @RequestParam(required = false) Double maxScore,
                                                    @RequestParam(required = false) Double minScore,
                                                    @RequestParam(required = false) String type) throws IOException {

        Filters filter=Filters.builder()
                .type(type)
                .maxMinutes(maxMinutes)
                .minMinutes(minMinutes)
                .maxYear(maxYear)
                .minYear(minYear)
                .genre(genre)
                .minScore(minScore)
                .maxScore(maxScore)
                .build();

        return new ResponseEntity<>(elasticService.getQuery(filter),HttpStatus.ACCEPTED);
    }








}
