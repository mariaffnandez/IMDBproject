package co.empathy.IMDBproject.Service;

import co.empathy.IMDBproject.Help.IMDbData;
import co.empathy.IMDBproject.Model.Movie;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ElasticServiceImpl implements ElasticService {
    private final ElasticEngine elasticEngine;
    private final String imdbIndex ="test";

    int blockLines= 50000;

    private IMDbData imdb;

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
    public Boolean deleteIndex(String name) {
        return elasticEngine.deleteIndex(name);
    }

    @Override
    public Boolean indexDoc(String indexName, Movie movie) {
        return elasticEngine.indexDoc(indexName, movie);
    }


    @Override
    public Boolean indexIMDBData(MultipartFile basicsFile, MultipartFile ratingFile) throws IOException {
        imdb= new IMDbData(basicsFile,ratingFile);
        //create imdb index and
        createIndex(imdbIndex,imdb.jsonMapping());
        System.out.println("Reading ");

        List<Movie> movieList= new ArrayList<>();
        try {
            //trying strange things
            BufferedReader basicsReader=imdb.reader(basicsFile);
            BufferedReader ratingsReader=imdb.reader(ratingFile);
            int countLines=0;
            String readLine;
            boolean moreLines=true;

            while(moreLines){
                readLine = basicsReader.readLine();
                //adds the movies to the list
                imdb.readMovieTitleBasicsLines(readLine);
                imdb.moviesList(movieList,imdb.readMovieTitleBasicsLines(readLine));
                countLines ++;
                if (countLines==blockLines || readLine==null){
                    elasticEngine.indexMultipleDocs(imdbIndex,movieList);
                    //prepare the next list
                    countLines=0;
                    movieList.clear();
                    if (readLine==null)
                        moreLines=false;
                }
            }
            System.out.println("Indexed");
            return true;



        } catch (IOException e) {

            return false;
        }

    }
    /*
    @Override
    public Boolean indexIMDBTitleBasics(MultipartFile multipartFile) throws IOException {

        System.out.println("Reading "+multipartFile.getOriginalFilename());
        imdb= new IMDbData();
        List<Movie> movieList= new ArrayList<>();
        try {
            InputStream inputStream = multipartFile.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //first line read (no movie's info)
            String types=reader.readLine();

            int countLines=0;
            String readLine;
            boolean moreLines=true;

            while(moreLines){
                readLine = reader.readLine();
                //adds the movies to the list
                imdb.moviesList(movieList,imdb.readMovieTitleBasicsLines(readLine));
                countLines ++;
                if (countLines==blockLines || readLine==null){
                    elasticEngine.indexMultipleDocs(indexName,movieList);
                    //prepare the next list
                    countLines=0;
                    movieList.clear();
                    if (readLine==null)
                        moreLines=false;
                }
            }
            System.out.println("Indexed");
            return true;



        } catch (IOException e) {

            return false;
        }

    }


*/

}

