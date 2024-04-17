/**
 * User order page - showing history of user orders.
 * @author Lukas Petr <xpetrl06>
 */

import { useEffect, useState } from 'react';
import { api } from '../../api';
import OrdersTable from '../../components/OrdersTable';
/**
 * User order page component.
 * @component
 */
export default function UserOrdersPage() {
  const [orders, setOrders] = useState([]);
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
  return (
    <>
      <h2>History of my orders</h2>
      {orders.length === 0
        ? (
          <div className='display-5 text-center text-muted mt-5'>
            Your cart is empty
          </div>
        ) : (
          <OrdersTable orders={orders}/>
      )}
    </>
  );
}
