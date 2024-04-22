/**
 * Component for searching order by id and showing its detail.
 * @author Lukas Petr
 */

import { Button, Form, FormControl, InputGroup } from 'react-bootstrap';
import { api } from '../../api';
import { MessageContext } from '../../context/MessageContext';
import { useNavigate } from 'react-router-dom';
import { useContext, useState } from 'react';

/**
 * Component for searching order by id and showing its detail.
 */
export default function ShowOrderWithId() {
  const navigate = useNavigate();
  const {setMessage} = useContext(MessageContext);
  const [orderId, setOrderId] = useState('');

  function handleOrderIdChange(e) {
    const value = e.target.value;
    if (/^[0-9]*$/.test(value)) setOrderId(value);
  }
  function showOrderWithId(e) {
    e.preventDefault();
    api.get(`/order/${Number(orderId)}`)
      .then(() => {
        // success order exists - redirect
        navigate(`/order/${Number(orderId)}`)
      })
      .catch(() => {
        // error - order does no exist
        setMessage({variant: 'danger', text: 'Error: order with given id does not exist!'})
      })
  }

  return (
    <Form onSubmit={showOrderWithId}>
      <InputGroup>
        <FormControl type='text'
                    placeholder='Show order with No.'
                    value={orderId}
                    onChange={handleOrderIdChange}/>
        <Button variant='outline-success'
                type='submit'
                disabled={orderId === ''}>
          Show order
        </Button>
      </InputGroup>
    </Form>
  );
}