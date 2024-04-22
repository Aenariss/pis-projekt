/**
 * Table for showing orders.
 * @author Lukas Petr
 */
import { Button, Stack, Table } from 'react-bootstrap';
import { orderStateToString } from '../../utils/orders';
import { useNavigate } from 'react-router-dom';
import Paginator from '../Paginator';
import { useMemo, useState } from 'react';
import SorterButtons from '../SorterButton';

const AMOUNT_OF_ORDERS_ON_PAGE = 15;

/**
 * Table component for showing orders.
 * @param props.orders List of orders to be shown.
 * @param props.type Type 'user' / 'employee' - for user is shown
 * total price, for employee is shown email of the person who created the order.
 * @param props.defaultFromOldest True to sort by default orders from oldest
 * otherwise there will be sort from newest to oldest.
 */
export default function OrdersTable({
  orders,
  type='user',
  defaultFromOldest=false
}) {
  const [fromOldest, setFromOldest] = useState(defaultFromOldest)
  const [currentPage, setCurrentPage] = useState(1);
  const amountOfPages = Math.ceil(orders.length / AMOUNT_OF_ORDERS_ON_PAGE);
  
  const sortedOrders = useMemo(() => {
    if (fromOldest) {
      return orders.toSorted((o1, o2) => (o1.creationDate - o2.creationDate))
    } else {
      return orders.toSorted((o1, o2) => (o2.creationDate - o1.creationDate))
    }
  }, [fromOldest, orders])
  
  const ordersOnPage = sortedOrders.slice((currentPage-1) * AMOUNT_OF_ORDERS_ON_PAGE,
                                        currentPage * AMOUNT_OF_ORDERS_ON_PAGE);
  const navigate = useNavigate();
  return (
    <>
      <Table striped bordered>
        <thead>
          <tr>
            <th>No.</th>
            <th>
              <Stack direction='horizontal' gap={2}>
                Date of order
                <SorterButtons onSortChange={setFromOldest}/>
              </Stack>
            </th>
            {type === 'employee' && (
              <th>User email</th>
            )}
            <th>State</th>
            {type === 'user' && (
              <th>Total price</th>
            )}
            <th></th>
          </tr>
        </thead>
        <tbody>
          {ordersOnPage.map((order) => (
            <tr key={order.id}>
              <td>{order.id.toString().padStart(6,'0')}</td>
              <td>{new Date(order.creationDate).toLocaleString()}</td>
              {type === 'employee' && (
                <td>{order?.email}</td>
              )}
              <td>{orderStateToString(order.status)}</td>
              {type === 'user' && (
                <td>{order.totalPrice.toFixed(2)} $</td>
              )}
              <td>
                <Button size='sm'
                        onClick={() => navigate(`/order/${order.id}`)}>
                  Detail
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Paginator pages={amountOfPages}
                 activePage={currentPage}
                 setActivePage={setCurrentPage}/>
    </>
  );
}