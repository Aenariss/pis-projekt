/**
 * User form for showing and editing user informations.
 * @author Lukas Petr <xpetrl06>
 */

import {Accordion, Button, Form, Row, Stack} from 'react-bootstrap';
import ChangePassword from './ChangePassword';
import {useMemo, useState} from 'react';
import Address from './Adress';
import Password from './Password';
import Email from './Email';
import Name from './Name';
import Phone from './Phone';
import { AuthContext } from '../../context/AuthContext';
import { useContext } from 'react';

/**
 * User profile page component.
 * @param props Component props.
 * @param props.title Title of the form.
 * @param props.type Type of the form, one of 'register', 'profile', 'edit'.
 * Register is for registering new users, profile is for showing profile of logged user,
 * edit is for admin to edit info about another users.
 * @param props.title Title of the form.
 * @param props.defaultValues Values which will be shown by default.
 * @param {Function} props.onSubmit Function which will be called with filled data on form submit.
 * @param {number} props.userId For edit type, id of user which is being edited.
 * @returns {JSX.Element} - UserForm component
 * @constructor UserForm
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
  userId,
}) {
  // Check if admin is not editing himself
  // in edit page to block editing his email
  const {user} = useContext(AuthContext);
  const editingSelf = useMemo(() => (
    user?.email === defaultValues?.email
  ), [defaultValues, user]);
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
  const [invalidEmail, setInvalidEmail] = useState(type === 'register' ? true : false);
  const [invalidPassword, setInvalidPassword] = useState(type === 'register' ? true : false);

  const somethingChanged = useMemo(() => (
       defaultValues.firstname !== firstname
    || defaultValues.surname !== surname
    || defaultValues.email !== email
    || defaultValues.phone !== phone
    || defaultValues.state !== state
    || defaultValues.town !== town
    || defaultValues.street !== street
    || defaultValues.streetNumber !== streetNumber
    || defaultValues.postCode !== postCode
  ), [defaultValues, firstname, surname, email, phone, state, town, street, streetNumber, postCode]);
  const disableSubmit = (
       invalidEmail
    || (type === 'register' && invalidPassword)
    // if not register form disable also button if nothing changed
    || (type !== 'register' && !somethingChanged)
  );

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

  return (
    <>
      <Form className='w-50' onSubmit={handleSubmit}>
        <h2>{title}</h2>
        <Accordion defaultActiveKey={['0']} alwaysOpen>
          <Accordion.Item eventKey='0'>
            <Accordion.Header>
              <h3>Basic information</h3>
            </Accordion.Header>
            <Accordion.Body>
              <Stack gap={4}>
                <Row>
                  <Email email={email}
                         onChange={setEmail}
                         invalid={invalidEmail}
                         onInvalid={setInvalidEmail}
                         disabled={type === 'profile' || editingSelf}/>
                </Row>
                {type === 'register' && (
                  <Row>
                    <Password password1={password1}
                              password2={password2}
                              onPassword1Change={setPassword1}
                              onPassword2Change={setPassword2}
                              onInvalidPassword={setInvalidPassword}
                              passwordTitle='Password:'
                              repeatPasswordTitle='Repeat password:'/>
                  </Row>
                )}
                <Row>
                  <Name firstname={firstname}
                        surname={surname}
                        onFirstnameChange={setFirstname}
                        onSurnameChange={setSurname}/>
                </Row>
                <Row>
                  <Phone phone={phone}
                         onChange={setPhone} />
                </Row>
                {type !== 'register' && (
                  <ChangePassword userId={userId}/>
                )}
              </Stack>
            </Accordion.Body>
          </Accordion.Item>
          <Accordion.Item eventKey='1'>
            <Accordion.Header>
              <h3>Address</h3>
            </Accordion.Header>
            <Accordion.Body>
              <Stack gap={4}>
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
              </Stack>
            </Accordion.Body>
          </Accordion.Item>
        </Accordion>
        <Row className='p-3'>
          <Button type='submit' disabled={disableSubmit}>
            {type !== 'register'
              ? 'Save'
              : 'Register'
            }
          </Button>
        </Row>
      </Form>
    </>
  );
}
