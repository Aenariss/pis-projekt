/**
 * Modal window for changing passwords.
 * @author Lukas Petr.
 */
import { useContext, useState } from 'react';
import {Alert, Button, Col, Form, Modal} from 'react-bootstrap';
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
  const [error, setError] = useState(null);
  const {setMessage} = useContext(MessageContext);

  function handleSubmit(old_password, password) {
    const url = userId
    ? `/user/password/${userId}`
    : `/user/password`;
    api.put(url, {password, old_password})
    .then(() => {
      setShowModal(false);
      setError(null);
      setMessage({text: 'Password has been successfully changed', variant: 'success'});
    })
    .catch((error) => {
        setError({text: `Error: ${error.response.data}`, variant: 'danger'});
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
        <ChangePasswordModal requireOriginalPassword={userId === undefined}
                             show={showModal}
                             onClose={() => setShowModal(false)}
                             onSubmit={handleSubmit}
                             error={error}/>
        )
      }
    </>
  );
}

/**
 * Modal window for changing password.
 * @param {boolean} props.show If the modal should be shown.
 * @param {Function} props.onClose User want to close the modal.
 * @param {Function} props.onSubmit User send the form, called with password.
 * @param {Function} props.requireOriginalPassword True to show field for filling the original password.
 * @param {Function} props.error Error message to show to the user.
 * @returns 
 */
function ChangePasswordModal({
  show,
  onClose,
  onSubmit,
  requireOriginalPassword,
  error,
}) {
  const [originalPassword, setOriginalPassword] = useState('');
  const [password1, setPassword1] = useState('');
  const [password2, setPassword2] = useState('');
  const [invalidPasswords, setInvalidPasswords] = useState();
  
  function handleSubmit(e) {
    e.preventDefault();
    e.stopPropagation();
    onSubmit(originalPassword, password1);
  }

  return (
    <Modal show={show} onHide={onClose}>
      <Modal.Header closeButton>
        <Modal.Title>Password change</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {error && <Alert variant={error.variant}>{error.text}</Alert>}
          { requireOriginalPassword && (
            <Form.Group controlId="password-original" as={Col}>
              <Form.Label>Original password</Form.Label>
              <Form.Control type="password"
                            required
                            value={originalPassword}
                            onChange={(e) => setOriginalPassword(e.target.value)}
                            isInvalid={originalPassword === ''} />
              <Form.Control.Feedback type="invalid">Fill your original password</Form.Control.Feedback>
            </Form.Group>
          )}
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
                  disabled={invalidPasswords && (requireOriginalPassword && originalPassword === '')}>
            Change the password
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}