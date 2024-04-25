/**
 * PIS Projekt 2024
 * Statistics.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.api.dto.StatisticsRequest;
import pis.api.dto.StatisticsResponse;
import pis.data.Order;
import pis.service.OrderManager;
import pis.service.RegisteredUserManager;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Nejprodavanejsi kategorie -- Pro kazdou kategorii ziskat pocty prodeju za urcite obdobi, graf prodeju podle kategorii v danem obdobi

 * Zisky za zvolenou dobu -- Pro vsechny objednavky v rozmezi secist, kolik staly

 * Nejprodavanejsi produkty za danou dobu (Top 10?)
 */

/**
 * REST API for statistics -- admins only
 */
@RolesAllowed("admin")
@Path("/statistics")
public class Statistics {

    /**
     * Local class to parse the given time (which is a string) to the LocalDateTime format
     */
    class TimeConvertor {

        private LocalDateTime fromDate;
        private LocalDateTime toDate;
        public LocalDateTime getFromDate() {
            return fromDate;
        }
        
        public LocalDateTime getToDate() {
            return toDate;
        }

        public TimeConvertor(String fromDate, String toDate) {

            LocalDateTime tmpFrom = LocalDateTime.parse(fromDate, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime tmpTo = LocalDateTime.parse(toDate, DateTimeFormatter.ISO_DATE_TIME);

            this.fromDate = tmpFrom;
            this.toDate = tmpTo;
        }
    }

    @Inject
    private OrderManager orderManager;

    /**
     * Returns number of total orders in given time range
     * @param r Request with both start and end dates -- both are required!
     * @return Dates & the corresponding number of sales
     */
    @POST
    @Path("/allSalesInTime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Successfully retrieved orders",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.ARRAY, implementation = StatisticsResponse.class))
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid request format",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
        ),
        @APIResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
        )
    })
    public Response allSalesInTime(StatisticsRequest r) {

        /* Check if the request contained both the from and to time */
        if (!r.valid()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        /* Convert the json string into java LocalDateTime */
        TimeConvertor times;
        try {
            times = new TimeConvertor(r.getFromDate(), r.getToDate());

            // From cannot be after To
            if (times.getFromDate().compareTo(times.getToDate()) > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
            }
        } // Error converting to correct data type -- bad request
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        /* Find numbers of orders from in between the given dates */
        List<Object[]> ords = this.orderManager.salesTimeRange(times.getFromDate(), times.getToDate());

        // Empty list initialization
        List<StatisticsResponse> resp = new ArrayList<>();

        // Populate the list with values returned from the database
        for (Object[] ord : ords) {
            Date time  = (Date) ord[0];
            Long occurences = (long) ord[1];
            StatisticsResponse tmp = new StatisticsResponse(time, occurences);
            resp.add(tmp);
        }

        /* 
         * Now, if this was a production version, there'd need to be some more preprocessing here.
         * We can't exactly process thousands of values in user's profile.
         * However, this will do for our purposes
         */

        return Response.status(Response.Status.OK).entity(resp).build();
    }
}
