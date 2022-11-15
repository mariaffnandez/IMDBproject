package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie;


import java.io.IOException;
import java.util.List;

public interface ElasticEngine {
    String listIndices() throws IOException;
    Boolean createIndex(String name, String source);
    Boolean deleteIndex(String name);
    Boolean indexDoc(String indexName, Movie movie);
    Boolean indexMultipleDocs(String indexName, List<Movie> movies) throws IOException;
    List<Movie> getQuery(Filters filters) throws IOException;


}
