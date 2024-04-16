/**
 * Component for showing price in book listing.
 * @author Lukas Petr
 */
import { Badge } from "react-bootstrap";

/**
 * Component for showing price in book listing.
 * @param {number} props.price Original price.
 * @param {number} props.discount Number of percentage down.
 * @component
 */
export default function Price({price, discount}) {
  if (discount) {
    let newPrice = price * ((100 - discount) / 100);
    return (
      <div className="d-flex flex-row">
        <del className='me-auto' style={{fontSize: '14px'}}>
          {price.toFixed(2)} $
        </del>
        <Badge bg='danger'>
          <b>
            {newPrice.toFixed(2)} $
          </b>
        </Badge>
      </div>
    );
  }
  return(
    <div>
      <b>
        {price.toFixed(2)} $
      </b>
    </div>
  );
}