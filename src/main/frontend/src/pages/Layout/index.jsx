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

/**
 * Website layout.
 * @component
 */
export default function Layout() {
  const navigate = useNavigate();
  const {user} = useContext(AuthContext);
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path;
  }

  let employeeNavBar = null;
  if (user?.role === 'admin' || user?.role === 'employee') {
    employeeNavBar = (
      <Navbar>
        <Container>
          <Nav>
            {user.role === 'admin' && (
              <>
                <Nav.Link as={Link} to="/category-manager" className={isActive("/category-manager") ? "active" : ""}>Categories</Nav.Link>
                <Nav.Link as={Link} to="/employees-manager" className={isActive("/employees-manager") ? "active" : ""}>Employees</Nav.Link>
                <Nav.Link as={Link} to="/orders-manager" className={isActive("/orders-manager") ? "active" : ""}>Orders</Nav.Link>
                <Nav.Link as={Link} to="/storage-manager" className={isActive("/storage-manager") ? "active" : ""}>Storage</Nav.Link>
                <Nav.Link as={Link} to="/book-add" className={isActive("/book-add") ? "active" : ""}>Add book</Nav.Link>
                <Nav.Link as={Link} to="/overview" className={isActive("/overview") ? "active" : ""}>Overview</Nav.Link>
              </>
            )}
            {user.role === 'employee' && (
              <>
                <Nav.Link as={Link} to="/orders-manager" className={isActive("/orders-manager") ? "active" : ""}>Orders</Nav.Link>
                <Nav.Link as={Link} to="/storage-manager" className={isActive("/storage-manager") ? "active" : ""}>Storage</Nav.Link>
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
            <Search />
          </Col>
          <Col md={1}>
            <Button onClick={() => navigate('/cart')}>
              <span className="me-2"><Cart size={30}/></span>
              <span>Cart</span>
            </Button>
          </Col>
          <Col md={2}>
            <UserInfo/>
          </Col>
        </Container>
      </Navbar>
      {employeeNavBar}
      <Container className="pt-5">
        <Outlet />
      </Container>
    </>
  );
}
