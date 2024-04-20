/**
 * Component for showing basic information about order.
 * @author Lukas Petr.
 */

import { Table } from 'react-bootstrap';

/**
 * Component for showing basic information about order.
 * @param props.order Order.
 * @component
 */
export function OrderInformation({order}) {
  return (
    <div className='mt-3'>
      <h3>Order information:</h3>
      <Table style={{fontSize: '18px'}} striped>
        <tbody>
          <tr>
            <th>Date of order:</th>
            <td> {new Date(order.creationDate).toLocaleString()}</td>
          </tr>
          <tr>
            <th>Name:</th>
            <td>
              {order?.orderUserInfo?.firstname} {order?.orderUserInfo?.surname}
            </td>
          </tr>
          <tr>
            <th>Email:</th>
            <td>
              {order?.orderUserInfo?.email}
            </td>
          </tr>
          <tr>
            <th>Phone:</th>
            <td>
              {order?.orderUserInfo?.phone}
            </td>
          </tr>
          <tr>
            <th>Billing address:</th>
            <td>
              {addressToString(order.userAddress)}
            </td>
          </tr>
          <tr>
            <th>Delivery address:</th>
            <td>
              {addressToString(order.deliveryAddress)}
            </td>
          </tr>
          <tr>
            <th>Total price:</th>
            <td>
              {order.totalPrice.toFixed(2)} $
            </td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
}

/**
 * Converts address to string.
 * @param a Address.
 */
function addressToString(a) {
  return `${a.street} ${a.streetNumber}, ${a.town} ${a.postCode}, ${a.state}`;
}