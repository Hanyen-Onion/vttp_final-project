package vttp.batch_b.min_project.server.services;

import java.io.StringReader;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch_b.min_project.server.models.User;
import vttp.batch_b.min_project.server.repository.AuthRepository;

@Service
public class AuthService {

    @Value("${amadeus.api.key}")
    private String amadeusApiKey;

    @Value("${amadeus.api.secret.key}")
    private String amadeusSecretKey;

    @Autowired
    private AuthRepository authRepo;

    @Autowired
    private UserService userSvc;

	public static final Logger logger = Logger.getLogger(AuthService.class.getName());

    public static final String AMADEUS = "amadeus";

    public static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/tokeninfo";
    public static final String GOOGLE_ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String AMADEUS_ACCESS_TOKEN_URL = "https://test.api.amadeus.com/v1/security/oauth2/token";

    public String retrieveAccessToken(String name) {
        //check expiration
        if (!authRepo.exists(name)) 
            getAmadeusAccessToken();

        return authRepo.getAccessToken(name);
    }

    public void getAmadeusAccessToken() {

        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", amadeusApiKey);
        form.add("client_secret", amadeusSecretKey);

        RequestEntity<MultiValueMap<String,String>> req = RequestEntity
            .post(AMADEUS_ACCESS_TOKEN_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form, MultiValueMap.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject obj = reader.readObject();

            String authState = obj.getString("state");
            int expireTime = obj.getInt("expires_in");
            String accessToken = obj.getString("access_token");

            System.out.println("access code: " + accessToken + "\n");
            authRepo.saveAccessToken(AMADEUS, accessToken, expireTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean isGoogleTokenValidated(User user) {
        return user != null;
    }

    public User getGoogleIdToken(String tokenId) {

        String url = UriComponentsBuilder.fromUriString(GOOGLE_TOKEN_URL)
                                        .queryParam("id_token", tokenId)
                                        .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();                                     
        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            
            if(resp.getStatusCode().is2xxSuccessful()) {
                
                //google decode jwt and send back the info as payload after authentication
                String payload = resp.getBody();
                JsonReader reader = Json.createReader(new StringReader(payload));
                JsonObject json = reader.readObject();
                User user = User.jsonToUser(json);

                //check if user exist
                if (!userSvc.isUserRegistered(user.getEmail())) {
                    //save user
                    if (userSvc.createNewUser(user))
                        logger.info("%s has been signed up".formatted(user.getEmail()));
                }
                return user;
            }
        } catch (HttpClientErrorException ex) {
            System.err.println(">>> error:\ninvalid token or unable to reach google: " + ex.getMessage());
        }
        return null;
    }


    



}
