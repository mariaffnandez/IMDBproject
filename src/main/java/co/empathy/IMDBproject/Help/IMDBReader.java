package co.empathy.IMDBproject.Help;

import co.empathy.IMDBproject.Model.Movie;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IMDBReader {
    private BufferedReader basicsReader;
    private BufferedReader ratingsReader;
    private BufferedReader akasReader;
    private BufferedReader crewReader;
    String ratingLine;
    String akasLine;
    String crewLine;
    private IMDbData data;
    public boolean moreLines=true;



    public IMDBReader(MultipartFile basicsFile, MultipartFile ratingsFile,MultipartFile akasFile, MultipartFile crewFile) throws IOException {
        this.basicsReader =reader(basicsFile);
        this.ratingsReader =reader(ratingsFile);
        this.akasReader =reader(akasFile);
        this.crewReader =reader(crewFile);
        this.data= new IMDbData();
        //provisional
        ratingLine=ratingsReader.readLine();
        akasLine=akasReader.readLine();
        crewLine=crewReader.readLine();

    }
    //returns the movie with all the info readed from the files
    public Movie readMovie () throws IOException {

        String basicLine;
        Movie movie;

        while (moreLines) {

            //read basics and create a movie
            basicLine=basicsReader.readLine();
            movie= data.setBasicsLines(basicLine);

            if (basicLine == null)
                moreLines = false;

            //set ratings
            //if the rating line has the same id
            if (data.sameId(basicLine,ratingLine)){
                //adds the rating info
                data.setRatings(ratingLine,movie);
                //and read the next rating line
                ratingLine=ratingsReader.readLine();
            }
            //set akas
            //there are different akas for a unique movie
            while (data.sameId(basicLine,akasLine)){
                data.setAkas(data.readAkas(akasLine),movie);
                akasLine=akasReader.readLine();
            }
            //set directors
            if (data.sameId(basicLine,crewLine)){
                //adds the rating info
                data.setDirector(crewLine,movie);
                //and read the next rating line
                crewLine=crewReader.readLine();
            }



            return movie;

            }

        return null;
    }


    public BufferedReader reader(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //read the first line (no useful info)
            String types=reader.readLine();
            return reader;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
