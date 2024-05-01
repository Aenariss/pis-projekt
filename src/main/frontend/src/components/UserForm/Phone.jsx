/**
 * Form for filling phone number.
 * @author Lukas Petr
 */

import { Col, Form } from "react-bootstrap";


/**
 * Form component for filling phone number.
 * @param props Component props.
 * @param {string} props.phone Phone number to show.
 * @param {Function} props.onChange Called with new value of phone number when
 * phone number changes.
 * @returns {JSX.Element} - Phone component
 * @constructor Phone
 */
export default function Phone({
  phone,
  onChange
}) {
  function handlePhoneChange(phone) {
    if (/^[\d\s-.+]*$/.test(phone)) {
      onChange(phone);
    }
  }
  return (
    <Form.Group as={Col}>
      <Form.Label htmlFor='phone'>Phone number:</Form.Label>
      <Form.Control id='phone'
                    type='text'
                    value={phone}
                    placeholder="+420123456789"
                    onChange={e => handlePhoneChange(e.target.value)} />
    </Form.Group>
  );
}