/**
 * Form for filling name and surname.
 * @author Lukas Petr
 */

import { Col, Form } from 'react-bootstrap';

/**
 * Form component for filling name and surname.
 * @param {string} props.firstname Firstname to show.
 * @param {string} props.surname Surname to show.
 * @param {Function} props.onFirstnameChange Called when firstname changges with the new value.
 * @param {Function} props.onSurnameChange Called when surname changges with the new value.
 * @component
 */
export default function Name({
  firstname,
  surname,
  onFirstnameChange,
  onSurnameChange
}) {
  return (
    <>
      <Form.Group as={Col}>
        <Form.Label htmlFor='firstname'>First name:</Form.Label>
        <Form.Control id='firstname'
                      type='text'
                      value={firstname}
                      maxLength={20}
                      onChange={(e) => onFirstnameChange(e.target.value)}/>
      </Form.Group>
      <Form.Group as={Col}>
        <Form.Label htmlFor='surname'>Last name:</Form.Label>
        <Form.Control id='surname'
                      type='text'
                      value={surname}
                      maxLength={20}
                      onChange={(e) => onSurnameChange(e.target.value)}/>
      </Form.Group>
    </>
  );
}