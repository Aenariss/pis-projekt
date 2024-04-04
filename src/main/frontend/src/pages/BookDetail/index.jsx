/**
 * Page with book detail.
 * @author Lukas Petr
 */
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Button, ButtonGroup, Col, Image, Row, Spinner, Table} from "react-bootstrap";
import {Dash, Plus} from "react-bootstrap-icons";
import Container from "react-bootstrap/Container";
import {api} from "../../api";

/**
 * Page with book detail.
 * @component
 */
export default function BookDetailPage() {
  // Product information fetched from backend.
  const [product, setProduct] = useState(null);
  // TODO cart logic
  const [inCart, setInCart] = useState(0)
  // Getting book id from url
  const params = useParams();
  const bookId = Number(params.bookId);

  // Fetching book information.
  useEffect(() => {
    api.get(`/productdescription/${bookId}`)
      .then(response => setProduct(response.data))
  }, [bookId]);

  /**
   * Increments amount of product in cart.
   */
  function amountIncrement() {
    setInCart(amount => amount+1);
  }
  /**
   * Decrements amount of product in cart.
   */
  function amountDecrement() {
    setInCart(amount => amount-1);
  }
  // Loading data
  if (product === null) {
    return (
      <Spinner animation="border" role="status">
        <span className="visually-hidden">Loading...</span>
      </Spinner>
    );
  }

  return (
    <>
      <h1>{product.name}</h1>
      <Container>
        <Row>
          <Col md={6}>
            <p className="text-secondary">Author: {product?.author?.firstName} {product?.author?.lastName}</p>
            <p>{product.description}</p>
            <Table className="mt-5">
              {/* TODO make the values be links for searching. */}
              <tbody>
              <tr>
                <td>Pages</td>
                <td>{product?.pages}</td>
              </tr>
              <tr>
                <td>Language</td>
                <td>{product?.language?.language}</td>
              </tr>
              <tr>
                <td>ISBN</td>
                <td>{product?.ISBN}</td>
              </tr>
              <tr>
                <td>Categories</td>
                <td>
                  <ul className="list-unstyled">
                    {product.categories.map(category => <li key={category.id}>{category.name}</li>)}
                  </ul>
                </td>
              </tr>
              </tbody>
            </Table>
          </Col>
          <Col md={4} className="text-center">
            <Image src={product?.image} width="55%" className="my-2"/>
            <p className="display-6 mt-2">{product.price} $</p>
            <ButtonGroup>
              <Button variant="primary" onClick={amountDecrement} disabled={inCart === 0}><Dash size={30}/></Button>
              <span className="px-3 d-inline-flex align-items-center border border-primary ">{inCart}</span>
              <Button variant="primary" onClick={amountIncrement}><Plus size={30}/></Button>
            </ButtonGroup>
          </Col>
        </Row>
      </Container>
    </>
  );
}