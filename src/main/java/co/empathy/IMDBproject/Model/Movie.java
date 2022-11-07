package co.empathy.IMDBproject.Model;

import lombok.Value;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Value
@With
public class Movie {

    //Cannot use lists -> problems
    String id;
    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
    Boolean isAdult;
    int startYear;
    int endYear;
    int runtimeMinutes;
    //List<String> genres;
    String genres;
    double averageRating ;
    int numVotes;
    //List<String> akas;
    //List<String> starring;



}
