package co.empathy.IMDBproject.Model;

import co.empathy.IMDBproject.Model.Facets.Facets;
import co.empathy.IMDBproject.Model.Movie.Movie;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Response {
    List<Movie> hits;
    ArrayList<Facets> facets;


}
