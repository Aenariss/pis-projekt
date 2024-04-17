/**
 * Lists books.
 * @author Lukas Petr <xpetrl06>
 */
import {Col, Pagination, Row, Stack} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import Book from "./Book";
import {useState} from "react";
import Paginator from "../../components/Paginator";

const AMOUNT_OF_BOOKS_ON_PAGE = 10;

/**
 * Component for listing of books.
 * @param {Object} props.products List of product descriptions.
 * @component
 */
export default function BookListing({products}) {
  const [currentPage, setCurrentPage] = useState(1);
  const amountOfPages = Math.ceil(products.length / AMOUNT_OF_BOOKS_ON_PAGE);
  const productsOnPage = products.slice((currentPage-1) * AMOUNT_OF_BOOKS_ON_PAGE,
                                        currentPage * AMOUNT_OF_BOOKS_ON_PAGE);

  return (
    <Container className="h-100">
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
    </Container>
  );
}
