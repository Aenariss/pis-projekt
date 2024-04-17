/**
 * Book information for book listing.
 * @author Lukas Petr <xpetrl06>
 */
import {useContext, useMemo, useState} from "react";
import {Button, Card} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {Cart} from "react-bootstrap-icons";
import Price from "./Price";
import { CartContext } from "../../context/CartContext";

/**
 * Component for showing information about book in book listing.
 * @param props.id Book id
 * @param props.name Name of book
 * @param props.author Author information
 * @param props.price Price
 * @param props.discount Discount in percentage.
 * @param props.availableQuantity Amount of available quantity.
 * @component
 */
export default function Book({
  id,
  name,
  author,
  price,
  image,
  discount,
  availableQuantity,
}) {
  const navigate = useNavigate();

  /** Click on the book - show detail */
  function handleClick() {
    navigate(`/book/${id}`);
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
            <Card.Text className="fs-5 mt-2 text-end py-2" as='div'>
              <Price price={price} discount={discount} />
            </Card.Text>
            <AddToCard bookId={id} availableQuantity={availableQuantity}/>
          </div>
        </Card.Body>
      </Card>
  );
}

/**
 * Button for adding item to card.
 * @param props.id Book id
 * @param props.availableQuantity Amount of available quantity.
 * @component
 */
function AddToCard({
  bookId: id,
  availableQuantity,
}) {
  const {addOneToCart, getAmountForId} = useContext(CartContext);
  const amount = useMemo(() => getAmountForId(id), [id, getAmountForId]);
  // Variable for temporary changing look of add to cart button when user adds item to cart.
  const [added, setAdded] = useState(false);
  /**
   * Adds one piece of the book to cart.
   */
  function handleAddToCart(e) {
    e.stopPropagation();
    setAdded(true);
    setTimeout(()=> setAdded(false), 1000);
    addOneToCart(id);
  }
  const disableButton = added || amount >= availableQuantity;
  let text = null;
  if (added) {
    text = "Added";
  } else if (amount >= availableQuantity) {
    text = "Sold out"
  } else {
    text = (
      <>
        <span>Add to cart</span>
        <span className="align-middle ms-2 pb-1">
          <Cart size={20}/>
        </span>
      </>
    );
  }
  return (
    <Button onClick={handleAddToCart} disabled={disableButton} className="w-100">
      {text}
    </Button>
  );
}
