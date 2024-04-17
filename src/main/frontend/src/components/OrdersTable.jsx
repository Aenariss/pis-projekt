/**
 * Table for showing orders.
 * @author Lukas Petr
 */
import { Button, Table } from 'react-bootstrap';
import { orderStateToString } from '../utils/orders';

/**
 * Table component for showing orders.
 * @param props.orders List of orders to be shown.
 */
export default function OrdersTable({
  orders,
}) {
  return (
    <Table striped>
      <thead>
        <tr>
          <th>Date of order</th>
          <th>State</th>
          <th>Total price</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {orders.map((order) => (
          <tr key={order.id}>
            <td>{new Date(order.creationDate).toLocaleString()}</td>
            <td>{orderStateToString(order.status)}</td>
            <td>{order.totalPrice.toFixed(2)} $</td>
            <td><Button size='sm'>Detail</Button></td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
} 