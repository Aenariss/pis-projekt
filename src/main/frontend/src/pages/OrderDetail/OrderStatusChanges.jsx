/**
 * Component for showing changes in order status.
 * @author Lukas Petr
 */

import { Table } from 'react-bootstrap';
import { orderStateToString } from '../../utils/orders';
import { useContext } from 'react';
import { AuthContext } from '../../context/AuthContext';

/**
 * Component for showing changes in order status.
 * @param props.modifications List of changes done to order status.
 */
export default function OrderStatusChanges({
  modifications
}) {
  // Check if user who sees the detail is not admin,
  // If it is then show firstname, surname and email of the author of the change.
  const {user} = useContext(AuthContext);
  const isAdmin = user?.role === 'admin';

  if (modifications.length <= 0) return null;
  return (
    <div className='mt-3'>
      <h3>History of changes:</h3>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Time of change</th>
            <th>Changed to status</th>
            {isAdmin && (
              <th>Author of change</th>
            )}
          </tr>
        </thead>
        <tbody>
          {modifications.map((mod) => (
            <tr key={mod?.modificationDate}>
              <td>{mod?.modificationDate.toLocaleString()}</td>
              <td>{orderStateToString(mod?.toStatus)}</td>
              {isAdmin && (
                <td>{mod?.firstname} {mod?.surname} ({mod?.email})</td>
              )}
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
