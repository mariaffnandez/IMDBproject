package co.empathy.IMDBproject.ReadData;


import co.empathy.IMDBproject.Help.IMDbData;
import co.empathy.IMDBproject.Model.Movie.Akas;
import co.empathy.IMDBproject.Model.Movie.Movie;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
public class IMDBReaderTest {
    @Test
    void givenTwoMovieswithSameIDReturnTrue() {
        Movie movie1= new Movie();
        Movie movie2= new Movie();
        movie1.setTconst("aaa");
        movie2.setTconst("aaa");

        IMDbData dataIMDB = new IMDbData();
        Boolean response = dataIMDB.sameId(movie1.getTconst(),movie2.getTconst());
        assertThat(true).isEqualTo(response);
    }

    @Test
    void givenNullArray(){
        Movie movie= new Movie();
        Akas aka1= new Akas();

        ArrayList<Akas> expectedArray= new ArrayList<>();
        expectedArray.add(aka1);

        movie.setAkas(expectedArray);

        Akas aka2= new Akas();


        IMDbData dataIMDB = new IMDbData();
        dataIMDB.setAkas(aka2,movie);


        ArrayList<Akas> expectedList= new ArrayList<>();
        expectedList.add(aka1);
        expectedList.add(aka2);


        assertThat(movie.getAkas()).isEqualTo(expectedArray);

    }
    @Test
    void smallerID(){
        String line1="tt0001";
        String  line2="tt0002";
        IMDbData dataIMDB = new IMDbData();
        boolean result= dataIMDB.smallerID(line1,line2);
        assertThat(result).isEqualTo(true);

    }
    @Test
    void biggerID(){
        String line1="tt00010";
        String  line2="tt0002";
        IMDbData dataIMDB = new IMDbData();
        boolean result= dataIMDB.smallerID(line1,line2);
        assertThat(result).isEqualTo(false);

    }






}
