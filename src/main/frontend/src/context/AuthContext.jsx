/**
 * Context for containing info about user.
 * @author Lukas Petr
 */
import {createContext, useState} from 'react';
import {api} from "../api";

export const AuthContext = createContext(null);

/**
 * Component for providing AuthContext - info about logged in user.
 * @component
 * @param children Subelements.
 */
export default function AuthProvider({ children }) {
  // user = {token, email} if logged in else user = null
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
          // Getting JWT token from response.
          const token = response.data;
          // Saving user info to context state and local storage.
          const user = {email, token};
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
   * Sets that user is logged out.
   */
  function logout() {
    setUser(null);
    localStorage.removeItem('user')
  }

  return (
    <AuthContext.Provider value={{user, login, logout}}>
      {children}
    </AuthContext.Provider>
  );
}