/**
 * PIS Projekt 2024
 * Login.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.data.RegisteredUser;
import pis.data.LoginRequest;

import pis.service.RegisteredUserManager;

import java.security.Key;
import java.util.Date;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Path("/login")
public class Login {

    @Inject
	private RegisteredUserManager userManager;

    private static final Key key = Keys.hmacShaKeyFor("12345789101112131415161718192021222324252627282930".getBytes());

    /**
     * Login
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest r) {
        
        RegisteredUser u = userManager.findByEmail(r.getEmail());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Unknown user!").build();
        }
        if (u.validatePassword(r.getPassword()) ) {

            // TODO: REMOVE - only for tests
            if (u.getEmail().matches("test@test.cz")) {
                u.setRole("admin");
                userManager.save(u);
            }
            
            String token = Jwts.builder()
                    .setSubject(u.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token expires in 1 hour
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
            
            token = token + ';' + u.getRole(); // Add user role after the token, separated by semicolon
            return Response.status(Response.Status.OK).entity(token).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Wrong password!").build();
    }
}
