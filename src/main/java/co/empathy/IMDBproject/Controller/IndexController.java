package co.empathy.IMDBproject.Controller;


import co.empathy.IMDBproject.Model.Movie.Movie;

import co.empathy.IMDBproject.Service.ElasticServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@Tag(name="Index")

public class IndexController {
    @Autowired

    private ElasticServiceImpl elasticService;


    public IndexController(ElasticServiceImpl elasticService) {

        this.elasticService = elasticService;
    }



    //Creates a new index


    //index a single doc
    @Operation(summary = "Index a single movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully indexed"),
            @ApiResponse(responseCode = "400", description = "Bad Request") })

    @PostMapping("/{indexName}/_doc")
    public ResponseEntity indexDoc(@PathVariable String indexName, @RequestBody Movie movie) throws IOException {
        boolean created = elasticService.indexDoc(indexName, movie);
        if (created)
            return new ResponseEntity<>(HttpStatus.CREATED);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }
    @Operation(summary = "Delete an indices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Bad Request") })

    @DeleteMapping("/{indexName}")public ResponseEntity deleteIndex(@PathVariable String indexName) throws IOException {

        if(elasticService.deleteIndex(indexName))
            return new ResponseEntity(HttpStatus.OK);


       else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    @Operation(summary = "Creates a new 'IMDb' index")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully indexed"),
            @ApiResponse(responseCode = "400", description = "Bad Request") })

    @PostMapping(value="/index", consumes = {"multipart/form-data"})
    public ResponseEntity<String> indexDoc(@RequestParam("basics") MultipartFile basicsFile,
                                           @RequestParam("ratings") MultipartFile ratingFile,
                                           @RequestParam("akas") MultipartFile akasFile,
                                           @RequestParam("crew") MultipartFile crewFile,
                                           @RequestParam("principals") MultipartFile principalsFile

                                           ) {

        try {
             elasticService.indexIMDBData(basicsFile, ratingFile, akasFile, crewFile, principalsFile);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (IOException ex){
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
}