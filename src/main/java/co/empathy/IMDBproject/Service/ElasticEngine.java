package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Facets.Facets;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;
import co.empathy.IMDBproject.Model.Response;


import java.io.IOException;
import java.util.List;

public interface ElasticEngine {
    String listIndices() throws IOException;
    Boolean createIndex(String name, String source);
    Boolean deleteIndex(String name);
    Boolean indexDoc(String indexName, Movie movie);
    Boolean indexMultipleDocs(String indexName, List<Movie> movies) throws IOException;
    Response getQuery(Filters filters) throws IOException;
    Response getSearchQuery(String searchText) throws IOException;
    Facets getAggregations(String field) throws IOException;


}
