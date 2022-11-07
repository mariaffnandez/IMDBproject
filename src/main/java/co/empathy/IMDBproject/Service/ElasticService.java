package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Movie;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


public interface ElasticService {
    String listIndices() throws IOException;

    Boolean createIndex(String name, String source);

    Boolean indexDoc(String indexName, Movie movie);

    void indexIMDBData(MultipartFile multipartFile) throws IOException;
}