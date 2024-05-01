/**
 * Component for inserting number - used for setting price in filter.
 * @author Lukas Petr
 */
import { Form } from 'react-bootstrap';

/**
 * Component for inserting number - used for setting price in filter.
 * @param props Component props.
 * @param props.value Value of number
 * @param props.onChange Called with new value if it was set to number.
 * @param props.label Label of the input.
 * @param props.isInvalid If the input is invalid.
 * @returns {JSX.Element} - NumberInput component
 * @constructor NumberInput
 */
export default function NumberInput({
  value,
  onChange,
  label,
  isInvalid,
}) {
  // allow only numbers or empty field
  function handleChange(value) {
    if (/^(|[0-9]+|[0-9]+(,|.)[0-9]*)$/.test(value)) {
      onChange(value);
    }
  }
  return (
    <Form.Group className='d-flex align-items-center me-3'>
      <Form.Label className='me-2'>{label}</Form.Label>
      <Form.Control type='text' maxLength={6}
                    value={value}
                    onChange={e => handleChange(e.target.value)}
                    isInvalid={isInvalid}/>
    </Form.Group>
  );
}