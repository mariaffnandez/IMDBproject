# IMDBproject

## Empathy Academy: Learning project


Creating an IMDB-like search engine using Elasticsearch
### Tech stack 
- Java
- Maven
- SpringBoot
- Elasticsearch
- Docker

### Installation

1. Clone the repo (provisionally)
   ```sh
   git clone -b New_features https://github.com/mariaffnandez/IMDBproject.git

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


### Endpoints:

#### `GET /search`
It´s used to search movies using filters

Parameters:
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
