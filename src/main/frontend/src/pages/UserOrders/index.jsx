/**
 * User order page - showing history of user orders.
 * @author Lukas Petr <xpetrl06>
 */

import { useEffect, useState } from 'react';
import { api } from '../../api';
import OrdersTable from '../../components/OrdersTable';
import { Spinner } from 'react-bootstrap';
/**
 * User order page component.
 * @component
 */
export default function UserOrdersPage() {
  const [orders, setOrders] = useState(null);
  useEffect(() => {
    api.get('order')
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
        You do not have any orders
      </div>
    );
  } else {
    content = (<OrdersTable orders={orders}/>);
  }
  return (
    <>
      <h2 className='my-3'>History of my orders</h2>
      {content}
    </>
  );
}
