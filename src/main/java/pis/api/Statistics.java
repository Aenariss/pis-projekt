/**
 * PIS Projekt 2024
 * Statistics.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.api;

import pis.api.dto.StatisticsRequest;
import pis.api.dto.StatisticsEarningsDTO;
import pis.api.dto.StatisticsItemDTO;
import pis.api.dto.StatisticsItemsInTime;
import pis.api.dto.StatisticsOrdersInTime;
import pis.data.Order;
import pis.service.OrderManager;

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
 * REST API for statistics -- admins only
 */
@Path("/statistics")
public class Statistics {

    @Inject
    private OrderManager orderManager;

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

    /**
     * Private method used to get the time values from the request
     * If the request was not valid, return null
     * @param r Request
     * @return TimeConvertor object with from and to dates
     */
    private TimeConvertor getTimesFromRequest(StatisticsRequest r) {
        /* Convert the json string into java LocalDateTime */
        TimeConvertor times_err = null;

        /* Check if the request contained both the from and to time */
        if (!r.valid()) {
            return times_err;
        }

        TimeConvertor times;
        try {
            times = new TimeConvertor(r.getFromDate(), r.getToDate());

            // From cannot be after To
            if (times.getFromDate().compareTo(times.getToDate()) > 0) {
                return times_err;
            }
        } // Error converting to correct data type -- bad request
        catch (Exception e) {
            return times_err;
        }
        return times;
    }

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
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatisticsEarningsDTO.class))
        ),
        @APIResponse(responseCode = "400", description = "Invalid request format"),
        @APIResponse(responseCode = "401", description = "Unauthorized")
    })
    public Response allSalesInTime(StatisticsRequest r) {

        TimeConvertor times = this.getTimesFromRequest(r);

        if (times == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        /* Find numbers of orders from in between the given dates */
        List<Object[]> ords = this.orderManager.salesTimeRange(times.getFromDate(), times.getToDate());

        // Empty list initialization
        List<StatisticsOrdersInTime> resp = new ArrayList<>();
        long total = 0;

        if (ords == null) {
            StatisticsEarningsDTO dto = new StatisticsEarningsDTO();
            dto.setPerDay(resp);
            dto.setTotal(total);
            return Response.status(Response.Status.OK).entity(dto).build();
        }

        // Populate the list with values returned from the database
        for (Object[] ord : ords) {
            Date time  = (Date) ord[0];
            Long occurences = (long) ord[1];
            total += occurences;
            StatisticsOrdersInTime tmp = new StatisticsOrdersInTime(time, occurences);
            resp.add(tmp);
        }

        StatisticsEarningsDTO dto = new StatisticsEarningsDTO();
        dto.setPerDay(resp);
        dto.setTotal(total);


        /* 
         * Now, if this was a production version, there'd need to be some more preprocessing here.
         * We can't exactly process thousands of values in user's profile.
         * However, this will do for our purposes
         */

        return Response.status(Response.Status.OK).entity(resp).build();
    }

    /**
     * Endpoint to get the most sold categories (with the nubmer of sales) for specific time range
     * @param r Valid request with fromDate and toDate attributes
     * @return List with category name and corresponding number
     */
    @POST
    @Path("/mostSoldCategories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Successfully retrieved orders per category",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatisticsItemDTO.class))
        ),
        @APIResponse(responseCode = "400", description = "Invalid request format"),
        @APIResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public Response mostSoldCategories(StatisticsRequest r) {
        TimeConvertor times = this.getTimesFromRequest(r);

        if (times == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        /* Find numbers of categories from in between the given dates */
        List<Object[]> ords = this.orderManager.categoriesSalesInRange(times.getFromDate(), times.getToDate());

        // Empty list initialization
        List<StatisticsItemsInTime> resp = new ArrayList<>();
        long total = 0;

        if (ords == null) {
            StatisticsItemDTO dto = new StatisticsItemDTO();
            dto.setPerDay(resp);
            dto.setTotal(total);
            return Response.status(Response.Status.OK).entity(dto).build();
        }

        // Populate the list with values returned from the database
        for (Object[] ord : ords) {
            String cat  = (String) ord[0];
            Long occurences = (long) ord[1];
            total += occurences;
            StatisticsItemsInTime tmp = new StatisticsItemsInTime(cat, occurences);
            resp.add(tmp);
        }

        StatisticsItemDTO dto = new StatisticsItemDTO();
        dto.setPerDay(resp);
        dto.setTotal(total);

        return Response.status(Response.Status.OK).entity(dto).build();
    }

    /**
     * Endpoint to get the TOP 20 most sold items (with the nubmer of sales) for specific time range
     * @param r Valid request with fromDate and toDate attributes
     * @return List with item name and corresponding number
     */
    @POST
    @Path("/mostSoldItems")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Successfully retrieved orders per item",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatisticsItemDTO.class))
        ),
        @APIResponse(responseCode = "400", description = "Invalid request format"),
        @APIResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public Response mostSoldItems(StatisticsRequest r) {
        TimeConvertor times = this.getTimesFromRequest(r);

        if (times == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        /* Find numbers of items from in between the given dates */
        List<Object[]> ords = this.orderManager.itemSalesInRange(times.getFromDate(), times.getToDate());

        // Empty list initialization
        List<StatisticsItemsInTime> resp = new ArrayList<>();

        long total = 0;

        if (ords == null) {
            StatisticsItemDTO dto = new StatisticsItemDTO();
            dto.setPerDay(resp);
            dto.setTotal(total);
            return Response.status(Response.Status.OK).entity(dto).build();
        }

        // Populate the list with values returned from the database
        for (Object[] ord : ords) {
            String cat  = (String) ord[0];
            Long occurences = (long) ord[1];
            total += occurences;
            StatisticsItemsInTime tmp = new StatisticsItemsInTime(cat, occurences);
            resp.add(tmp);
        }

        StatisticsItemDTO dto = new StatisticsItemDTO();
        dto.setPerDay(resp);
        dto.setTotal(total);

        return Response.status(Response.Status.OK).entity(dto).build();
    }

    /**
     * Endpoint to get how much $$$ the shop earned in given time -- returns earnings per day and total earning
     * @param r Valid request with fromDate and toDate attributes
     * @return List with dates & correspondiong earnings and total earnings
     */
    @POST
    @Path("/incomePerTime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Successfully retrieved orders per item",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = StatisticsEarningsDTO.class))
        ),
        @APIResponse(responseCode = "400", description = "Invalid request format"),
        @APIResponse(responseCode = "401", description = "Unauthorized")
        }
    )
    public Response incomePerTime(StatisticsRequest r) {
        TimeConvertor times = this.getTimesFromRequest(r);

        if (times == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request!").build();
        }

        /* Find earnings per day from in between the given dates */
        List<Object[]> ords = this.orderManager.earningsInRange(times.getFromDate(), times.getToDate());

        // Empty list initialization
        List<StatisticsOrdersInTime> resp = new ArrayList<>();

        long total = 0;

        if (ords == null) {
            StatisticsEarningsDTO dto = new StatisticsEarningsDTO();
            dto.setPerDay(resp);
            dto.setTotal(total);
            return Response.status(Response.Status.OK).entity(dto).build();
        }

        // Populate the list with values returned from the database
        for (Object[] ord : ords) {
            Date d  = (Date) ord[0];
            double tmpNum = (double) ord[1]; // idk why i need to first save it
            Long occurences = (long) tmpNum; // but this doesnt work otherwise
            total += occurences;
            StatisticsOrdersInTime tmp = new StatisticsOrdersInTime(d, occurences);
            resp.add(tmp);
        }

        StatisticsEarningsDTO dto = new StatisticsEarningsDTO();
        dto.setPerDay(resp);
        dto.setTotal(total);

        return Response.status(Response.Status.OK).entity(dto).build();
    }
}
