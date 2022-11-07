package co.empathy.IMDBproject.Controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.empathy.IMDBproject.Model.Movie;

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
    private ElasticsearchClient client;
    private ElasticServiceImpl elasticService;


    public IndexController(ElasticServiceImpl elasticService){

        this.elasticService= elasticService;
    }

    @GetMapping("/_cat/indices")
    public ResponseEntity<String> showIndex() throws IOException, InterruptedException {

        String indices= elasticService.listIndices();
        return new ResponseEntity<String>(indices, HttpStatus.OK);
    }

    //Creates a new index
    @PutMapping("/create/{indexName}")
    public ResponseEntity createIndex(@PathVariable String indexName){

        if (elasticService.createIndex(indexName,null)){

            return new ResponseEntity(HttpStatus.CREATED);
        }
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    //index a single doc
    @PostMapping("/{indexName}/_doc")
    public ResponseEntity indexDoc(@PathVariable String indexName, @RequestBody Movie movie) throws IOException {
        boolean created= elasticService.indexDoc(indexName, movie);
        if (created)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(movie);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/upload")
    public ResponseEntity<String> indexDoc(@RequestParam("file") MultipartFile file) throws IOException {
        elasticService.indexData(file);
        return new ResponseEntity("what",HttpStatus.OK);
    }


}