/**
 * Page, where employee can manage orders.
 * @author Martin Balaz
 * @author Lukas Petr
 */

import { Spinner } from 'react-bootstrap';
import OrdersTable from '../../components/OrdersTable';
import { useEffect, useState } from 'react';
import { api } from '../../api';

/**
 * Page, where employee can manage orders.
 * @component
 */
export default function OrdersManager() {
  const [orders, setOrders] = useState(null);
  useEffect(() => {
    api.get('/order/all')
      .then((response) =>{
        let orders = response.data;
        orders = orders.map(o => (
          {...o, creationDate: new Date(o.creationDate)}
        ));
        // Sort by the newest to oldest
        orders.sort((o1, o2) => (o2.creationDate - o1.creationDate))
        setOrders(orders);
      })
  }, [])
  let content = null;
  if (orders === null) {
    content = (<Spinner animation='border' />);
  } else if (orders.length === 0) {
    content = (
      <div className='display-5 text-center text-muted mt-5'>
        No orders to show
      </div>
    );
  } else {
    content = (<OrdersTable orders={orders} type='employee'/>);
  }
  return (
    <>
      <h2 className='my-3'>Orders Manager</h2>
      {content}
    </>
  );
}