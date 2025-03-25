package vttp.batch_b.min_project.server.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.batch_b.min_project.server.models.User;
import vttp.batch_b.min_project.server.services.AuthService;

@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${redirect.url}")
    private String redirectUrl;

    @Autowired
    private AuthService authSvc;

    @PostMapping(path="/google-login", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> authLogin(@RequestBody MultiValueMap<String, String> form) {

        String jwt = form.getFirst("credential");
        String csrfToken = form.getFirst("g_csrf_token");

        System.out.println("jwt:\n" + jwt);
        System.out.println("csrf:\n" + csrfToken);

        User user = authSvc.getGoogleIdToken(jwt);

        if (authSvc.isGoogleTokenValidated(user) == true) {
            return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create(redirectUrl))
            .body(User.toJson(user).toString());
        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}