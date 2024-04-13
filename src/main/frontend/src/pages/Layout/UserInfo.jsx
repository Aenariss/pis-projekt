/**
 * User information component, handles log in, registering, showing that user is logged in.
 * @author Lukas Petr
 */

import {PersonCircle} from "react-bootstrap-icons";
import {Button, Dropdown, Stack} from "react-bootstrap";
import React, {useContext, useState} from "react";
import Login from "./Login";
import {AuthContext} from "../../context/AuthContext";
import {useNavigate} from "react-router-dom";

/**
 * DropDown toggle component for showing possible account actions for user.
 * @component
 */
const UserDropDownToggle = React.forwardRef(({ children, onClick, disabled }, ref) => {
  return (
    <Button ref={ref} onClick={onClick} disabled={disabled}>
      <PersonCircle size="40" />
    </Button>
  );
});

/**
 * Navigation component showing info about user - handling login, registering.
 * @component
 */
export default function UserInfo() {
  const navigate = useNavigate();
  // For showing login/register modals, possible values: null, login, register
  const [show, setShow] = useState(null);
  const {user, logout} = useContext(AuthContext);

  /** Handles when login/register windows want to be hidden. */
  function hideHandle() {
    setShow(null);
  }

  let info = null;
  if (user) {
    // User is logged in
    /* Part before @, probably should change to firstname and password but need API for this. */
    const username = user.email.split('@')[0];
    info = (
      <>
        <span className="">{username}</span>
        <span>
          <a className="link-underline-opacity-0 link-underline-opacity-100-hover link-light"
             role="button" onClick={() => logout()}>
            Log out
          </a>
        </span>
      </>
    );
  } else {
    // User is not logged in, showing register, login buttons
    info = (
      <>
        <span>
          <a className="link-underline-opacity-0 link-underline-opacity-100-hover link-light"
             role="button" onClick={() => setShow('login')}>
            Log in
          </a>
        </span>
        <span>
          <a className="link-underline-opacity-0 link-underline-opacity-100-hover link-light"
             role="button" onClick={() => navigate('/register')}>
            Register
          </a>
        </span>
      </>
    );
  }

  return (
    <span>
      <Stack direction="horizontal" gap="2">
        <Dropdown data-bs-theme="light">
          <Dropdown.Toggle as={UserDropDownToggle} disabled={! user}/>
          <Dropdown.Menu>
            <Dropdown.Item onClick={() => navigate('/profile')}>Profile</Dropdown.Item>
            <Dropdown.Item onClick={() => navigate('/my-orders')}>My orders</Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
        <Stack direction="vertical">
          { info }
        </Stack>
      </Stack>
      { show === 'login' && <Login onHide={hideHandle}/> }
    </span>
  );
}