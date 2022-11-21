package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Facets.Facets;
import co.empathy.IMDBproject.Model.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;
import co.empathy.IMDBproject.Model.Response;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ElasticEngine {
    String listIndices() throws IOException;
    Boolean createIndex(String name, InputStream mapping);
    Boolean deleteIndex(String name);
    Boolean indexDoc(String indexName, Movie movie);
    Boolean indexMultipleDocs(String indexName, List<Movie> movies) throws IOException;
    Response getQuery(Filters filters,int maxHits) throws IOException;
    Response getSearchQuery(String searchText) throws IOException;


}
