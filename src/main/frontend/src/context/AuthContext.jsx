/**
 * Context for containing info about user.
 * @author Lukas Petr
 */
import {createContext, useCallback, useState} from 'react';
import {api} from "../api";
import { useNavigate } from 'react-router-dom';

export const AuthContext = createContext(null);

/**
 * Component for providing AuthContext - info about logged in user.
 * @component
 * @param children Subelements.
 */
export default function AuthProvider({ children }) {
  const navigate = useNavigate();
  // user = {token, email, role} if logged in else user = null
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

  /**
   * Tries to log in user.
   * @param credentials User credentials.
   * @param {string} credentials.email Email of user.
   * @param {string} credentials.password Password of user.
   * @param {function} onSuccess Function called if the logging in was successful.
   * @param {function(string)} onError Function called if the logging in was unsuccessful, calls the function
   *   with data from server response.
   */
  function login({email, password}, onSuccess, onError) {
    api.post("/login", {email, password})
      .then(response => {
        if (response.status === 200) {
          // Successful log in
          // Getting JWT token and role from response.
          const [token, role] = response.data.split(';');
          // Saving user info to context state and local storage.
          const user = {email, token, role};
          setUser(user);
          localStorage.setItem('user', JSON.stringify(user));
          // Calling user callback.
          onSuccess();
        } else {
          onError(response.data);
        }
      })
      .catch(error => {
        onError(error.response.data);
      });
  }

  /**
   * Tries to renew JWT token, must be called before the JWT token expires.
   * @param errorHandler Called when the renewal was unsuccessful.
   */
  const renewToken = useCallback((errorHandler) => {
    api.post('/renewToken', {email: user.email})
      .then(response => {
        if (response.status === 200) {
          // Getting JWT token and role from response.
          const [newToken] = response.data.split(';');
          const userRenewed = {...user, token: newToken};
          setUser(userRenewed);
          localStorage.setItem('user', JSON.stringify(userRenewed));
        }
      })
      // Catching errors
      .catch(() => {
        errorHandler();
      });
  },[user]);

  /**
   * Sets that user is logged out.
   */
  function logout() {
    navigate('/');
    setUser(null);
    localStorage.removeItem('user')
  }

  return (
    <AuthContext.Provider value={{user, login, logout, renewToken}}>
      {children}
    </AuthContext.Provider>
  );
}