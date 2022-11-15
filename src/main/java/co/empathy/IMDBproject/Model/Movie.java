package co.empathy.IMDBproject.Model;

import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor


public class Movie {


    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
    Boolean isAdult;
    int startYear;
    int endYear;
    int runtimeMinutes;
    String[] genres;
    double averageRating ;
    int numVotes;




}
