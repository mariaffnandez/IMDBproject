package co.empathy.IMDBproject.Controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Response;
import co.empathy.IMDBproject.Model.Sort;
import co.empathy.IMDBproject.Service.ElasticServiceImpl;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

import static java.util.Objects.nonNull;


@RestController
@Tag (name="Search")
public class SearchController  {
    @Autowired

    private ElasticServiceImpl elasticService;


    public SearchController(ElasticServiceImpl elasticService) {

        this.elasticService = elasticService;
    }



    @Operation(summary = "Get movies using filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieves documents matching the query"
                    , content = {@Content(mediaType = "application/json")}),

            @ApiResponse(responseCode = "400", description = "Invalid query", content = @Content) })
    @GetMapping("/search")
    public ResponseEntity<Response> getMovies(   @RequestParam(required = false) String [] genres,
                                                    @RequestParam(required = false) Integer maxYear,
                                                    @RequestParam(required = false) Integer minYear,
                                                    @RequestParam(required = false) Integer maxMinutes,
                                                    @RequestParam(required = false) Integer minMinutes,
                                                    @RequestParam(required = false) Double maxScore,
                                                    @RequestParam(required = false) Double minScore,
                                                    @RequestParam(required = false) String[] type,
                                                    @RequestParam (required = false) Integer maxNHits,
                                                    @RequestParam(required = false) String sortRating,
                                                    @RequestParam(required = false) String sortYear,
                                                    @RequestParam(required = false) String sortNumVotes) throws IOException {


        Filters filter=Filters.builder()
                .type(type)
                .maxMinutes(maxMinutes)
                .minMinutes(minMinutes)
                .maxYear(maxYear)
                .minYear(minYear)
                .genre(genres)
                .minScore(minScore)
                .maxScore(maxScore)
                .maxNHits(maxNHits)
                .build();

        Sort sort= Sort.builder()
                .sortRating(sortRating)
                .sortYear(sortYear)
                .sortNumVotes(sortNumVotes)
                .build();

        Response response=elasticService.getQuery(filter,sort);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }



    @GetMapping("/search/text")
    public ResponseEntity<Response> getSearchMovies( @RequestParam String searchText) throws IOException {
        System.out.println(searchText);
        return new ResponseEntity<>(elasticService.getSearchQuery(searchText),HttpStatus.ACCEPTED);
    }









}
