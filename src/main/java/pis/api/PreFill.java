/**
 * Api for prefilling the DB for testing purposes.
 * @author Lukas Petr <xpetrl06>
 * @author Tomas Ondrusek <xondru18>
 */

package pis.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pis.data.BookAuthor;
import pis.data.Category;
import pis.data.Discount;
import pis.data.Language;
import pis.data.Order;
import pis.data.OrderStatus;
import pis.data.OrderUserInfo;
import pis.data.ProductDescription;
import pis.service.BookAuthorManager;
import pis.service.CategoryManager;
import pis.service.DiscountManager;
import pis.service.LanguageManager;
import pis.service.OrderManager;
import pis.service.OrderUserInfoManager;
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

    @Inject
    DiscountManager discountManager;

    @Inject
    OrderUserInfoManager orderUserInfoManager;

    @Inject
    OrderManager orderManager;

    @GET
    public Response prefillDB() {
        // Note some source for filling the DB: wordery.com, wikipedia, the things does
        // not have to exists,
        // information can be edited, lorem ipsum generator can be used, ... just to
        // fill db

        // Categories
        Category crime = new Category("Crime",
                "Narratives that centre on criminal acts and especially on the investigation, either by an amateur or a professional detective, of a crime, often a murder.");
        crime = categoryManager.save(crime);
        Category travelWritings = new Category("Travel writing",
                "The genre of travel literature or travelogue encompasses outdoor literature, guide books, nature writing, and travel memoirs.");
        travelWritings = categoryManager.save(travelWritings);
        Category drama = new Category("Drama",
                "The genre of travel literature or travelogue encompasses outdoor literature, guide books, nature writing, and travel memoirs.");
        drama = categoryManager.save(drama);
        Category fantasy = new Category("Fantasy",
                "A genre of speculative fiction set in a fictional universe, often inspired by myth and folklore.");
        fantasy = categoryManager.save(fantasy);
        Category romance = new Category("Romance",
                "A genre of fiction that focuses on the romantic relationships between characters.");
        romance = categoryManager.save(romance);
        Category scienceFiction = new Category("Science fiction",
                "A genre of speculative fiction that typically deals with imaginative and futuristic concepts such as advanced science and technology, space exploration, time travel, parallel universes, and extraterrestrial life.");
        scienceFiction = categoryManager.save(scienceFiction);

        // Authors
        BookAuthor agathaCristie = new BookAuthor("Agatha", "Christie");
        agathaCristie = bookAuthorManager.save(agathaCristie);
        BookAuthor jkRowling = new BookAuthor("J.K.", "Rowling");
        jkRowling = bookAuthorManager.save(jkRowling);
        BookAuthor williamShakespeare = new BookAuthor("William", "Shakespeare");
        williamShakespeare = bookAuthorManager.save(williamShakespeare);
        BookAuthor janeAusten = new BookAuthor("Jane", "Austen");
        janeAusten = bookAuthorManager.save(janeAusten);
        BookAuthor georgeOrwell = new BookAuthor("George", "Orwell");
        georgeOrwell = bookAuthorManager.save(georgeOrwell);
        BookAuthor charlesDickens = new BookAuthor("Charles", "Dickens");
        charlesDickens = bookAuthorManager.save(charlesDickens);

        // Discounts
        Discount discount1 = new Discount(10);
        discount1 = discountManager.save(discount1);
        Discount discount2 = new Discount(20);
        discount2 = discountManager.save(discount2);
        Discount discount3 = new Discount(30);
        discount3 = discountManager.save(discount3);
        Discount discount4 = new Discount(40);
        discount4 = discountManager.save(discount4);
        Discount discount5 = new Discount(50);
        discount5 = discountManager.save(discount5);
        Discount discount6 = new Discount(60);
        discount6 = discountManager.save(discount6);

        // Languages
        Language english = new Language("English");
        english = languageManager.save(english);
        Language czech = new Language("Czech");
        czech = languageManager.save(czech);
        Language slovak = new Language("Slovak");
        slovak = languageManager.save(slovak);
        Language spanish = new Language("Spanish");
        spanish = languageManager.save(spanish);
        ;
        Language german = new Language("German");
        german = languageManager.save(german);
        Language french = new Language("French");
        french = languageManager.save(french);

        // Products
        ProductDescription kingfisherHill = new ProductDescription(30,
                "Killings at Kingfisher Hill: The New Hercule Poirot Mystery",
                "The world's greatest detective, Hercule Poirot-legendary star of Agatha Christie's Murder on the Orient Express and Death on the Nile-returns to solve a fiendish new mystery.",
                "9780008264550", 120, agathaCristie, List.of(crime), english, null,
                "https://m.media-amazon.com/images/I/711N2BMxJGL._AC_UF894,1000_QL80_.jpg");
        kingfisherHill = productDescriptionManager.save(kingfisherHill);
        ProductDescription earlyCases = new ProductDescription(45, "Poirot's Early Cases",
                "Captain Hastings recounts 18 of Poirot's early cases from the days before he was famous...",
                "9780008164843", 326, agathaCristie, List.of(crime, drama), english, discount1,
                "https://img-cloud.megaknihy.cz/286607-large/85e79da72e071ca03e75bc370a16ac76/poirot-s-early-cases.jpg");
        earlyCases = productDescriptionManager.save(earlyCases);
        ProductDescription groundTour = new ProductDescription(12,
                " Grand Tour: Letters and Photographs from the British Empire Expedition 1922",
                "Unpublished for 90 years, Agatha Christie's extensive and evocative letters and photographs from her year-long round-the-world trip to South Africa, Australia, New ...",
                "9780007460687", 420, agathaCristie, List.of(travelWritings, fantasy, scienceFiction),
                english, discount2, "https://wordery.com/images/9780007460687.jpg");
        groundTour = productDescriptionManager.save(groundTour);
        ProductDescription harryPotter = new ProductDescription(25, "Harry Potter and the Philosopher's Stone",
                "Harry Potter has never even heard of Hogwarts when the letters start dropping on the doormat at number four, Privet Drive.",
                "9781408855652", 352, jkRowling, List.of(fantasy), english, discount3,
                "https://m.media-amazon.com/images/I/71olNZROxYL._AC_UF1000,1000_QL80_.jpg");
        harryPotter = productDescriptionManager.save(harryPotter);
        ProductDescription harryPotter2 = new ProductDescription(25, "Harry Potter and the Chamber of Secrets",
                "Harry Potter's summer has included the worst birthday ever, doomy warnings from a house-elf called Dobby, and rescue from the Dursleys by his friend Ron Weasley in a magical flying car!",
                "9781408855669", 384, jkRowling, List.of(fantasy), english, discount4,
                "https://m.media-amazon.com/images/M/MV5BMjE0YjUzNDUtMjc5OS00MTU3LTgxMmUtODhkOThkMzdjNWI4XkEyXkFqcGdeQXVyMTA3MzQ4MTc0._V1_.jpg");
        harryPotter2 = productDescriptionManager.save(harryPotter2);
        ProductDescription treeBodyProblem = new ProductDescription(20, "The Three-Body Problem",
                "The Three-Body Problem is the first chance for English-speaking readers to experience this multiple award winning phenomenon from China's most beloved science fiction author, Liu Cixin.",
                "9781784971551", 400, null, List.of(scienceFiction), english, discount5,
                "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1415428227i/20518872.jpg");
        treeBodyProblem = productDescriptionManager.save(treeBodyProblem);
        ProductDescription romeoAndJuliet = new ProductDescription(10, "Romeo and Juliet",
                "Romeo and Juliet is a tragedy written by William Shakespeare early in his career about two young star-crossed lovers whose deaths ultimately reconcile their feuding families.",
                "9780141396477", 400, williamShakespeare, List.of(drama), english, discount1,
                "https://m.media-amazon.com/images/M/MV5BOTViY2FkZDMtNGRmZS00MTZmLTg3MzMtZjA4OWU2Mzk4N2JkXkEyXkFqcGdeQXVyNjkzNjUzODY@._V1_.jpg");
        romeoAndJuliet = productDescriptionManager.save(romeoAndJuliet);
        ProductDescription onenineeigthfour = new ProductDescription(30, "1984",
                "1984 is a dystopian social science fiction novel by English novelist George Orwell. It was published on 8 June 1949 by Secker & Warburg as Orwell's ninth and final book completed in his lifetime.",
                "9780141396477", 400, georgeOrwell, List.of(scienceFiction), english, discount2,
                "https://www.slovart.cz/buxus/images/image_16865_19_v1.jpeg");
        onenineeigthfour = productDescriptionManager.save(onenineeigthfour);
        ProductDescription greatExpectations = new ProductDescription(25, "Great Expectations",
                "Great Expectations is the thirteenth novel by Charles Dickens and his penultimate completed novel, which depicts the education of an orphan nicknamed Pip.",
                "9780141439563", 400, charlesDickens, List.of(drama), english, discount3,
                "https://m.media-amazon.com/images/M/MV5BYTRkYjAyNmEtMzdjMC00MjRhLWJkYjItNTU5OWNlNjc4ODk3XkEyXkFqcGdeQXVyMzA5NjkwNDM@._V1_.jpg");
        greatExpectations = productDescriptionManager.save(greatExpectations);
        ProductDescription loremIpsum = new ProductDescription(25, "Lorem Ipsum",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                "9780141439563", 400, null, List.of(), null, null, null);
        loremIpsum.setDefaulImage();
        loremIpsum = productDescriptionManager.save(loremIpsum);

        OrderUserInfo user1 = new OrderUserInfo("John", "Doe", "+421912333333", "john@doe.sk");
        user1 = orderUserInfoManager.save(user1);
        OrderUserInfo user2 = new OrderUserInfo("Roman", "Piers", "+421912633453", "roman@piers.sk");
        user2 = orderUserInfoManager.save(user2);
        OrderUserInfo user3 = new OrderUserInfo("Arnold", "Schwartzeneger", "+420988645333",
                "arnold@seznam.cz");
        user3 = orderUserInfoManager.save(user3);
        OrderUserInfo user4 = new OrderUserInfo("John", "Wick", "+420912333131", "john@wick.cz");
        user4 = orderUserInfoManager.save(user4);

        OrderStatus inProgress = OrderStatus.IN_PROGRESS;
        OrderStatus confirmed = OrderStatus.CONFIRMED;
        OrderStatus packed = OrderStatus.PACKED;
        OrderStatus shipped = OrderStatus.SHIPPED;
        OrderStatus delivered = OrderStatus.DELIVERED;
        OrderStatus canceled = OrderStatus.CANCELED;
        OrderStatus returned = OrderStatus.RETURNED;

        Order order1 = new Order(delivered, user1, "Slovakia", "Bratislava", "Komenskeho", "32", "03601",
                "Slovakia", "Bratislava", "Komenskeho", "32", "03601");
        order1 = orderManager.save(order1);
        Order order2 = new Order(confirmed, user2, "Slovakia", "Bratislava", "Dlha", "56", "02348", "Slovakia",
                "Bratislava", "Dlha", "56", "02348");
        order2 = orderManager.save(order2);
        Order order3 = new Order(inProgress, user3, "Czech Republic", "Praha", "Karlova", "4", "23456",
                "Czech Republic", "Brno", "Kolejni", "2", "67843");
        order3 = orderManager.save(order3);
        Order order4 = new Order(shipped, user4, "Czech Republic", "Praha", "Palackeho", "3", "234345",
                "Czech Republic", "Brno", "Bozetechova", "2", "63012");
        order4 = orderManager.save(order4);
        Order order5 = new Order(inProgress, user1, "Slovakia", "Bratislava", "Zilinska", "65", "76907",
                "Slovakia", "Kosice", "Kosicka", "342", "12345");
        order5 = orderManager.save(order5);

        return Response.status(Response.Status.OK).entity("DB was prefilled").build();
    }
}
