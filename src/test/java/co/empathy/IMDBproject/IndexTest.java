package co.empathy.IMDBproject;

import co.empathy.IMDBproject.Service.ElasticEngine;
import co.empathy.IMDBproject.Service.ElasticService;
import co.empathy.IMDBproject.Service.ElasticServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


    @ExtendWith(MockitoExtension.class)
    public class IndexTest {


        @Test
        void givenNewIndexName_ReturnTrue() throws IOException, InterruptedException {
            String indexName="test";

            ElasticEngine elasticEngine = mock(ElasticEngine.class);
            given(elasticEngine.createIndex(indexName,null)).willReturn(true);

            ElasticService elasticService = new ElasticServiceImpl(elasticEngine);

            Boolean actualResponse = elasticService.createIndex(indexName,null);
            assertThat(true).isEqualTo(actualResponse);
        }



    }

