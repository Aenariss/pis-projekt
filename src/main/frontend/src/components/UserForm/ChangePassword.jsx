/**
 * Modal window for changing passwords.
 * @author Lukas Petr.
 */
import { useContext, useState } from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import Password from './Password';
import { api } from '../../api';
import { MessageContext } from '../../context/MessageContext';

/**
 * Button with modal window for changing passwords.
 * @param {String} props.userId If changing password of another user - id of the user.
 * @component
 */
export default function ChangePassword({userId}) {
  const [showModal, setShowModal] = useState(false);
  const {setMessage} = useContext(MessageContext);

  function handleSubmit(password) {
    setShowModal(false);
    const url = userId
      ? `/user/password/${userId}`
      : `/user/password`;
    api.put(url, {password})
      .then(() => {
        setMessage({text: 'Password has been successfully changed', variant: 'success'});
      })
      .catch(() => {
        setMessage({text: 'Error: password could not be changed!', variant: 'danger'});
      })
  }
  return (
    <>
      <Button type='button'
              size='sm'
              variant='outline-primary'
              onClick={() => setShowModal(true)}>
        Change password
      </Button>
      { showModal && (
        <ChangePasswordModal show={showModal}
                             onClose={() => setShowModal(false)}
                             onSubmit={handleSubmit}/>
        )
      }
    </>
  );
}

/**
 * Modal window for changing password.
 * @param {boolean} props.show If the modal should be shown.
 * @param {Function} props.onClose User want to close the modal.
 * @param {Function} props.onSubmit User send the form, called with password..
 * @returns 
 */
function ChangePasswordModal({
  show,
  onClose,
  onSubmit,
}) {
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  const [invalidPasswords, setInvalidPasswords] = useState();
  
  function handleSubmit(e) {
    e.preventDefault();
    e.stopPropagation();
    onSubmit(password1);
  }

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Password change</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Password password1={password1}
                    password2={password2}
                    onPassword1Change={setPassword1}
                    onPassword2Change={setPassword2}
                    onInvalidPassword={setInvalidPasswords}
                    passwordTitle='New password:'
                    repeatPasswordTitle='Retype the new password:'/>
        </Modal.Body>
        <Modal.Footer>
          <Button type='submit'
                  disabled={invalidPasswords}>
            Change the password
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}