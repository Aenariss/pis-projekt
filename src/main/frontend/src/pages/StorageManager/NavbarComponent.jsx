/**
 * Component for the navbar in the Storage Manager page
 * @author Martin Balaz
 */

import React, {useContext, useState} from 'react';
import { Navbar, Nav, Form, FormControl, Button } from 'react-bootstrap';
import Container from "react-bootstrap/Container";
import {Link, useNavigate, useSearchParams} from "react-router-dom";
import BookForm from "./BookForm";
import {AuthContext} from "../../context/AuthContext";

/**
 * Component for the navbar in the Storage Manager page
 * @param books - list of books
 * @param getBooks - function to get books
 * @param bookToEdit - book to edit (sent to the BookForm)
 * @param setBookToEdit - function to set book to edit
 * @returns {Element} - Storage Manager navbar
 * @constructor - StorageNavbarComponent
 */
export default function StorageNavbarComponent({books, getBooks, bookToEdit, setBookToEdit}) {
    const {user} = useContext(AuthContext);
    const isAdmin = user?.role === 'admin';

    const [input, setInput] = useState('');
    const navigate = useNavigate();
    const [showModal, setShowModal] = useState(false); // For popup window

    /**
     * Function for handling the search
     * @param event
     */
    const handleSearch = (event) => {
        event.preventDefault();
        navigate({pathname: '/storage-manager', search: `?${new URLSearchParams({query: input})}`}); // Add query to URL
    };

    // TODO maybe add checkbox for filtering books with discount

    return (
        <Navbar bg="light" expand="lg">
            <Container>
                <Navbar.Brand as={Link} to="/storage-manager" onClick={() => setInput('')}>Book Manager</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav>
                        <Form className="d-flex" onSubmit={handleSearch}>
                            <FormControl type="text" placeholder="Search by book name" className="mr-sm-2 long-search-bar" value={input} onChange={(e) => setInput(e.target.value)} />
                            <Button variant="outline-success" type="submit">Search</Button>
                        </Form>
                    </Nav>
                </Navbar.Collapse>
                <Navbar.Toggle aria-controls="basic-navbar-nav-2" />
                {isAdmin &&
                <Navbar.Collapse id="basic-navbar-nav-2" className="justify-content-end">
                    <Navbar.Text>
                        <Button variant="success" onClick={() => {
                            setBookToEdit(null);
                            setShowModal(true);
                        }}>Add Book</Button>
                    </Navbar.Text>
                </Navbar.Collapse>}
                <BookForm showModal={showModal} setShowModal={setShowModal} bookToEdit={bookToEdit} getBooks={getBooks} books={books} setBookToEdit={setBookToEdit} />
            </Container>
        </Navbar>
    );
}