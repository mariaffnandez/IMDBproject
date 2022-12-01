package co.empathy.IMDBproject.Model.Query;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class Filters {

    String[] genre;
    Integer minYear;
    Integer maxYear;
    Double minScore;
    Double maxScore;
    Integer maxMinutes;
    Integer minMinutes;
    String[] type;
    Integer maxNHits;

}