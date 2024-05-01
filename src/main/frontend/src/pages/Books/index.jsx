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
 * @returns {JSX.Element} - BooksPage component
 * @constructor BooksPage
 */
export default function BooksPage() {
  const [products, setProducts] = useState(null);
  const [searchParams] = useSearchParams();

  useEffect(() => {
    // Searching for books?
    const query = searchParams.get('query');
    // Or filtering books?
    const categoryIds = searchParams.getAll('categoryIds');
    const authorIds = searchParams.getAll('authorIds');
    const languageIds = searchParams.getAll('languageIds');
    const priceFrom = searchParams.get('priceFrom');
    const priceTo = searchParams.get('priceTo');
    if (query && query !== '') {
      // User is searching for book.
      api.post('/productdescription/search', {query})
        .then(response => {
          if (response.status === 200) {
            setProducts(response.data);
          }
        })
    } else if (categoryIds.length > 0 || languageIds.length > 0 || authorIds.length > 0
               || priceFrom || priceTo ) {
      let body = {};
      if (categoryIds.length > 0) body.categoryIds = categoryIds;
      if (authorIds.length > 0) body.authorIds = authorIds;
      if (languageIds.length > 0) body.languageIds = languageIds;
      if (priceFrom) body.priceFrom = Number(priceFrom);
      if (priceTo) body.priceTo = Number(priceTo);
      // User is filtering by category
      api.post('/productdescription/filter', body)
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
      <Row className='vh-75'>
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