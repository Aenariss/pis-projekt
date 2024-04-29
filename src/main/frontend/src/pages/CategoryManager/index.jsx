/**
 * Page, where admin can manage categories.
 * @author Martin Balaz
 */

import {useState, useEffect} from 'react';
import {api} from '../../api';
import {Button, Modal, Form} from "react-bootstrap";
import Navbar from "react-bootstrap/Navbar";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import CategoryList from './CategoryListing';
import CategoryForm from "./CategoryForm";

/**
 * Component for the Category Manager page.
 * @returns {JSX.Element} - Category Manager page
 * @constructor CategoryManager
 */
export default function CategoryManager() {
    const [showModal, setShowModal] = useState(false);
    const [categories, setCategories] = useState([]);
    const [categoryToEdit, setCategoryToEdit] = useState(null);

    /**
     * Function for getting all categories using the REST API.
     */
    function getCategories() {
        // Fetches list of categories from REST API.
        api.get('/category')
            .then(response => {
                response.data.sort((a, b) => a.name.localeCompare(b.name)); // Categories are in sorted order
                setCategories(response.data);
            })
            .catch(error => console.log(error));
    }

    /**
     * Function for opening pop-up for editing category.
     * @param category - category to edit
     */
    function editCategory(category) {
        setCategoryToEdit(category);
        setShowModal(true);
    }

    /**
     * Function for deleting category using the REST API.
     * @param id - id of the category to delete
     */
    function deleteCategory(id) {
        api.delete(`/category/${id}`)
            .then(response => {
                if (response.status === 200) {
                    getCategories(); // Update table of categories
                }
            })
            .catch(error => {
                alert(error.response.data) // Display error message
            });
    }

    /**
     * On page load, get all categories.
     */
    useEffect(() => {
        getCategories();
    }, []);

    return (
        <div>
            <Navbar bg="light" variant="light">
                <Container>
                    <Navbar.Brand>Category Manager</Navbar.Brand>
                    <Nav className="justify-content-end" style={{ width: "100%" }}>
                        <Button variant="success" onClick={ () => {
                            setCategoryToEdit(null); // Reset the categoryToEdit state
                            setShowModal(true);
                        }}>Add Category</Button>
                    </Nav>
                </Container>
            </Navbar>
            <br/>
            <CategoryList categories={categories} deleteCategory={deleteCategory} editCategory={editCategory} />
            <CategoryForm showModal={showModal} setShowModal={setShowModal} categoryToEdit={categoryToEdit} getCategories={getCategories} categories={categories} />
        </div>
    );
}
