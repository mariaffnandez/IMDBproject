package co.empathy.IMDBproject.Controller;


import co.empathy.IMDBproject.Model.Movie.Movie;

import co.empathy.IMDBproject.Service.ElasticServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
public class IndexController {
    @Autowired

    private ElasticServiceImpl elasticService;


    public IndexController(ElasticServiceImpl elasticService) {

        this.elasticService = elasticService;
    }

    @GetMapping("/_cat/indices")
    public ResponseEntity<String> showIndex() throws IOException {

        String indices = elasticService.listIndices();
        return new ResponseEntity<String>(indices, HttpStatus.OK);
    }

    //Creates a new index
    @PutMapping("/{indexName}")
    public ResponseEntity createIndex(@PathVariable String indexName, @RequestBody String source) {

        if (elasticService.createIndex(indexName, source)) {

            return new ResponseEntity(HttpStatus.CREATED);
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    //index a single doc
    @PostMapping("/{indexName}/_doc")
    public ResponseEntity indexDoc(@PathVariable String indexName, @RequestBody Movie movie) throws IOException {
        boolean created = elasticService.indexDoc(indexName, movie);
        if (created)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(movie);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }
    @DeleteMapping("/{indexName}")public ResponseEntity deleteIndex(@PathVariable String indexName) throws IOException {

        if( elasticService.deleteIndex(indexName))
            return new ResponseEntity(HttpStatus.ACCEPTED);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/index")
    public ResponseEntity<String> indexDoc(@RequestParam("basics") MultipartFile basicsFile,
                                           @RequestParam("ratings") MultipartFile ratingFile,
                                           @RequestParam("akas") MultipartFile akasFile,
                                           @RequestParam("crew") MultipartFile crewFile,
                                           @RequestParam("principals") MultipartFile principalsFile

                                           ) throws IOException {


        Boolean done=elasticService.indexIMDBData(basicsFile,ratingFile,akasFile,crewFile,principalsFile);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }
}