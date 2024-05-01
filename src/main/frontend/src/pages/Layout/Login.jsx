/**
 * Login form (modal).
 * @author Lukas Petr
 */
import {Alert, Button, Form, Modal} from 'react-bootstrap';
import {useContext, useState} from "react";
import {AuthContext} from "../../context/AuthContext";
import {isEmailValid, isPasswordValid} from "../../components/UserForm/utils";

/**
 * Component for logging in.
 * @param {Function} onHide - Handler for hiding logging modal window.
 * @returns {JSX.Element} - Login component
 * @constructor Login
 */
export default function Login({onHide}) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const invalidEmail = ! isEmailValid(email);
  const invalidPassword = ! isPasswordValid(password);
  const {login} = useContext(AuthContext);

  /** Handles change of email. */
  function handleEmailChange(e) {
    setEmail(e.target.value);
  }

  /** Handles change of password. */
  function handlePasswordChange(e) {
    setPassword(e.target.value);
  }

  /** Handles logging in. */
  function handleLogin(e) {
    e.preventDefault();
    setErrorMessage(null);
    login(
      { email, password },
      // success -- hiding the modal window
      () => { onHide() },
      // error -- showing the error message received from server
      (msg) =>  { setErrorMessage(msg) },
    );
  }

  return (
    <Modal show onHide={onHide} >
      <Modal.Header closeButton>
        <Modal.Title>
          Log in to your account
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleLogin}>
        <Modal.Body>
          {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
          <Form.Group controlId="login-email">
            <Form.Label>Email:</Form.Label>
            <Form.Control type="email" placeholder="example@google.com" value={email}
                          required onChange={handleEmailChange}
                          isInvalid={email !== '' && invalidEmail}/>
            <Form.Control.Feedback type="invalid">Email must be in form 'x@y.z'!</Form.Control.Feedback>
          </Form.Group>
          <Form.Group controlId="login-password">
            <Form.Label>Password:</Form.Label>
            <Form.Control type="password" required value={password}
                          onChange={handlePasswordChange}
                          isInvalid={password !== '' &&  invalidPassword}/>
            <Form.Control.Feedback type="invalid">Password must have at least 3 characters.</Form.Control.Feedback>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>Close</Button>
          <Button type="submit" disabled={invalidEmail || invalidPassword}>Log in</Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}