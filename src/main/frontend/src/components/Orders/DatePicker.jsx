/**
 * Component for selecting date.
 * @author Lukas Petr
 */
import { Form } from 'react-bootstrap';

/**
 * Component for selecting date.
 * @param props Component props.
 * @param props.label Label of the picker.
 * @param props.date Date to show - null means no selected date.
 * @param props.onChange Callback function called on change with new date.
 * @returns {JSX.Element} - DatePicker component
 * @constructor DatePicker
 */
export default function DatePicker({
  label,
  date,
  onChange,
}) {
  return (
    <Form.Group className='d-flex flex-row align-items-center'
                controlId={`date-picker-${label}`}>
      <Form.Label style={{width: '130px'}} className='my-0'>
        {label}
      </Form.Label>
      <Form.Control type='date'
                    value={date !== null ? getDateAsValue(date) : ''}
                    onChange={(e) => onChange(e.target.valueAsDate)}
                    min={new Date('2020-01-01').toISOString().split("T")[0]}
                    max={new Date().toISOString().split("T")[0]}/>
    </Form.Group>
  );
}

/**
 * Converts date to format excepted by input date.
 */
function getDateAsValue(date) {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
}