/**
 * Api for prefilling the DB for testing purposes.
 * @author Lukas Petr <xpetrl06>
 */

package pis.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pis.data.BookAuthor;
import pis.data.Category;
import pis.data.Language;
import pis.data.ProductDescription;
import pis.service.BookAuthorManager;
import pis.service.CategoryManager;
import pis.service.LanguageManager;
import pis.service.ProductDescriptionManager;

import java.util.List;

@Path("/prefill")
public class PreFill {
    @Inject
    CategoryManager categoryManager;
    @Inject
    BookAuthorManager bookAuthorManager;
    @Inject
    LanguageManager languageManager;
    @Inject
    ProductDescriptionManager productDescriptionManager;

    @GET
    public Response prefillDB() {
        // Note some source for filling the DB: wordery.com, wikipedia, the things does not have to exists,
        //      information can be edited, lorem ipsum generator can be used, ... just to fill db
        // Categories
        Category crime = new Category("Crime", "Narratives that centre on criminal acts and especially on the investigation, either by an amateur or a professional detective, of a crime, often a murder.");
        crime = categoryManager.save(crime);
        Category travelWritings = new Category("Travel writing", "The genre of travel literature or travelogue encompasses outdoor literature, guide books, nature writing, and travel memoirs.");
        travelWritings = categoryManager.save(travelWritings);
        // Authors
        BookAuthor agathaCristie = new BookAuthor("Agatha", "Christie");
        agathaCristie = bookAuthorManager.save(agathaCristie);
        // Languages
        Language english = new Language("English");
        english = languageManager.save(english);
        // Products
        ProductDescription kingfisherHill = new ProductDescription(30, "Killings at Kingfisher Hill: The New Hercule Poirot Mystery",
                "The world's greatest detective, Hercule Poirot-legendary star of Agatha Christie's Murder on the Orient Express and Death on the Nile-returns to solve a fiendish new mystery.",
                "9780008264550", 120, agathaCristie, List.of(crime), english, null);
        kingfisherHill = productDescriptionManager.save(kingfisherHill);
        ProductDescription earlyCases = new ProductDescription(45, "Poirot's Early Cases",
                "Captain Hastings recounts 18 of Poirot's early cases from the days before he was famous...",
                "9780008164843", 326, agathaCristie, List.of(crime), english, null);
        earlyCases = productDescriptionManager.save(earlyCases);
        ProductDescription groundTour = new ProductDescription(12, " Grand Tour: Letters and Photographs from the British Empire Expedition 1922",
                "Unpublished for 90 years, Agatha Christie's extensive and evocative letters and photographs from her year-long round-the-world trip to South Africa, Australia, New ...",
                "9780007460687", 420, agathaCristie, List.of(travelWritings), english, null);
        groundTour = productDescriptionManager.save(groundTour);

        return Response.status(Response.Status.OK).entity("DB was prefilled").build();
    }

}