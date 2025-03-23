package vttp.batch_b.min_project.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vttp.batch_b.min_project.server.exceptions.EmailNotFoundException;
import vttp.batch_b.min_project.server.services.UserService;


@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class UserController {
   
    @Autowired
    private UserService uSvc;

    @GetMapping(path="/validate-email")
    public ResponseEntity<String> validateEmail(@RequestParam String email) {

        if (!uSvc.isUserRegistered(email))
            throw new EmailNotFoundException("email not found: %s is not registered".formatted(email));

        return ResponseEntity.ok(null);
    }
}
