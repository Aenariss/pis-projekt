/**
 * PIS Projekt 2024
 * RenewToken.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.data.RegisteredUser;

import pis.service.RegisteredUserManager;

import java.security.Key;
import java.util.Date;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Path("/renewToken")
public class RenewToken {

    @Inject
	private RegisteredUserManager userManager;

    @Context
    private SecurityContext securityContext;

    private static final Key key = Keys.hmacShaKeyFor("12345789101112131415161718192021222324252627282930".getBytes());

    /**
     * Ask server for new token, only if the user is already logged in
     */
    @RolesAllowed({"admin", "employee", "user"})
    @POST
    public Response renewToken() {

        RegisteredUser u = userManager.findByEmail(securityContext.getUserPrincipal().getName());

        if (u == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("This shouldn't happen! Something went wrong").build();
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
}
