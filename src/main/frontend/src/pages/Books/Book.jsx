/**
 * Book information for book listing.
 * @author Lukas Petr <xpetrl06>
 */
import {useState} from "react";
import {Button, Card} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {Cart} from "react-bootstrap-icons";
import Price from "./Price";

/**
 * Component for showing information about book in book listing.
 * @param props.id Book id
 * @param props.name Name of book
 * @param props.author Author information
 * @param props.price Price
 * @param props.discount Discount in percentage.
 * @component 
 */
export default function Book({id, name, author, price, image, discount}) {
  const navigate = useNavigate();
  // Variable for temporary changing look of add to cart button when user adds item to cart.
  const [added, setAdded] = useState(false);

  /** Click on the book - show detail */
  function handleClick() {
    navigate(`/book/${id}`);
  }

  /**
   * Adds one piece of the book to cart.
   */
  function addToCart(e) {
    e.stopPropagation();
    setAdded(true);
    setTimeout(()=> setAdded(false), 1000);
    // TODO implement add to cart logic
  }

  return (
      <Card border="primary" onClick={handleClick} role="button" className="d-flex flex-column h-100">
        <Card.Img variant="top" src={image} height={150} className="object-fit-scale"/>
        <Card.Body className="d-flex flex-column justify-content-sm-between">
          <Card.Title className="fs-6 align-content-stretch">
            {name}
          </Card.Title>
          <div>
            <Card.Subtitle className="text-muted" style={{fontSize: "14px"}}>
                {author?.firstName} {author?.lastName}
            </Card.Subtitle>
            <Card.Text className="fs-5 mt-2 text-end">
              <Price price={price} discount={discount} />
            </Card.Text>
            <Button onClick={addToCart} disabled={added} className="w-100">
              {added
                ? ("Added")
                : (<><span>Add to cart</span><span className="align-middle ms-2 pb-1"><Cart size={20}/></span></>)
              }
            </Button>
          </div>
        </Card.Body>
      </Card>
  );
}
