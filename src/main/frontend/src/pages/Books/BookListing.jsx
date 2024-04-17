/**
 * Lists books.
 * @author Lukas Petr <xpetrl06>
 */
import {Col, Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import Book from "./Book";

/**
 * Component for listing of books.
 * @param {Object} props.products List of product descriptions.
 * @component
 */
export default function BookListing({products}) {
  // Removing duplicit - search can return multiple amount of the same item
  const ids = [];
  const productsCleaned = [];
  products.forEach(book => {
    if (!ids.includes(book.id)) {
      productsCleaned.push(book);
      ids.push(book.id);
    }
  })
  return (
    <Container>
      <Row md={5} >
        {productsCleaned.map(book =>
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
      {/* TODO add pagination  */}
    </Container>
  );
}