/**
 * Component for Navbar of Employee Manager.
 * @author Martin Balaz
 */

import React from 'react';
import { Navbar, Nav, Form, FormControl, Button } from 'react-bootstrap';
import '../../styles.css';
import {api} from "../../api";

/**
 * Component for Navbar of Employee Manager
 * @param activeLink - active link (which page is currently displayed - Employees or Users)
 * @param setActiveLink - function for setting the active link
 * @param setSearchQuery - function for setting the search query
 * @returns {Element} - Navbar of Employee Manager
 * @constructor - NavbarComponent
 */
export default function NavbarComponent({ activeLink, setActiveLink, setSearchQuery }) {

    /**
     * Function for handling the search using the REST API.
     * Call the appropriate endpoint based on the active link
     * and update the search query state.
     */
    const handleSearchClick = () => {
        // Get the value of the search bar
        const searchInput = document.querySelector('.long-search-bar').value;

        // Store the search query in the local storage
        localStorage.setItem('searchQuery', searchInput); // Handle refreshing the page

        // Determine which endpoint to call based on the activeLink
        if (searchInput !== '') {
            let endpoint;
            if (activeLink === '#employees') {
                endpoint = searchInput.includes('@') ? `/users/getEmployeesByEmail/${searchInput}` : `/users/getEmployeesByName/${searchInput}`;
            } else if (activeLink === '#users'){
                endpoint = searchInput.includes('@') ? `/users/getUsersByEmail/${searchInput}` : `/users/getUsersByName/${searchInput}`;
            }

            // Call the endpoint and update the searchQuery state
            api.get(endpoint)
                .then(response => {
                    if (response.status === 200) {
                        setSearchQuery(response.data);
                        localStorage.setItem('users', JSON.stringify(response.data)); // Handle refreshing the page
                    }
                });
        } else {
            // If the search bar is empty, get all employees
            if (activeLink === '#employees') {
                api.get('/users/getEmployees')
                    .then(response => {
                        if (response.status === 200) {
                            setSearchQuery(response.data);
                            localStorage.setItem('users', JSON.stringify(response.data)); // Handle refreshing the page
                        }
                    });
            // If the search bar is empty, hide all users
            } else if (activeLink === '#users') {
                setSearchQuery([]);
                localStorage.removeItem('users'); // Handle refreshing the page
            }
        }
    };

    return (
        <Navbar bg="light" expand="lg">
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                    <Nav.Link
                        href="#employees"
                        onClick={() => {
                            setActiveLink('#employees');
                            setSearchQuery('');  // Reset the search query
                            localStorage.setItem('activeLink', '#employees'); // Handle refreshing the page
                            localStorage.removeItem('users'); // Handle refreshing the page
                            localStorage.removeItem('searchQuery'); // Handle refreshing the page
                            document.querySelector('.long-search-bar').value = ''; // Reset the search bar
                        }}
                        active={activeLink === '#employees'}
                    >
                        Employee Manager
                    </Nav.Link>
                    <Nav.Link
                        href="#users"
                        onClick={() => {
                            setActiveLink('#users');
                            setSearchQuery('');  // Reset the search query
                            localStorage.setItem('activeLink', '#users'); // Handle refreshing the page
                            localStorage.removeItem('users'); // Handle refreshing the page
                            localStorage.removeItem('searchQuery'); // Handle refreshing the page
                            document.querySelector('.long-search-bar').value = ''; // Reset the search bar
                            document.querySelector('.long-search-bar').focus(); // Focus on search bar
                        }}
                        active={activeLink === '#users'}
                    >
                        User Manager
                    </Nav.Link>
                </Nav>
                <Form className="d-flex">
                    <FormControl type="text" placeholder="Search by e-mail/surname" className="mr-sm-2 long-search-bar"
                                 defaultValue={localStorage.getItem('searchQuery') || ''} // Handle refreshing the page
                                 // Handle the search on Enter key press
                                 onKeyDown={event => {
                                     if (event.key === 'Enter') {
                                         event.preventDefault(); // Prevent form submission
                                         handleSearchClick();
                                     }
                                 }}
                    />
                    <Button variant="outline-success" onClick={handleSearchClick}>Search</Button>
                </Form>
            </Navbar.Collapse>
        </Navbar>
    );
}