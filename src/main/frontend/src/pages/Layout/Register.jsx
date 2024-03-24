/**
 * Register form (modal window).
 * @author Lukas Petr
 */

import {Alert, Button, Form, Modal} from 'react-bootstrap';
import {useContext, useState} from "react";
import {api} from "../../api";
import {AuthContext} from "../../context/AuthContext";
import {isEmailValid, isPasswordValid} from "./utils";

/**
 * Component for user registration.
 * @param {Function} onHide - Handler for hiding modal window.
 * @component
 */
export default function Register({onHide}) {
  // Info from form
  const [email, setEmail] = useState('');
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  const [firstname, setFirstname] = useState('');
  const [surname, setSurname] = useState('');
  const [phone, setPhone] = useState('');
  // Error from server
  const [errorMessage, setErrorMessage] = useState(null);
  // Checking info validity
  const [invalidEmail, setInvalidEmail] = useState(true);
  const [invalidPassword1, setInvalidPassword1] = useState(true);
  // Disabling submitting
  const disableSubmit = invalidEmail || invalidPassword1 || password1 !== password2;

  const {login} = useContext(AuthContext);

  function handleEmailChange(e) {
    const newEmail = e.target.value;
    setEmail(newEmail);
    setInvalidEmail(! isEmailValid(newEmail));
  }

  function handlePassword1Change(e) {
    const newPassword1 = e.target.value;
    setPassword1(newPassword1);
    setInvalidPassword1(! isPasswordValid(newPassword1));
  }

  /**
   * Tries to register the user.
   * If the registration is successful hides the windows, else show error message.
   */
  function handleRegister(e) {
    e.preventDefault();
    const info = {
      email,
      firstname,
      surname,
      phone,
      password: password1
    }
    api.post('/register', info)
      .then(response => {
        if (response.status === 200) {
          // Successfully registered, logging in the user
          login(
            {email, password: password1},
            // success - hiding the registration modal window.
            () => onHide(),
            // failure - we should not get here
            (msg) => setErrorMessage(`Unable to log in: ${msg}`)
          );
        }
        else {
          // Registration was unsuccessful
          setErrorMessage(response.data);
        }
      })
      .catch(error => {
        setErrorMessage(error.response.data);
      })
  }

  return (
    <Modal show onHide={onHide} >
      <Modal.Header closeButton>
        <Modal.Title>
          Create an account
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleRegister}>
        <Modal.Body>
          {errorMessage && (<Alert variant="danger">{errorMessage}</Alert>)}
          <Form.Group controlId="register-email">
            <Form.Label>Email:</Form.Label>
            <Form.Control type="email" placeholder="example@google.com" value={email}
                          required onChange={handleEmailChange}
                          isInvalid={email !== '' && invalidEmail} isValid={!invalidEmail}/>
            <Form.Control.Feedback type="invalid">Email must be in form 'x@y.z'!</Form.Control.Feedback>
          </Form.Group>
          <Form.Group controlId="register-password1">
            <Form.Label>Password:</Form.Label>
            <Form.Control type="password" required value={password1}
                          onChange={handlePassword1Change}
                          isInvalid={password1 !== '' && invalidPassword1} isValid={!invalidPassword1}/>
            <Form.Control.Feedback type="invalid">Password must contain at least 3 symbols.</Form.Control.Feedback>
          </Form.Group>
          <Form.Group controlId="register-password2">
            <Form.Label>Repeat password:</Form.Label>
            <Form.Control type="password" required value={password2}
                          onChange={(e) => setPassword2(e.target.value)}
                          isInvalid={password1 !== password2} isValid={!invalidPassword1 && password1 === password2}/>
            <Form.Control.Feedback type="invalid">Passwords do not match.</Form.Control.Feedback>
          </Form.Group>
          <Form.Group controlId="register-firstname">
            <Form.Label>Firstname:</Form.Label>
            <Form.Control type="text" value={firstname} onChange={(e) => setFirstname(e.target.value)}
                          maxLength={20}/>
          </Form.Group>
          <Form.Group controlId="register-surname">
            <Form.Label>Surname:</Form.Label>
            <Form.Control type="text" value={surname} onChange={(e) => setSurname(e.target.value)}
                          maxLength={20}/>
          </Form.Group>
          <Form.Group controlId="register-phone">
            <Form.Label>Phone number:</Form.Label>
            <Form.Control type="tel" value={phone} placeholder="+420123456789" onChange={(e) => setPhone(e.target.value)}
                          maxLength={20}/>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>Close</Button>
          <Button type="submit" disabled={disableSubmit}>
            Register
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}