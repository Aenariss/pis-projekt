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
import OrderStatusChanges from './OrderStatusChanges';

/**
 * Page for showing details about order.
 * @component
 */
export default function OrderDetail() {
  const {orderId} = useParams();
  const [order, setOrder] = useState(null);
  const [error, setError] = useState(false);

  const getOrder = useCallback(() => {
    setError(false);
    api.get(`/order/${orderId}`)
      .then(response => {
        const data = response.data;
        // Converting modification dates to js type
        data?.modifications?.forEach((mod) => {
          mod.modificationDate = new Date(mod?.modificationDate);
        });
        // Sort modification date from the latest to oldest
        data?.modifications?.sort((mod1, mod2) => (
          mod2.modificationDate - mod1.modificationDate
        ));
        setOrder(data);
      })
      .catch((error) => {
        setError(true);
      })
  },[orderId]);

  useEffect(() => {
    getOrder()
  },[getOrder]);

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
        <OrderStatus order={order} onRefresh={() => getOrder()}/>
        <OrderInformation order={order} />
        <OrderedItems items={order.orderItems} />
        <OrderStatusChanges modifications={order?.modifications}/>
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