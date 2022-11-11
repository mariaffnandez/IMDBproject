package co.empathy.IMDBproject.ReadData;


import co.empathy.IMDBproject.Help.IMDbData;
import co.empathy.IMDBproject.Model.Movie;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class IMDBReaderTest {
    @Test
    void givenTwoMovieswithSameIDReturnTrue() throws IOException, InterruptedException {
        Movie movie1= new Movie();
        Movie movie2= new Movie();
        movie1.setTconst("aaa");
        movie2.setTconst("aaa");

        IMDbData dataIMDB = new IMDbData();
        Boolean response = dataIMDB.sameId(movie1.getTconst(),movie2.getTconst());
        assertThat(true).isEqualTo(response);
    }

}
