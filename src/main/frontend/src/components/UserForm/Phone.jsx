/**
 * Form for filling phone number.
 * @author Lukas Petr
 */

import { Col, Form } from "react-bootstrap";

/**
 * Form component for filling phone number.
 * @param {string} props.phone Phone number to show.
 * @param {Function} props.onChange Called with new value of phone number when
 * phone number changes.
 * @component
 */
export default function Phone({
  phone,
  onChange
}) {
  return (
    <Form.Group as={Col}>
      <Form.Label htmlFor='phone'>Phone number:</Form.Label>
      <Form.Control id='phone'
                    type='text'
                    value={phone}
                    placeholder="+420123456789"
                    onChange={e => onChange(e.target.value)} />
    </Form.Group>
  );
}