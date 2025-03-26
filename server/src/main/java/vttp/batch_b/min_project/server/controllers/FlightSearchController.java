package vttp.batch_b.min_project.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch_b.min_project.server.exceptions.QueryNotReceivedException;
import vttp.batch_b.min_project.server.models.FlightOffer;
import vttp.batch_b.min_project.server.models.dtos.FlightQuery;
import vttp.batch_b.min_project.server.services.FlightService;

@Controller
@RequestMapping("/api")
public class FlightSearchController {
    
    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    private FlightService airSvc;

    @GetMapping(path="/search", 
        consumes=MediaType.APPLICATION_JSON_VALUE
        //produces=MediaType.APPLICATION_JSON_VALUE
        )
    public ResponseEntity<List<FlightOffer>> getSearch (@RequestParam MultiValueMap<String, String> form) {
        
        if (form.getFirst("dep_airport") != null || form.getFirst("arr_airport") != null) {
            
            String depAir = form.getFirst("dep_airport");
            String arrAir = form.getFirst("arr_airport");
            
            String depIata = airSvc.getDataWithAirport(depAir);
      
            String arrIata = airSvc.getDataWithAirport(arrAir);

            FlightQuery query = new FlightQuery(
                depIata,
                arrIata,
                form.getFirst("dep_date"),
                form.getFirst("arr_date"),
                form.getFirst("class"),
                form.getFirst("trip_type"),
                Integer.valueOf(form.getFirst("passenger")),
                form.getFirst("timezone"),
                form.getFirst("currency")
            );
            
            List<FlightOffer> flights = airSvc.getFlights(query);
            //System.out.println("at controller: " + flights);
            return ResponseEntity.ok(flights);
        } 
        throw new QueryNotReceivedException("query not found");
    }

    @GetMapping(path="/google-maps-key")
    public ResponseEntity<String> getGoogleApiKey() {

        JsonObject json = Json.createObjectBuilder()
            .add("apiKey", apiKey)
            .build();
        //System.out.println(json.toString());

        return ResponseEntity.ok(json.toString());
    }
}
