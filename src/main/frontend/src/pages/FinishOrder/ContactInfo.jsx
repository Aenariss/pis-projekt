/**
 * Component for filling contact information for order.
 * @author Lukas Petr
 */

import { useState } from 'react';
import { Button, Form, Row, Stack } from 'react-bootstrap';
import Email from '../../components/UserForm/Email';
import Name from '../../components/UserForm/Name';
import Phone from '../../components/UserForm/Phone';

/**
 * Component for filling contact information for order.
 * @param props.userInfo Information about the user.
 * @param {Function} props.onSubmit Handler which will be called when user fills
 * the information, it will receive the user information.
 */
export default function ContactInfo({
  userInfo,
  onSubmit,
}) {
  const [firstname, setFirstname] = useState(userInfo.firstname);
  const [surname, setSurname] = useState(userInfo.surname);
  const [email, setEmail] = useState(userInfo.email);
  const [invalidEmail, setInvalidEmail] = useState(userInfo.email === '');
  const [phone, setPhone] = useState(userInfo.phone);

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({firstname, surname, email, phone});
  }

  return (
    <>
      <h3>Step 1: Contact information</h3>
      <Form className='w-50' onSubmit={handleSubmit}>
        <Stack gap={4}>
          <Row>
            <Email email={email}
                    onChange={setEmail}
                    invalid={invalidEmail}
                    onInvalid={setInvalidEmail}/>
          </Row>
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
          <Button type='submit'
                  disabled={invalidEmail}>
            Continue
          </Button>
        </Stack>
      </Form>
    </>
  );
}