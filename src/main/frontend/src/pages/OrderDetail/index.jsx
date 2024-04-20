/**
 * Page for showing details about order.
 * @author Lukas Petr
 */
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../../api';
import { orderStateToString } from '../../utils/orders';
import OrderedItems from './OrderedItems';
import { OrderInformation } from './OrderInformation';
import { Spinner } from 'react-bootstrap';

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
          <div className='my-3'>
            <h3>
              Status: {orderStateToString(order.status)}
            </h3>
          </div>
          <OrderInformation order={order} />
          <OrderedItems items={order.orderItems} />
        </>
      )}
    </>
  );

}