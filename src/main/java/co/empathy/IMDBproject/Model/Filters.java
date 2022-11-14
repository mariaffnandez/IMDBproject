package co.empathy.IMDBproject.Model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
public class Filters {
    String gender;
    int minYear;
    int maxYear;
    int minScore;
    int maxScore;
    int maxMinutes;
    int minMinutes;
    String type;

}