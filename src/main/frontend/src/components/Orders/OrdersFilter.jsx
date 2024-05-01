/**
 * Component for filtering orders by dates and state.
 */
import { Stack } from 'react-bootstrap';
import OrderStatusFilter from './OrderStatusFilter';
import DatePicker from './DatePicker';

/**
 * Component for filtering orders by dates and state.
 * @param props Component props.
 * @param props.status Current status or '' when not set.
 * @param props.onStatusChange Handler called with new status.
 * @param props.fromDate Date from which to show orders or null.
 * @param props.onFromDateChange Handler call with new from date.
 * @param props.toDate Date to which to show orders or null.
 * @param props.onToDateChange Handler call with new to date.
 * @returns {JSX.Element} - OrdersFilter component
 * @constructor OrdersFilter
 */
export default function OrdersFilter({
  status,
  onStatusChange,
  fromDate,
  onFromDateChange,
  toDate,
  onToDateChange,
}) {
  return (
    <Stack direction='horizontal' gap={3} className='mt-3'>
      <DatePicker label='From date:'
                  date={fromDate}
                  onChange={(date) => {
                    date?.setHours(0);
                    onFromDateChange(date);
                  }} />
      <DatePicker label='To date:'
                  date={toDate}
                  onChange={(date) => {
                    date?.setHours(24);
                    onToDateChange(date);
                  }} />
      <OrderStatusFilter status={status}
                         onChange={onStatusChange}/>
    </Stack>
  );
}