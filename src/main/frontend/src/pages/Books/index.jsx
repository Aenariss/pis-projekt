/**
 * Page for listing of books.
 * @author Lukas Petr
 */

import {useEffect, useState} from "react";
import {Col, Row, Spinner} from "react-bootstrap";
import {useSearchParams} from "react-router-dom";
import Container from "react-bootstrap/Container";
import {api} from "../../api";
import Filter from "./Filter";
import BookListing from "./BookListing";

/**
 * Page for listing of books.
 * @component
 */
export default function BooksPage() {
  const [products, setProducts] = useState(null);
  const [searchParams, setSearchParams] = useSearchParams()

  useEffect(() => {
    // Searching for books?
    const query = searchParams.get('query');
    // Or filtering books?
    const categoryIds = searchParams.getAll('categoryIds');
    if (query && query !== '') {
      // User is searching for book.
      api.post('/productdescription/search', {query})
        .then(response => {
          if (response.status === 200) {
            setProducts(response.data);
          }
        })
    } else if (categoryIds.length > 0) {
      // User is filtering by category
      api.post('/productdescription/filter', {categoryIds})
        .then(response => {
          if (response.status === 200) {
            setProducts(response.data);
          }
        });
    } else {
      // Not searching, not filtering, get all products
      api.get('/productdescription/')
        .then(response => {
          if (response.status === 200) {
            setProducts(response.data);
          }
        });
    }
  }, [searchParams]);

  return (
    <Container>
      <Row>
        <Col md={3}>
          <Filter />
        </Col>
        <Col md={9}>
          {products === null
            ? (<Spinner animation="border" role="status">
                <span className="visually-hidden">Loading...</span>
              </Spinner>)
            : <BookListing products={products} />
          }
        </Col>
      </Row>
    </Container>
  );
}