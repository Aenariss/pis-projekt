/**
 * Page, where employee can manage orders.
 * @author Lukas Petr
 */

import { Container, Navbar, Spinner, } from 'react-bootstrap';
import OrdersTable from '../../components/Orders/OrdersTable';
import { useEffect, useState } from 'react';
import { api } from '../../api';
import { useSearchParams } from 'react-router-dom';
import ShowOrderWithId from './ShowOrderWithId';
import SearchByEmail from './SearchByEmail';
import OrdersFilter from '../../components/Orders/OrdersFilter';

/** For all orders by default showing orders from today. */
const DEFAULT_FROM_DATE = new Date();
DEFAULT_FROM_DATE.setHours(0,0,0,0);

/** For all orders by default showing orders to midnight of today. */
const DEFAULT_TO_DATE = new Date();
DEFAULT_TO_DATE.setHours(23,59,59,999);

/** For all order by default showing only ordered orders - not processed yet. */
const DEFAULT_STATE = 'IN_PROGRESS';

/**
 * Page, where employee can manage orders.
 * @component
 */
export default function OrdersManager() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [orders, setOrders] = useState(null);
  const [status, setStatus] = useState(DEFAULT_STATE);
  const [fromDate, setFromDate] = useState(DEFAULT_FROM_DATE);
  const [toDate, setToDate] = useState(DEFAULT_TO_DATE);

  /** Preprocess orders and saves them to state. */
  function preprocessOrders(orders) {
    orders = orders.map(o => (
      {...o, creationDate: new Date(o.creationDate)}
    ));
    setOrders(orders);
  }

  useEffect(() => {
    const email = searchParams.get('email');
    if (email) {
      // Mail is set - get orders by mail
      api.get(`/order/byEmail/${email}`)
        .then((response) =>{
          // success
          preprocessOrders(response.data);
          // Do not set any filter by default when searching by email
          setStatus('');
          setFromDate(null);
          setToDate(null);
        })
    } else {
      api.get('/order/all')
        .then((response) =>{
          preprocessOrders(response.data);
          // showing all orders - set default filters which user can change
          setStatus(DEFAULT_STATE);
          setFromDate(DEFAULT_FROM_DATE);
          setToDate(DEFAULT_TO_DATE);
        })
    }
  }, [searchParams]);

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
    content = (<OrdersTable orders={ordersToShow} type='employee' defaultFromOldest={true}/>);
  }

  return (
    <div>
      <Navbar bg="light" variant="light">
        <Container>
          <Navbar.Brand>Orders Manager</Navbar.Brand>
          <ShowOrderWithId />
        </Container>
      </Navbar>
      <div className='my-3 d-flex'>
        <SearchByEmail />
      </div>
      <OrdersFilter status={status}
                    onStatusChange={setStatus}
                    fromDate={fromDate}
                    onFromDateChange={setFromDate}
                    toDate={toDate}
                    onToDateChange={setToDate}/>
      <br/>
      {content}
    </div>
  );
}