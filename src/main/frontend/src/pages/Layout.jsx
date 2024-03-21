import {Link, Outlet} from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

export default function Layout() {
  return (
    <>
      <Navbar>
        <Container>
          <Navbar.Brand as={Link} to="/">BookShop</Navbar.Brand>
          <Nav>
            <Nav.Link as={Link} to="/category-manager">Categories</Nav.Link>
          </Nav>
        </Container>
      </Navbar>

      <Container>
        <Outlet />
      </Container>
    </>
  );
}