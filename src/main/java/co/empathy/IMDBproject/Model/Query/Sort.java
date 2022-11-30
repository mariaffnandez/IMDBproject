package co.empathy.IMDBproject.Model.Query;

import lombok.Builder;
import lombok.Value;



@Value
@Builder
public class Sort {
    String sortYear;
    String sortRating;
    String sortNumVotes;


}
