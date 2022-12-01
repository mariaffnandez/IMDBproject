package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Movie.Movie;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;


public interface ElasticService {

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
     *
     * @param basicsFile, the tsv file which contains basic´s info
     * @param ratingFile, the tsv file which contains rating´s info
     * @param akaFile, the tsv file which contains akas`s info
     * @param crewFile, the tsv file which contains crew´s info
     * @param principalsFile, he tsv file which contains principal´s info
     * @return true if all the docs were successfully indexed
     * @throws IOException
     */


    Boolean indexIMDBData(MultipartFile basicsFile, MultipartFile ratingFile,MultipartFile akaFile,MultipartFile crewFile,MultipartFile principalsFile) throws IOException;
}