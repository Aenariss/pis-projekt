/**
 * PIS Projekt 2024
 * Authentication.java
 * @author Vojtech Fiala <xfiala61>
 * @author Lukas Petr <xpetrl06>
 */

package pis.service;

import pis.data.RegisteredUser;

import java.security.Key;
import java.security.Principal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response;

/**
 * Filter to check if request has an authorization header
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class Authentication implements ContainerRequestFilter {
    
    @Inject
	private RegisteredUserManager userManager;

    @Context
    private SecurityContext securityContext;

    @Context
    private ResourceInfo resourceInfo;

    private static final Key key = Keys.hmacShaKeyFor("12345789101112131415161718192021222324252627282930".getBytes()); // very stupid but w/e

    /**
     * Perform filter on each incoming request
     */
    public void filter(ContainerRequestContext ctx) {
        String authorizationHeader = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
        // If there is an authorization header and the JWT is ok, set security context
        // so we can get the user which make the request in API endpoints.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length()); // get the useful token part

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // If it got here, token should be valid

                String email = claims.getSubject();
                RegisteredUser u = userManager.findByEmail(email);
                
                // User not found
                if (u == null) {
                    ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }

                ctx.setSecurityContext(new SecurityContext() { // Custom SecurityContext, isUserInRole is used in @RolesAllowed annotation
                    @Override
                    public Principal getUserPrincipal() {
                        return u::getEmail;
                    }
    
                    @Override
                    public boolean isUserInRole(String role) {
                        return u.getRole().equals(role);
                    }
    
                    @Override
                    public boolean isSecure() {
                        return securityContext.isSecure();
                    }
    
                    @Override
                    public String getAuthenticationScheme() {
                        return "Basic";
                    }
                });
                return;
            } 
            catch (Exception e) {
                // The JWT was not ok, if there was rules allowed annotation on the API
                // abort the processing, if not nothing happens.
                if (resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) {
                    System.out.println(e);
                    ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            }
        }
        // There was not authorization header, but the api is restricted for certain users
        // abort the processing.
        else if (resourceInfo.getResourceMethod().isAnnotationPresent(RolesAllowed.class)) { // Invalid authorization header
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
