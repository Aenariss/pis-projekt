/**
 * User order page - showing history of user orders.
 * @author Lukas Petr <xpetrl06>
 */

import { useEffect, useState } from 'react';
import { api } from '../../api';
import OrdersTable from '../../components/Orders/OrdersTable';
import { Spinner } from 'react-bootstrap';
import OrdersFilter from '../../components/Orders/OrdersFilter';
/**
 * User order page component.
 * @component
 */
export default function UserOrdersPage() {
  const [orders, setOrders] = useState(null);
  const [status, setStatus] = useState('');
  const [fromDate, setFromDate] = useState(null);
  const [toDate, setToDate] = useState(null);
  useEffect(() => {
    api.get('order')
      .then((response) =>{
        let orders = response.data;
        orders = orders.map(o => (
          {...o, creationDate: new Date(o.creationDate)}
        ));
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
    let ordersToShow = orders;
    // Filtering
    if (status !== '') {
      ordersToShow = ordersToShow.filter(o => o.status === status);
    }
    if (fromDate) {
      ordersToShow = ordersToShow.filter(o => o.creationDate >= fromDate);
    }
    if (toDate) {
      ordersToShow = ordersToShow.filter(o => o.creationDate <= toDate);
    }
    content = (<OrdersTable orders={ordersToShow}/>);
  }
  return (
    <>
      <h2 className='my-3'>History of my orders</h2>
      <OrdersFilter status={status}
                    onStatusChange={setStatus}
                    fromDate={fromDate}
                    onFromDateChange={setFromDate}
                    toDate={toDate}
                    onToDateChange={setToDate}/>
      <br/>
      {content}
    </>
  );
}
