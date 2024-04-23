/**
 * Page for showing details about order.
 * @author Lukas Petr
 */
import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../../api';
import OrderedItems from './OrderedItems';
import { OrderInformation } from './OrderInformation';
import { Alert, Spinner } from 'react-bootstrap';
import OrderStatus from './OrderStatus';

/**
 * Page for showing details about order.
 * @component
 */
export default function OrderDetail() {
  const {orderId} = useParams();
  const [order, setOrder] = useState(null);
  const [error, setError] = useState(false);
  
  useEffect(() => {
    setError(false);
    api.get(`/order/${orderId}`)
      .then(response => {
        setOrder(response.data);
      })
      .catch((error) => {
        setError(true);
      })
  },[orderId]);

  const updateStatus = useCallback((status) => {
    setOrder((order) => ({...order, status}));
  }, []);

  let content = null;
  if (error) {
    content = (
      <Alert variant='danger'>
        Error: Order with given id does not exist.
      </Alert>
    );
  } else if (order !== null ){
    content = (
      <>
        <OrderStatus order={order} updateStatus={updateStatus}/>
        <OrderInformation order={order} />
        <OrderedItems items={order.orderItems} />
      </>
    );
  } else {
    content = (
      <Spinner animation="border" />
    );
  }

  return (
    <>
      <h2 className='mt-4 mb-2'>
        Order No. {orderId.toString().padStart(6, '0')}
      </h2>
      { content }
    </>
  );

}