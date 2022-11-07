package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public interface ElasticService {
    String listIndices() throws IOException;

    Boolean createIndex(String name, String source);

    Boolean indexDoc(String indexName, Movie movie);

    void indexData(MultipartFile multipartFile) throws IOException;
}