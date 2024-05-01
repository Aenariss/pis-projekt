/**
 * Showing cart info.
 * Button + how many items does person has in cart.
 * @author Lukas Petr
 */

import { useContext, useEffect, useMemo, useState } from 'react';
import { Button } from 'react-bootstrap';
import { Cart } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';
import { CartContext } from '../../context/CartContext';

/**
 * Showing cart info.
 * @returns {JSX.Element} - CartInfo component
 * @constructor CartInfo
 */
export default function CartInfo() {
  const navigate = useNavigate();
  const {items} = useContext(CartContext);
  const [amount,setAmount] = useState(0);
  useEffect(() => {
    setAmount(
      Array.from(items.values()).reduce((total,amount) => total+amount, 0)
    );
  }, [items]);

  return (
    <Button className=' position-relative'
            onClick={() => navigate('/cart')}>
      <span className='me-2'><Cart size={30}/></span>
      <span>Cart</span>
      {amount !== 0 && (
        <span className='position-absolute top-100 start-0 translate-middle badge rounded-pill bg-danger'>
          {amount > 99 ? '99+' : amount}
        </span>
      )}
    </Button>
  )
}