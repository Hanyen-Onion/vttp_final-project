package vttp.batch_b.min_project.server.services;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vttp.batch_b.min_project.server.exceptions.AirportNotFoundException;
import vttp.batch_b.min_project.server.models.Airport;
import vttp.batch_b.min_project.server.models.dtos.AirportQuery;
import vttp.batch_b.min_project.server.repository.AirportRepository;

@Service
public class AirportService {

    @Value("${forex.api.key}")
    private String exApiKey;

    @Value("${flight.api.key}")
    private String flightApiKey;

    @Autowired
    private AirportRepository airRepo;

    public static final String EX_URL = "https://api.exchangeratesapi.io/v1/latest";
    public static final String ONE_WAY_URL = "https://api.flightapi.io/onewaytrip";

    public Airport getDataWithAirport(String airport) {

        List<Document> docs = airRepo.findAirportByName(
            airport.toLowerCase().replace("airport", "").strip()
        );

        //no airport found
        if (docs.size() < 1) {
            throw new AirportNotFoundException("cannot find %s airport".formatted(airport));
        }

        Document doc = docs.get(0);
        
        return Airport.docToAirport(doc, airport);
    }

    public void getOneWayTrip(AirportQuery query) {
        String url = UriComponentsBuilder
            .fromUriString(ONE_WAY_URL)
            .pathSegment(
                flightApiKey,
                query.depAirport(),
                query.arrAirport(),
                query.depDate(),
                query.passenger().toString(),
                "0", "0",
                query.cabinClass(),
                "SGD"
            ).toUriString();

            System.out.println(query);
            System.out.println(url);
        
        RequestEntity req = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);

            String payload = resp.getBody();
            System.out.println(payload);
        } catch (Exception e) {
        }

    }

    public void getExchangeRate(String currency) {

        String url = UriComponentsBuilder
            .fromUriString(EX_URL)
            .queryParam("access_key", exApiKey)
            .queryParam("base", "SGD")
            .toUriString();

        RequestEntity req = RequestEntity.get(url).build();

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);

            String payload = resp.getBody();
        } catch (Exception e) {
        }
    }


}

// public void insertAirports(List<Document> docs) {
//     mongoRepo.insertMany(docs);
// }

// public List<Document> getAirports(int limit, int offset) {
//     List<Airport> airports = airportRepo.getAllByBatch(limit, offset);

//     List<Document> docs = airports.stream()
//         .map(obj -> toDoc(obj))
//         .collect(Collectors.toList());
    
//     return docs;
// }


