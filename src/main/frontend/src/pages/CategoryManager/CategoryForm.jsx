/**
 * Pop-up form for adding/editing a category.
 * @author Martin Balaz
 */

import {useState, useEffect} from 'react';
import {api} from '../../api';
import {Button, Modal, Form} from "react-bootstrap";

/**
 * Form for adding/editing a category.
 * @param showModal - boolean value for showing the modal
 * @param setShowModal - function for setting the modal
 * @param categoryToEdit - category to edit
 * @param getCategories - function for fetching the categories
 * @param categories - list of categories
 * @returns {JSX.Element} - form for adding/editing a category
 * @constructor - CategoryForm
 */
export default function CategoryForm({showModal, setShowModal, categoryToEdit, getCategories, categories}) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    /**
     * Function for setting the form values when editing a category.
     */
    useEffect(() => {
        if (categoryToEdit) {
            setName(categoryToEdit.name);
            setDescription(categoryToEdit.description);
        }
    }, [categoryToEdit]);

    /**
     * Function for handling the form submit using the REST API.
     * @param e - event of the form
     */
    function handleSubmit(e) {
        e.preventDefault();
        const category = {name, description};

        // Check if category name already exists or is too short
        if (categories.some(cat => cat.name.toLowerCase() === name.toLowerCase() && (!categoryToEdit || cat.id !== categoryToEdit.id))) {
            setErrorMessage('Category with this name already exists.');
            return;
        } else if (name.length < 2) {
            setErrorMessage('Category name must be at least 2 characters long.');
            return;
        }

        if (categoryToEdit) {
            // Calls REST API for updating category.
            api.put(`/category/${categoryToEdit.id}`, category)
                .then(response => {
                    if (response.status === 200) {
                        resetForm();
                        getCategories(); // Update the table of categories
                    }
                })
                .catch(error => console.log(error));
        } else {
            // Calls REST API for adding category.
            api.post('/category', category)
                .then(response => {
                    if (response.status === 200) {
                        resetForm();
                        getCategories(); // Update the table of categories
                    }
                })
                .catch(error => console.log(error));
        }
    }

    /**
     * Function for resetting the form values and hiding the modal.
     */
    function resetForm() {
        setDescription('');
        setName('');
        setShowModal(false);
        setErrorMessage(''); // Reset the error message
    }

    return (
        <Modal show={showModal} onHide={resetForm}>
            <Modal.Header closeButton>
                <Modal.Title>{categoryToEdit ? 'Edit Category' : 'Add new category'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Category name</Form.Label>
                        <Form.Control type="text" value={name} onChange={(e) => setName(e.target.value)} isInvalid={!!errorMessage} required />
                        <Form.Control.Feedback type="invalid">{errorMessage}</Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group
                        className="mb-3"
                        controlId="exampleForm.ControlTextarea1"
                    >
                        <Form.Label>Description</Form.Label>
                        <Form.Control as="textarea" rows={3} type="text" value={description} onChange={(e) => setDescription(e.target.value)} required />
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        {categoryToEdit ? 'Update category' : 'Add category'}
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
}