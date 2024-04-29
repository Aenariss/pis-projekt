/**
 * Page with book detail.
 * @author Lukas Petr
 */
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {
  Spinner,
} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import {api} from "../../api";
import BookInfo from "./BookInfo";
import BookEditHistory from "./BookEditHistory";

/**
 * Page with book detail.
 * @component
 */
export default function BookDetailPage() {
  const params = useParams();
  // Getting book id from url
  const bookId = Number(params.bookId);
  // Product information fetched from backend.
  const [product, setProduct] = useState(null);

  // Fetching book information.
  useEffect(() => {
    api.get(`/productdescription/${bookId}`)
      .then(response => setProduct(response.data))
  }, [bookId]);

  // Loading data
  if (product === null) {
    return (
      <Spinner animation="border" role="status">
        <span className="visually-hidden">Loading...</span>
      </Spinner>
    );
  }
  return (
    <Container>
      <h1>{product.name}</h1>
      <BookInfo product={product} bookId={bookId}/>
      <BookEditHistory bookId={bookId} />
    </Container>
  );
}

