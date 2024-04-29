/**
 * Website layout (header).
 * @author Lukas Petr (xpetrl06)
 * @author Martin Balaz
 */
import {Link, Outlet, useLocation, useNavigate} from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import UserInfo from "./UserInfo";
import {Button, Col} from "react-bootstrap";
import {Cart} from "react-bootstrap-icons";
import {useContext} from 'react';
import {AuthContext} from '../../context/AuthContext';
import Search from "./Search";
import Message from './Message';
import CartInfo from './CartInfo';

/**
 * Website layout.
 * @component
 */
export default function Layout() {
  const navigate = useNavigate();
  const {user} = useContext(AuthContext);
  const location = useLocation();

  let employeeNavBar = null;
  if (user?.role === 'admin' || user?.role === 'employee') {
    employeeNavBar = (
      <Navbar>
        <Container>
          <Nav>
            {user.role === 'admin' && (
              <>
                <Nav.Link as={Link} to="/category-manager"
                          active={location.pathname === "/category-manager"}>Categories</Nav.Link>
                <Nav.Link as={Link} to="/language-manager"
                            active={location.pathname === "/language-manager"}>Languages</Nav.Link>
                <Nav.Link as={Link} to="/employees-manager"
                          active={location.pathname === "/employees-manager"}>Employees</Nav.Link>
                <Nav.Link as={Link} to="/orders-manager"
                          active={location.pathname === "/orders-manager"}>Orders</Nav.Link>
                <Nav.Link as={Link} to="/storage-manager"
                          active={location.pathname === "/storage-manager"}>Storage</Nav.Link>
                <Nav.Link as={Link} to="/overview"
                          active={location.pathname === "/overview"}>Overview</Nav.Link>
              </>
            )}
            {user.role === 'employee' && (
              <>
                <Nav.Link as={Link} to="/orders-manager"
                          active={location.pathname === "/orders-manager"}>Orders</Nav.Link>
                <Nav.Link as={Link} to="/storage-manager"
                          active={location.pathname === "/storage-manager"}>Storage</Nav.Link>
              </>
            )}
          </Nav>
        </Container>
      </Navbar>
    );
  }


  return (
    <>
      <Navbar bg="primary" data-bs-theme="dark" className="text-light">
        <Container>
          <Col md={2}>
            <Navbar.Brand as={Link} to="/">BookShop</Navbar.Brand>
          </Col>
          <Col md={7}>
            <Search/>
          </Col>
          <Col md={1}>
            <CartInfo />
          </Col>
          <Col md={2}>
            <UserInfo/>
          </Col>
        </Container>
      </Navbar>
      {employeeNavBar}
      <Container className="pt-2">
        <Message />
        <Outlet/>
      </Container>
    </>
  );
}
