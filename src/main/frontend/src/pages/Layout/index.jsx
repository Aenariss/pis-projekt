/**
 * Website layout (header).
 * @author Lukas Petr (xpetrl06)
 */
import {Link, Outlet} from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import UserInfo from "./UserInfo";
import {Button, Col, Form, InputGroup} from "react-bootstrap";
import {Cart} from "react-bootstrap-icons";
import {useContext} from 'react';
import {AuthContext} from '../../context/AuthContext';

/**
 * Website layout.
 * @component
 */
export default function Layout() {
  const {user} = useContext(AuthContext);
  let employeeNavBar = null;
  if (user?.role === 'admin' || user?.role === 'employee') {
    employeeNavBar = (
      <Navbar>
        <Container>
          <Nav>
            {user.role === 'admin' && (
              <>
                <Nav.Link as={Link} to="/category-manager">Categories</Nav.Link>
                <Nav.Link as={Link} to="/employees-manager">Employees</Nav.Link>
                <Nav.Link as={Link} to="/orders-manager">Orders</Nav.Link>
                <Nav.Link as={Link} to="/storage-manager">Storage</Nav.Link>
                <Nav.Link as={Link} to="/book-add">Add Book</Nav.Link>
                <Nav.Link as={Link} to="/overview">Overview</Nav.Link>
              </>
            )}
            {user.role === 'employee' && (
              <>
                <Nav.Link as={Link} to="/orders-manager">Orders</Nav.Link>
                <Nav.Link as={Link} to="/storage-manager">Storage</Nav.Link>
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
            <Form className="mx-5">
              <InputGroup>
                <Form.Control type="text" data-bs-theme="light" placeholder="Search by book name" />
                <Button onClick={() => alert("Not implemented yet")}>Search</Button>
              </InputGroup>
            </Form>
          </Col>
          <Col md={1}>
            <Button onClick={() => alert("Not implemented yet")}>
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