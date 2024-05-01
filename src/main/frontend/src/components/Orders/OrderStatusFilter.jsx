/**
 * Component for filtering orders by their status.
 * @author Lukas Petr
 */
import { Form } from 'react-bootstrap';
import { orderStateToString } from '../../utils/orders';

/**
 * Component for filtering orders by their status.
 * @param props Component props.
 * @param props.status Current status.
 * @param props.onChange Handler called with new status.
 * @returns {JSX.Element} - OrderStatusFilter component
 * @constructor OrderStatusFilter
 */
export default function OrderStatusFilter({
  status,
  onChange,
}) {
  return (
    <Form.Group className='d-flex flex-row align-items-center' controlId='order-state'>
      <Form.Label style={{width: '170px'}} className='my-0'>
        Order state:
      </Form.Label>
      <Form.Select value={status}
                  onChange={(e) => onChange(e.target.value)}>
        <option value=''>all</option>
        <option value='IN_PROGRESS'>{orderStateToString('IN_PROGRESS')}</option>
        <option value='CONFIRMED'>{orderStateToString('CONFIRMED')}</option>
        <option value='PACKED'>{orderStateToString('PACKED')}</option>
        <option value='SHIPPED'>{orderStateToString('SHIPPED')}</option>
        <option value='DELIVERED'>{orderStateToString('DELIVERED')}</option>
        <option value='CANCELED'>{orderStateToString('CANCELED')}</option>
        <option value='RETURNED'>{orderStateToString('RETURNED')}</option>
      </Form.Select>
    </Form.Group>
  );
}