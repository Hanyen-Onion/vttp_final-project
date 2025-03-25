package vttp.batch_b.min_project.server.repository;

import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch_b.min_project.server.exceptions.CachingException;
import vttp.batch_b.min_project.server.models.FlightOffer;

@Repository
public class FlightResultsRepository {
    @Autowired @Qualifier("redis")
    private RedisTemplate<String, String> template;

    //smembers
    public List<JsonObject> getFlightsOffers(String key) {
        
        Set<String> strings = template.opsForSet().members(key);

        List<JsonObject> flights = strings.stream()
            .map(str -> {
                return Json.createReader(new StringReader(str)).readObject();
            })
            .collect(Collectors.toList());
        //System.out.println("smembers: " + flights);
        return flights;
    }

    //sadd
    public void cacheFlightOffers(List<FlightOffer> flights, String key) throws JsonProcessingException {
        try {
            flights.forEach(f -> {
                template.opsForSet().add(key, FlightOffer.toJson(f).toString());
            });
            //expires
            template.expire(key, Duration.ofHours(1));

        } catch (Exception e) {
            e.printStackTrace();
            throw new CachingException("unable to cache to redis");
        }
    }

    // exists
    public Boolean exists(String key) {
        return template.hasKey(key);
    }

}
