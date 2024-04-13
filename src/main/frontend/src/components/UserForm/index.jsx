/**
 * User form for showing and editing user informations.
 * @author Lukas Petr <xpetrl06>
 */

import {Button, Col, Form, Row, Stack} from 'react-bootstrap';
import ChangePassword from './ChangePassword';
import {useState} from 'react';
import { isEmailValid, isPasswordValid } from './utils';
import Address from './Adress';

/**
 @param props.title Title of the form.
 * User profile page component.
 * @param props.type Type of the form, one of 'register', 'profile'.
 * @param props.title Title of the form.
 * @param props.defaultValues Values which will be shown by default.
 * @param {Function} props.onSubmit Function which will be called with filled data on form submit.
 * @component
 */
export default function UserForm({
  type='register',
  title,
  defaultValues={
    firstname: '',
    surname: '',
    email: '',
    phone: '',
    state: '',
    town: '',
    street: '',
    streetNumber: '',
    postCode: '',
  },
  onSubmit,
}) {
  // User info
  const [firstname, setFirstname] = useState(defaultValues.firstname);
  const [surname, setSurname] = useState(defaultValues.surname);
  const [email, setEmail] = useState(defaultValues.email);
  const [phone, setPhone] = useState(defaultValues.phone);
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  // Address
  const [state, setState] = useState(defaultValues.state);
  const [town, setTown] = useState(defaultValues.town);
  const [street, setStreet] = useState(defaultValues.street);
  const [streetNumber, setStreetNumber] = useState(defaultValues.streetNumber);
  const [postCode, setPostCode] = useState(defaultValues.postCode);
  // Checking info validity
  const [invalidEmail, setInvalidEmail] = useState(true);
  const [invalidPassword1, setInvalidPassword1] = useState(true);

  const disableSubmit = invalidEmail || invalidPassword1 || password1 !== password2;

  function handleSubmit(e) {
    e.preventDefault();
    const data = {
      firstname,
      password: password1,
      surname,
      email,
      phone,
      state,
      town,
      street,
      streetNumber,
      postCode,
    }
    onSubmit(data);
  }


  function handleEmailChange(newEmail) {
    setEmail(newEmail);
    setInvalidEmail(! isEmailValid(newEmail));
  }

  function handlePassword1Change(e) {
    const newPassword1 = e.target.value;
    setPassword1(newPassword1);
    setInvalidPassword1(! isPasswordValid(newPassword1));
  }

  return (
    <>
      <ChangePassword ></ChangePassword>
      <Form className='w-50' onSubmit={handleSubmit}>
        <Stack gap={4}>
          <h2>{title}</h2>
          <h3>Basic information</h3>
          <Row>
            <Form.Group as={Col}>
                <Form.Label htmlFor='email'>Email address:</Form.Label>
                <Form.Control id='email'
                              type='text'
                              value={email}
                              required
                              placeholder="example@google.com"
                              isInvalid={email === '' && invalidEmail} isValid={!invalidEmail}
                              onChange={e => handleEmailChange(e.target.value)}/>
                <Form.Control.Feedback type="invalid">Email must be in form 'x@y.z'!</Form.Control.Feedback>
            </Form.Group>
          </Row>
          {type === 'register' && (
            <Row>
              <Form.Group controlId="register-password1" as={Col}>
                <Form.Label>Password:</Form.Label>
                <Form.Control type="password" required value={password1}
                              onChange={handlePassword1Change}
                              isInvalid={password1 === '' && invalidPassword1} isValid={!invalidPassword1}/>
                <Form.Control.Feedback type="invalid">Password must contain at least 3 symbols.</Form.Control.Feedback>
              </Form.Group>
              <Form.Group controlId="register-password2" as={Col}>
                <Form.Label>Repeat password:</Form.Label>
                <Form.Control type="password" required value={password2}
                              onChange={(e) => setPassword2(e.target.value)}
                              isInvalid={password1 !== password2} isValid={!invalidPassword1 && password1 === password2}/>
                <Form.Control.Feedback type="invalid">Passwords do not match.</Form.Control.Feedback>
              </Form.Group>
            </Row>
          )}
          <Row>
            <Form.Group as={Col}>
              <Form.Label htmlFor='firstname'>First name:</Form.Label>
              <Form.Control id='firstname'
                            type='text'
                            value={firstname}
                            maxLength={20}
                            onChange={(e) => setFirstname(e.target.value)}/>
            </Form.Group>
            <Form.Group as={Col}>
              <Form.Label htmlFor='surname'>Last name:</Form.Label>
              <Form.Control id='surname'
                            type='text'
                            value={surname}
                            maxLength={20}
                            onChange={(e) => setSurname(e.target.value)}/>
            </Form.Group>
          </Row>
          <Row>
            <Form.Group as={Col}>
              <Form.Label htmlFor='phone'>Phone number:</Form.Label>
              <Form.Control id='phone'
                            type='text'
                            value={phone}
                            placeholder="+420123456789"
                            onChange={e => setPhone(e.target.value)} />
            </Form.Group>
          </Row>

          {type === 'profile' && (
            <Button type='button' size='sm' variant='outline-primary'>Change password</Button>
          )}
          <h3>Address</h3>
          <Address state={state}
                   town={town}
                   postcode={postCode}
                   street={street}
                   streetNumber={streetNumber}
                   onStateChange={setState}
                   onTownChange={setTown}
                   onPostcodeChange={setPostCode}
                   onStreetChange={setStreet}
                   onStreetNumberChange={setStreetNumber}/>
          <Button type='submit' disabled={disableSubmit}>Register</Button>
        </Stack>
      </Form>
    </>
  );
}
