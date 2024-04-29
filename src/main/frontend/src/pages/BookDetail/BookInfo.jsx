/**
 * Component for showing information about book.
 * @author Lukas Petr
 */
import { Badge, Col, Image, Row, Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import AmountChanger from '../../components/Cart/AmountChanger';

/**
 * Component for showing information about book.
 * @param props.product Product information.
 * @param props.boobId Id of the book.
 */
export default function BookInfo({
  product,
  bookId
}) {
  return (
    <Row>
      <Col md={6}>
        <p className="text-secondary">
          Author:{' '}
          <Link as='a'
                role='button'
                className='link-secondary link-underline link-underline-opacity-0 link-underline-opacity-100-hover'
                to={`/?authorIds=${product?.author?.id}`}>
            {product?.author?.firstName} {product?.author?.lastName}
          </Link>
        </p>
        <p>{product?.description}</p>
        <Table className="mt-5">
          <tbody>
          <tr>
            <td>Pages</td>
            <td>{product?.pages}</td>
          </tr>
          <tr>
            <td>Language</td>
            <td>
              <Link as='a'
                  role='button'
                  className='link-dark link-underline link-underline-opacity-0 link-underline-opacity-100-hover'
                  to={`/?languageIds=${product?.language?.id}`}>
                {product?.language?.language}
              </Link>
            </td>
          </tr>
          <tr>
            <td>ISBN</td>
            <td>{product?.ISBN}</td>
          </tr>
          <tr>
            <td>Categories</td>
            <td>
              <ul className="list-unstyled">
                {product.categories.map(category => (
                  <Link as='a'
                        title={category?.description}
                        role='button'
                        key={`category-${category?.id}`}
                        className='link-dark link-underline link-underline-opacity-0 link-underline-opacity-100-hover'
                        to={`/?categoryIds=${category?.id}`}>
                    <li key={category.id}>{category.name}</li>
                  </Link>
                ))}
              </ul>
            </td>
          </tr>
          </tbody>
        </Table>
      </Col>
      <Col md={4} className="text-center">
        <Image src={product?.image} width="55%" className="my-2"/>
        <Price price={product.price} discount={product?.discount?.discount} />
        <div className='pb-2 mb-1'>Quantity in the cart:</div>
        <AmountChanger bookId={bookId}
                        availableQuantity={product.availableQuantity}/>
      </Col>
    </Row>
  );
}

/**
 * Component for showing price of product.
 * @param {number} props.price Original price.
 * @param {number} props.discount Number of percentage down.
 * @component
 */
function Price({price, discount}) {
  if (discount) {
    let newPrice = price * ((100 - discount) / 100);
    return (
      <div>
        <del className='me-auto'>
          Original price: {price.toFixed(2)} $
        </del>
        <div style={{fontSize: '25px'}}>
          <Badge bg='danger'>
            - {discount} %
          </Badge>
        </div>
        <div className='display-6 text-danger pt-2 pb-3'>
          <b>
            {newPrice.toFixed(2)} $
          </b>
        </div>
      </div>
    );
  }
  return(
    <div>
      <p className='display-6 mt-2'>{price.toFixed(2)} $</p>
    </div>
  );
}