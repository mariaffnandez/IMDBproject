package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Query.Filters;
import co.empathy.IMDBproject.Model.Movie.Movie;
import co.empathy.IMDBproject.Model.Response;
import co.empathy.IMDBproject.Model.Query.Sort;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ElasticEngine {


    /**
     *
     * @param name, the index name
     * @param mapping, an input streamn with the settings, mapping and aliases
     * @return true if the index was successfully created
     */
    Boolean createIndex(String name, InputStream mapping);
    /**
     *
     * @param name, the index name that will be deleted
     * @return, trye is the index was successfully deleted
     */
    Boolean deleteIndex(String name);
    /**
     *
     * @param indexName, the index name
     * @param movie, the movie that will be added to the index
     * @return true if it was successfully added
     */
    Boolean indexDoc(String indexName, Movie movie);
    /**
     * Sends multiple docs in one request
     * @param indexName, the index name
     * @param movies, a list with movies that will be created or replaced if they exist
     * @return true if the operation was successfully done
     * @throws IOException
     */
    Boolean indexMultipleDocs(String indexName, List<Movie> movies) throws IOException;
    /**
     *
     * @param filters, a filter object with the filters that will be applied
     * @param sort, a sort object with the fields and the order
     * @return a Response object that contains the hits and the facets
     * @throws IOException
     */
    Response getQuery(Filters filters, Sort sort) throws IOException;

    /**
     *
     * @param searchText, the movie´s name they are looking for
     * @return a Response object that contains the hits and the facets
     * @throws IOException
     */
    Response getSearchQuery(String searchText) throws IOException;


}
