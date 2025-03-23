package vttp.batch_b.min_project.server.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch_b.min_project.server.exceptions.AirportNotFoundException;
import vttp.batch_b.min_project.server.models.Airport;
import vttp.batch_b.min_project.server.models.FlightOffer;
import vttp.batch_b.min_project.server.models.dtos.AirportQuery;
import vttp.batch_b.min_project.server.repository.AirportRepository;
import vttp.batch_b.min_project.server.utils.TimeParser;

import static vttp.batch_b.min_project.server.services.AuthService.AMADEUS;

@Service
public class AirportService {

    @Value("${forex.api.key}")
    private String exApiKey;

    @Autowired
    private AuthService authSvc;

    @Autowired
    private AirportRepository airRepo;

    public static final String EX_URL = "https://api.exchangeratesapi.io/v1/latest";
    public static final String FLIGHT_OFFER_URL = "https://test.api.amadeus.com/v2/shopping/flight-offers";

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

    public void getFlightOffer(AirportQuery query) {

        String url = "";

        if (query.tripType().equals("one-way")) {
            url = UriComponentsBuilder
            .fromUriString(FLIGHT_OFFER_URL)
            .queryParam("originLocationCode", query.depAirport())
            .queryParam("destinationLocationCode", query.arrAirport())
            .queryParam("departureDate", query.depDate())
            .queryParam("adults", query.passenger())
            .queryParam("travelClass", query.cabinClass().toUpperCase())
            .queryParam("nonStop", true)
            .queryParam("max", 50)
            .toUriString();
        } else if (query.tripType().equals("round-trip")) {
            url = UriComponentsBuilder
            .fromUriString(FLIGHT_OFFER_URL)
            .queryParam("originLocationCode", query.depAirport())
            .queryParam("destinationLocationCode", query.arrAirport())
            .queryParam("departureDate", query.depDate())
            .queryParam("returnDate", query.arrDate())
            .queryParam("adults", query.passenger())
            .queryParam("travelClass", query.cabinClass().toUpperCase())
            .queryParam("nonStop", true)
            .queryParam("max", 50)
            .toUriString();
        }
        
        String accessCode = authSvc.retrieveAccessToken(AMADEUS);

        RequestEntity<Void> req = RequestEntity
            .get(url)
            .header("Authorization", "Bearer " + accessCode)
            .build();
        
        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();
            
            //getflightData(payload);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public void getflightData(String payload) throws IOException {
    public void getflightData(File payload) throws IOException {
        JsonReader jr = Json.createReader(new BufferedReader(new FileReader(payload)));
        //JsonReader jr = Json.createReader(new StringReader(payload));
        JsonObject json = jr.readObject();
        JsonArray data = json.getJsonArray("data");

        for (int i = 0; i < data.size(); i++) {
            JsonArray segments = data.getJsonObject(i).getJsonArray("itineraries").getJsonObject(0).getJsonArray("segments");
            
            for (int j = 0; j < segments.size(); j++) {
                JsonObject seg = segments.getJsonObject(j);
                //String segId = seg.getString("id");
                String depCode = seg.getJsonObject("departure").getString("iataCode", "");
                String depTerminal = seg.getJsonObject("departure").getString("terminal","");
                String depTime = seg.getJsonObject("departure").getString("at","");
                String arrCode = seg.getJsonObject("arrival").getString("iataCode", "");
                String arrTerminal = seg.getJsonObject("arrival").getString("terminal","");
                String arrTime = seg.getJsonObject("arrival").getString("at","");
                String carrierCode = seg.getString("carrierCode");
                String duration = seg.getString("duration","");

                String carrier = json.getJsonObject("dictionaries").getJsonObject("carriers").getString(carrierCode);
                String currency = data.getJsonObject(i).getJsonObject("price").getString("currency");
                String price = data.getJsonObject(i).getJsonObject("price").getString("total");

                FlightOffer fo = new FlightOffer();
                fo.setDuration(TimeParser.parseDuration(duration));

            }           
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


