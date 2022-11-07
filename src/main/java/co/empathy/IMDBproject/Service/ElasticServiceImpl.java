package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Model.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ElasticServiceImpl implements ElasticService {
    private final ElasticEngine elasticEngine;

    public ElasticServiceImpl(ElasticEngine searchEngine) {
        this.elasticEngine = searchEngine;
    }
    @Override
    public String listIndices() throws IOException {
        return elasticEngine.listIndices();
    }
    @Override
    public Boolean createIndex(String name, String source) {
        return elasticEngine.createIndex(name, source);
    }
    @Override
    public Boolean indexDoc(String indexName, Movie movie) {
        return elasticEngine.indexDoc(indexName, movie);
    }

    @Override
    public void indexData(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String line;
        while ((line = reader.readLine()) != null) {

        }

    }
}

