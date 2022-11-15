package co.empathy.IMDBproject.Model;

import lombok.Builder;
import lombok.Value;

//@Data
//@NoArgsConstructor
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

}