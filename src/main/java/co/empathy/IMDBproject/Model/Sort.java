package co.empathy.IMDBproject.Model;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class Sort {
    String sortYear;
    String sortRating;
    String sortNumVotes;


}
