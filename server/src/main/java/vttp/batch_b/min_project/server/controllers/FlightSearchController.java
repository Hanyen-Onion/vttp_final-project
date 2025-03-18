package vttp.batch_b.min_project.server.controllers;

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
import vttp.batch_b.min_project.server.exceptions.QueryNotFoundException;
import vttp.batch_b.min_project.server.models.Airport;
import vttp.batch_b.min_project.server.models.dtos.AirportQuery;
import vttp.batch_b.min_project.server.services.AirportService;

@Controller
@RequestMapping("/api")
public class FlightSearchController {
    
    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    private AirportService airSvc;

    @GetMapping(path="/search", 
        consumes=MediaType.APPLICATION_JSON_VALUE
        //produces=MediaType.APPLICATION_JSON_VALUE
        )
    public ResponseEntity<String> getSearch (@RequestParam MultiValueMap<String, String> form) {
        
        if (form.getFirst("dep_airport") != null || form.getFirst("arr_airport") != null) {
            
            String depAir = form.getFirst("dep_airport")
                .toLowerCase().replace("airport", "").strip();
            String arrAir = form.getFirst("arr_airport")
                .toLowerCase().replace("airport", "").strip();
            
            Airport dep = airSvc.getDataWithAirport(depAir);
            Airport arr = airSvc.getDataWithAirport(arrAir);
    
            AirportQuery query = new AirportQuery(
                dep.getAirport(),
                arr.getAirport(),
                form.getFirst("dep_date"),
                form.getFirst("arr_date"),
                form.getFirst("class"),
                form.getFirst("trip_type"),
                Integer.valueOf(form.getFirst("passenger"))
            );
            
            airSvc.getOneWayTrip(query);

        } else {

            throw new QueryNotFoundException("query not found");
        }


        return ResponseEntity.ok("{}");
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
