/**
 * Page for showing details about order.
 * @author Lukas Petr
 */
import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../../api';
import OrderedItems from './OrderedItems';
import { OrderInformation } from './OrderInformation';
import { Spinner } from 'react-bootstrap';
import OrderStatus from './OrderStatus';

/**
 * Page for showing details about order.
 * @component
 */
export default function OrderDetail() {
  const {orderId} = useParams();
  const [order, setOrder] = useState(null);
  
  useEffect(() => {
    api.get(`/order/${orderId}`)
      .then(response => {
        setOrder(response.data);
      })
  },[orderId]);

  const updateStatus = useCallback((status) => {
    setOrder((order) => ({...order, status}));
  }, []);

  if (order === null) return null;

  return (
    <>
      <h2 className='mt-4 mb-2'>
        Order No. {orderId.toString().padStart(6, '0')}
      </h2>
      { order === null
        ? (<Spinner animation="border" />)
        : (
        <>
          <OrderStatus order={order} updateStatus={updateStatus}/>
          <OrderInformation order={order} />
          <OrderedItems items={order.orderItems} />
        </>
      )}
    </>
  );

}