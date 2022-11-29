package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Movie.Movie;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;


public interface ElasticService {

    Boolean createIndex(String name, InputStream mapping);
     Boolean deleteIndex(String name);

    Boolean indexDoc(String indexName, Movie movie);


    Boolean indexIMDBData(MultipartFile basicsFile, MultipartFile ratingFile,MultipartFile akaFile,MultipartFile crewFile,MultipartFile principalsFile) throws IOException;
}