/**
 * Form component for entering email.
 * @author Lukas Petr
 */

import { Col, Form } from 'react-bootstrap';
import { isEmailValid } from './utils';

/**
 * Form component for entering email.
 * @param props Component props.
 * @param {string} props.email Email to be shown.
 * @param {Function} props.onChange Called with new email address.
 * @param {boolean} props.invalid Value telling if the email is invalid.
 * @param {Function} prop.onInvalid Called for setting email invalid status.
 * @param {boolean} props.disabled True to disable the input.
 * @returns {JSX.Element} - Email component
 * @constructor Email
 */
export default function Email({
  email,
  onChange,
  invalid,
  onInvalid,
  disabled,
}) {
  function handleEmailChange(newEmail) {
    onChange(newEmail);
    onInvalid(! isEmailValid(newEmail));
  }
  return (
    <Form.Group as={Col}>
      <Form.Label htmlFor='email'>Email address:</Form.Label>
      <Form.Control id='email'
                    type='text'
                    value={email}
                    required
                    placeholder='example@google.com'
                    disabled={disabled}
                    isInvalid={invalid} isValid={!invalid}
                    onChange={e => handleEmailChange(e.target.value)}/>
      <Form.Control.Feedback type='invalid'>Email must be in form 'x@y.z'!</Form.Control.Feedback>
      <Form.Control.Feedback type='valid'>&nbsp;</Form.Control.Feedback>
    </Form.Group>
  );
}