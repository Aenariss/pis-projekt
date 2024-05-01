/**
 * Password form component.
 * @author Lukas Petr
 */

import { Col, Form } from "react-bootstrap";
import { isPasswordValid } from "./utils";
import { useMemo } from "react";

/**
 * Password form component.
 * @param props Component props.
 * @param {string} props.password1 Password in the first field.
 * @param {string} props.password2 Password retyped by user in second field.
 * @param {Function} props.onPassword1Change Function called on password 1 change.
 * @param {Function} props.onPassword2Change Function called on password 2 change.
 * @param {Function} props.onInvalidPassword Function called for updating is passwords are invalid.
 * @param {String} props.passwordTitle Label for the first password field.
 * @param {String} props.repeatPasswordTitle Label for the second password field.
 * @returns {JSX.Element} - Password component
 * @constructor Password
 */
export default function Password({
  password1,
  password2,
  onPassword1Change,
  onPassword2Change,
  onInvalidPassword,
  passwordTitle,
  repeatPasswordTitle,
}) {
  const invalidPassword1 = useMemo(() => {
    return ! isPasswordValid(password1);
  }, [password1]);

  function handlePassword1Change(e) {
    const newPassword1 = e.target.value;
    const isNewPasswordValid = isPasswordValid(newPassword1);
    onPassword1Change(newPassword1);
    onInvalidPassword(! (isNewPasswordValid && newPassword1 === password2 ));
  }
  function handlePassword2Change(e) {
    const newPassword2 = e.target.value;
    onPassword2Change(newPassword2);
    onInvalidPassword(!(! invalidPassword1 && password1 === newPassword2));
  }
  return (
    <>
      <Form.Group controlId="password1" as={Col}>
        <Form.Label>{passwordTitle}</Form.Label>
        <Form.Control type="password"
                      required
                      value={password1}
                      onChange={handlePassword1Change}
                      isInvalid={invalidPassword1}
                      isValid={!invalidPassword1}/>
        <Form.Control.Feedback type="invalid">Password must contain at least 3 symbols.</Form.Control.Feedback>
        <Form.Control.Feedback type="valid">Password is valid</Form.Control.Feedback>
      </Form.Group>
      <Form.Group controlId="password2" as={Col}>
        <Form.Label>{repeatPasswordTitle}</Form.Label>
        <Form.Control type="password"
                      required
                      value={password2}
                      onChange={handlePassword2Change}
                      isInvalid={password1 !== password2}
                      isValid={!invalidPassword1 && password1 === password2}/>
        <Form.Control.Feedback type="invalid">Passwords do not match.</Form.Control.Feedback>
        <Form.Control.Feedback type="valid">Passwords do match.</Form.Control.Feedback>
      </Form.Group>
    </>
  );
}