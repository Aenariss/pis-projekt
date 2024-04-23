/**
 * Lists books.
 * @author Lukas Petr <xpetrl06>
 */
import {Button, Col, InputGroup, Row, Stack} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import Book from "./Book";
import {useMemo, useState} from "react";
import Paginator from "../../components/Paginator";

const AMOUNT_OF_BOOKS_ON_PAGE = 10;

// Options how books can be sorted
// From the cheapest to the most expensive
const SORT_BY_PRICE_ASC = 0;
// From the most expensive to cheapest
const SORT_BY_PRICE_DES = 1;
// From without discount to the biggest discount
const SORT_BY_DISCOUNT_ASC = 2;
// From the biggest discount to without discount
const SORT_BY_DISCOUNT_DES = 3;

/**
 * Component for listing of books.
 * @param {Object} props.products List of product descriptions.
 * @component
 */
export default function BookListing({products}) {
  const [currentPage, setCurrentPage] = useState(1);
  const [sortBy, setSortBy] = useState(SORT_BY_PRICE_ASC);
  const amountOfPages = Math.ceil(products.length / AMOUNT_OF_BOOKS_ON_PAGE);

  // Sorted product by choice set in sortBy
  const sortedProducts = useMemo(() => {
    switch (sortBy) {
      case SORT_BY_PRICE_DES:
        return products.toSorted((book1, book2) => book2.currentPrice - book1.currentPrice);
      case SORT_BY_PRICE_ASC:
        return products.toSorted((book1, book2) => book1.currentPrice - book2.currentPrice);
      case SORT_BY_DISCOUNT_ASC:
        return products.toSorted((book1, book2) => {
          // returning negative means book1 should be before book2
          // if both books do not have discount returns the more expensive
          if (! book1.discount && ! book2.discount) {
            return book2.price - book1.price;
          }
          // if book2 has discount but book 1 not return book1 (without discount)
          if (! book1.discount) return -1;
          // if book1 has discount but book 2 not return book2 (without discount)
          if (! book2.discount) return 1;
          // otherwise put first the one with lower discount
          return book1?.discount?.discount - book2?.discount?.discount;
        });
      case SORT_BY_DISCOUNT_DES:
        return products.toSorted((book1, book2) => {
          // if both books do not have discount returns the cheapest
          if (! book1.discount && ! book2.discount) {
            return book1.price - book2.price;
          }
          // book 1 has discount should be before book 2
          if (book1.discount && ! book2.discount) return -1;
          // book 2 has discount should be before book 1
          if (!book1.discount && book2.discount) return 1;
          // if book 1 has bigger discount should be before book 2
          return book2?.discount?.discount - book1?.discount?.discount;
        });
      default : return products;
    }
  }, [sortBy, products]);

  const productsOnPage = sortedProducts.slice((currentPage-1) * AMOUNT_OF_BOOKS_ON_PAGE,
                                        currentPage * AMOUNT_OF_BOOKS_ON_PAGE);
  let content = (
    <div className='display-5 text-center text-muted mt-5'>
      No books were found
    </div>
  );
  if (products.length > 0) {
    content = (
      <>
        <SortChoice sortBy={sortBy} onChange={(choice) => setSortBy(choice)} />
        <Stack className="h-100">
          <Row md={5}>
            {productsOnPage.map(book =>
              <Col key={book.id} className="p-2">
                <Book id={book.id}
                      name={book.name}
                      author={book.author}
                      price={book.price}
                      image={book.image}
                      discount={book?.discount?.discount}
                      availableQuantity={book?.availableQuantity}/>
              </Col>
            )}
          </Row>
        </Stack>
        <Paginator activePage={currentPage}
                   pages={amountOfPages}
                   setActivePage={setCurrentPage}/>
      </>
    );
  }
  return (
    <Container className="h-100">
      {content}
    </Container>
  );
}
/**
 * Component for user to choose how should be the books sorted.
 * @param props.sortBy Current choice.
 * @param props.onChange Handler called with new choice.
 */
function SortChoice({
  sortBy,
  onChange,
}) {
  return(
    <InputGroup>
      <Button onClick={() => onChange(SORT_BY_PRICE_ASC)}
              active={sortBy === SORT_BY_PRICE_ASC}
              variant='outline-primary'>
        Price (low to high)
      </Button>
      <Button onClick={() => onChange(SORT_BY_PRICE_DES)}
              active={sortBy === SORT_BY_PRICE_DES}
              variant='outline-primary'>
        Price (high to low)
      </Button>
      <Button onClick={() => onChange(SORT_BY_DISCOUNT_ASC)}
              active={sortBy === SORT_BY_DISCOUNT_ASC}
              variant='outline-primary'>
        Discount (low to high)
      </Button>
      <Button onClick={() => onChange(SORT_BY_DISCOUNT_DES)}
              active={sortBy === SORT_BY_DISCOUNT_DES}
              variant='outline-primary'>
        Discount (high to low)
      </Button>
    </InputGroup>
  );
}
