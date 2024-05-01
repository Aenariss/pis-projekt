/**
 * Component for showing status of order
 * and for employees / admin it enables to change the status.
 * @author Lukas Petr
 */
import { useCallback, useContext } from 'react';
import { AuthContext } from '../../context/AuthContext';
import { orderStateToString } from '../../utils/orders';
import { Dropdown, DropdownButton, InputGroup, SplitButton } from 'react-bootstrap';
import { api } from '../../api';
import { MessageContext } from '../../context/MessageContext';

/**
 * Component for showing status of order
 * and for employees / admin it enables to change the status.
 * @param props Component props.
 * @param props.order Order.
 * @param {Function} onRefresh Callback to handle refreshing the page if something changes.
 * @returns {JSX.Element} - OrderStatus component
 * @constructor OrderStatus
 */
export default function OrderStatus({order, onRefresh}) {
  const {user} = useContext(AuthContext);
  const {setMessage} = useContext(MessageContext);

  const changeStatus = useCallback((status) => {
    api.put('/order/update', { status, id: order.id })
      .then(() => {
        // Refreshing the whole page so the modification table contains current data
        onRefresh();
      })
      .catch(() => {
        setMessage({
          text: 'Error: it was not possible to update the status',
          variant: 'danger',
        });
      })
  }, [order.id, onRefresh, setMessage]);


  if (['admin', 'employee'].includes(user?.role)) {
    // if admin or employee is logged in then he can update the status of the order
    return (
      <div className='my-3'>
        <h3>
          Status: {orderStateToString(order.status)}
          <InputGroup className='px-3 d-inline'>
            <StatusSelector status={order.status} changeStatus={changeStatus}>
              <Dropdown.Item onClick={() => changeStatus('CONFIRMED')}>{orderStateToString('CONFIRMED')}</Dropdown.Item>
              <Dropdown.Item onClick={() => changeStatus('PACKED')}>{orderStateToString('PACKED')}</Dropdown.Item>
              <Dropdown.Item onClick={() => changeStatus('SHIPPED')}>{orderStateToString('SHIPPED')}</Dropdown.Item>
              <Dropdown.Item onClick={() => changeStatus('DELIVERED')}>{orderStateToString('DELIVERED')}</Dropdown.Item>
              <Dropdown.Item onClick={() => changeStatus('CANCELED')}>{orderStateToString('CANCELED')}</Dropdown.Item>
            </StatusSelector>
          </InputGroup>
          <LastChanged modifications={order?.modifications} />
        </h3>
      </div>
    );
  }
  return (
    <div className='my-3'>
      <h3>
        Status: {orderStateToString(order.status)}
        <LastChanged modifications={order?.modifications} />
      </h3>
    </div>
  );
}

/**
 * Component for selecting status.
 * @param props.status Current status.
 * @param props.changeStatus Function for updating status.
 * @component
 */
function StatusSelector({children, status, changeStatus}) {
  const nextStatus = expectedNextStatus(status);
  if (nextStatus !== null) {
    // If there is expected status then show it by default
    // but also enable user to choose from other options
    return (
      <SplitButton title={orderStateToString(nextStatus)}
                   onClick={() => changeStatus(nextStatus)}>
        {children}
      </SplitButton>
    );
  } else {
    return (
      <DropdownButton title='Change status'>
        {children}
      </DropdownButton>
    );
  }
}

/**
 * Returns expected next status based on current status.
 * If the expected next status is not known returns null.
 * @param {string} status Current status.
 */
function expectedNextStatus(status) {
  switch (status) {
    case 'IN_PROGRESS': return 'CONFIRMED';
    case 'CONFIRMED': return 'PACKED';
    case 'PACKED': return 'SHIPPED';
    case 'SHIPPED': return 'DELIVERED';
    default: return null;
  }
}

/**
 * Component for showing when the status was last changed.
 * @param props.modifications List of modifications.
 */
function LastChanged({modifications}) {
  if (modifications?.length > 0) {
    return (
        <span className='text-muted ms-2' style={{fontSize: '15px'}}>
          (Last changed: {modifications[0]?.modificationDate?.toLocaleString()})
        </span>
    );
  }
  return null;
}