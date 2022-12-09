# IMDBproject

## Empathy Academy: Learning project
We´ve created an IMDb-like search engine based on different filters using the IMDb's data sets. 

### Tech stack 
- Java
- Maven
- SpringBoot
- Elasticsearch
- Docker

### Installation

1. Clone the repo 
   ```sh
   git clone https://github.com/mariaffnandez/IMDBproject.git

   ```
   
2. Run the project
   ```sh
   docker-compose up –-build -d

   ```
    ```sh
   docker-compose up -d

   ```
3. To shut down containers:
   ```sh
   docker-compose down

   ```
#### Image
- searchAPI: 
   ```sh
   mariaffnandez/imdb-api:latest
    ```




### Endpoints:

#### `GET /search`
It´s used to search movies using filters

##### Parameters:
- **genres** (String) - Value of genres to filter by multiple genres. It should be sent separeted by commas (e.g genres=Action,Sci-Fi)  
- **type** (String) - Value of title type to filter by values. It should be sent in the same way as genres parameter 
- **maxYear** (Integer) - Max value of start year to filter by 
- **minYear** (Integer) - Min value of start year to filter by
- **maxMinutes** (Integer) - Max value of runtime minutes to filter by
- **minMinutes** (Integer) - Min value of runtime minutes to filter by
- **maxScore** (Double) - Max value of average rating to filter by
- **minScore** (Double) - Min value of average rating to filter by
- **maxNHits**  (Integer)  - Upper bound of the number of hits returned (500 by default)
- **sortRating** (String) - (asc/desc) The ordering of the sort 
- **sortNumVotes** (String) - (asc/desc) The ordering of the sort 
- **sortYear** (String) - (asc/desc) The ordering of the sort 


Example: `http://localhost:8080/search?maxYear=2022&minYear=2019&genres=Action&sortYear=Asc`

##### Output: 
Example of a possible output from this endpoint

``` javascript
{
    "hits": [
         {
            "tconst": "tt08080648",
            "titleType": "movie",
            "primaryTitle": "Ring of Silence",
            "originalTitle": "Ring of Silence",
            "isAdult": false,
            "startYear": 2019,
            "endYear": -1,
            "runtimeMinutes": 95,
            "genres": [
                "Drama"
            ],
            "averageRating": 9.0,
            "numVotes": 509,
            "akas": [
                {
                    "title": "Ring of Silence",
                    "region": "GB",
                    "language": "\\N",
                    "isOriginalTitle": false
                },
                {
                    "title": "Ring of Silence",
                    "region": "\\N",
                    "language": "\\N",
                    "isOriginalTitle": false
                },
                {
                    "title": "Ring of Silence",
                    "region": "US",
                    "language": "\\N",
                    "isOriginalTitle": false
                }
            ],
            "directors": [
                {
                    "nconst": "nm5029090"
                }
            ],
            "starring": [
                {
                    "name": {
                        "nconst": "nm4340529"
                    },
                    "characters": "\\N"
                },
                ...
            ]
        },
        ...
     ],
        "facets": [
        {
            "type": "value",
            "facet": "facetgenres",
            "values": [
                {
                    "value": "Drama",
                    "count": 507
                },
            ...
            ]
        },
        {
            "type": "value",
            "facet": "facettitleType",
            "values": [
                {
                    "value": "tvEpisode",
                    "count": 910
                },
           ... 
            ]
        }
    ]
}
        

```
 
#### `POST /index`
It´s used to create an index of documents from different files

Parameters:
- **basics** * - File containing the basics information (title.basics.tsv)
- **ratings** * - File containing the ratings information (title.ratings.tsv)
- **akas** * - File containing the ratings information (title.rating.tsv)
- **principals** * - File containing the principals information (title.principals.tsv)
- **crew** * - File containing the crew information (title.crew.tsv)
- **episodes** * - File containing the episodes information (title.episode.tsv)

<sub>* required </sub>
