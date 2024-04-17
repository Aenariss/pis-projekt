/**
 * Component for changing amount of item in the cart.
 * @author Lukas Petr
 */
import { useContext, useMemo, useState } from 'react';
import { Button, ButtonGroup } from 'react-bootstrap';
import { Dash, Plus } from 'react-bootstrap-icons';
import { CartContext } from '../../context/CartContext';

/**
 * Component for changing amount of item in the cart.
 * @param props.bookId Book id
 * @param props.availableQuantity Amount of available quantity.
 * @component
 */
export default function AmountChanger({
  bookId,
  availableQuantity,
}) {
  const {getAmountForId,
         addOneToCart,
         setAmountForId,
         removeOneFromCart} = useContext(CartContext);
  const [amount, setAmount] = useState(getAmountForId(bookId));
  /**
   * Increments amount of product in cart.
   */
  function amountIncrement() {
    addOneToCart(bookId);
    setAmount(amount+1)
  }
  /**
   * Decrements amount of product in cart.
   */
  function amountDecrement(e) {
    removeOneFromCart(bookId);
    setAmount(amount-1)
  }
  /**
   * Handles manual change of amount.
   */
  function handleChange(e) {
    const newAmount = e.target.value;
    if (newAmount === '') {
      setAmount(newAmount)
    }
    else if (0 <= newAmount && newAmount <= availableQuantity) {
      setAmountForId(bookId, Number(newAmount));
      setAmount(newAmount)
    }
  }
  const availableQuantityText = useMemo(() => {
    const q = availableQuantity;
    if (q > 20) return null;
    if (q === 0) {
      return (
        <div className="text-danger mt-2">
          (sold out)
        </div>
      )
    }
    return (
      <div className="text-muted mt-2">
        ({q} piece{q > 1 ? 's' : ''} in stock)
      </div>
    );
  }, [availableQuantity]);

  return (
    <div>
      <ButtonGroup>
        <Button variant='primary'
                onClick={amountDecrement}
                disabled={amount === 0 || amount === ''}><Dash size={30}/>
        </Button>
          <input style={{width: '40px'}} className='text-center'
                value={amount} onChange={handleChange}>
          </input>
        <Button variant='primary'
                onClick={amountIncrement}
                disabled={amount >= availableQuantity}>
          <Plus size={30}/>
        </Button>
      </ButtonGroup>
      { availableQuantityText }
    </div>
  )
}
