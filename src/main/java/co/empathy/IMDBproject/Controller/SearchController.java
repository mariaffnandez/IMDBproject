package co.empathy.IMDBproject.Controller;

import co.empathy.IMDBproject.Service.ElasticServiceImpl;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController  {
    @Autowired

    private ElasticServiceImpl elasticService;


    public SearchController(ElasticServiceImpl elasticService) {

        this.elasticService = elasticService;
    }


    @GetMapping("/search")
    public ResponseEntity<String> getMovies(   @RequestParam(required = false) String genre,
                                                    @RequestParam(required = false) Integer maxYear,
                                                    @RequestParam(required = false) Integer minYear,
                                                    @RequestParam(required = false) Integer maxMinutes,
                                                    @RequestParam(required = false) Integer minMinutes,
                                                    @RequestParam(required = false) Integer maxScore,
                                                    @RequestParam(required = false) Integer minScore,
                                                    @RequestParam(required = false) String type) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



}
