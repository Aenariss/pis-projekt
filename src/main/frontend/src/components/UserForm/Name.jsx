/**
 * Form for filling name and surname.
 * @author Lukas Petr
 */

import { Col, Form } from 'react-bootstrap';

/** Basic regex for checking name - cannot contain numbers and white characters. */
const REGEX_BASIC_NAME = /^[^\d\s]*$/;

/**
 * Form component for filling name and surname.
 * @param props Component props.
 * @param {string} props.firstname Firstname to show.
 * @param {string} props.surname Surname to show.
 * @param {Function} props.onFirstnameChange Called when firstname changges with the new value.
 * @param {Function} props.onSurnameChange Called when surname changges with the new value.
 * @returns {JSX.Element} - Name component
 * @constructor Name
 * @component
 */
export default function Name({
  firstname,
  surname,
  onFirstnameChange,
  onSurnameChange
}) {
  /** Returns true if the name is 'correct'.*/
  function checkName(name) {
    return REGEX_BASIC_NAME.test(name);
  }
  /** Updates name if the name is 'correct'. */
  function handleFirstnameChange(name) {
    if (checkName(name)) onFirstnameChange(name);
  }
  function handleSurnameChange(name) {
    if (checkName(name)) onSurnameChange(name);
  }
  return (
    <>
      <Form.Group as={Col}>
        <Form.Label htmlFor='firstname'>First name:</Form.Label>
        <Form.Control id='firstname'
                      type='text'
                      value={firstname}
                      maxLength={20}
                      onChange={(e) => handleFirstnameChange(e.target.value)}/>
      </Form.Group>
      <Form.Group as={Col}>
        <Form.Label htmlFor='surname'>Last name:</Form.Label>
        <Form.Control id='surname'
                      type='text'
                      value={surname}
                      maxLength={20}
                      onChange={(e) => handleSurnameChange(e.target.value)}/>
      </Form.Group>
    </>
  );
}