/**
 * Register page
 * @author Lukas Petr
 */
import { useContext, useState } from "react";
import { api } from "../../api";
import UserForm from "../../components/UserForm";
import { AuthContext } from "../../context/AuthContext";
import { Navigate, useNavigate } from "react-router-dom";
import { Alert } from "react-bootstrap";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState('');
  const {login} = useContext(AuthContext);
  const {user} = useContext(AuthContext);

  // If user is logged in then redirect him.
  if (user) {
    return <Navigate to='/' replace/>;
  }

  /**
   * Tries to register the user.
   * If the registration is successful hides the windows, else show error message.
   */
    function handleRegister(data) {
      api.post('/register', data)
        .then(response => {
          if (response.status === 200) {
            // Successfully registered, logging in the user
            login(
              {email: data.email, password: data.password},
              // success - hiding the registration modal window.
              () => navigate('/'),
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
    <>
      {errorMessage && <Alert variant="danger">{errorMessage}</Alert>}
      <UserForm title='Create an account'
                onSubmit={handleRegister}/>
    </>
  );
}
