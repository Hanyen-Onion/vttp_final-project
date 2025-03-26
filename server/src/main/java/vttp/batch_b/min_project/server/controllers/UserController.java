package vttp.batch_b.min_project.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonObject;
import vttp.batch_b.min_project.server.exceptions.EmailNotFoundException;
import vttp.batch_b.min_project.server.models.User;
import vttp.batch_b.min_project.server.models.dtos.Country;
import vttp.batch_b.min_project.server.services.UserService;

@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class UserController {
   
    @Autowired
    private UserService uSvc;

    @PostMapping(path="/create-user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        
        if (uSvc.createNewUser(user)) {
            System.out.println(user);
            return ResponseEntity.ok(User.toJson(user).toString());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }



    @GetMapping(path="/validate-email")
    public ResponseEntity<String> validateEmail(@RequestParam String email) {

        if (!uSvc.isUserRegistered(email))
            throw new EmailNotFoundException("email not found: %s is not registered".formatted(email));

        return ResponseEntity.ok(null);
    }

    @GetMapping(path="/countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        return ResponseEntity.ok(uSvc.getAllCountries());
    }
}
