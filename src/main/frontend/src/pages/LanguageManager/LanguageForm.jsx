/**
 * Pop-up form for adding/editing a language.
 * @author Martin Balaz
 */

import { useState, useEffect } from 'react';
import { Modal, Form, Button } from 'react-bootstrap';
import {api} from "../../api";

/**
 * Form for adding/editing a language.
 * @param showModal - boolean value for showing the modal
 * @param setShowModal - function for setting the modal
 * @param languageToEdit - language to edit
 * @param getLanguages - function for fetching the languages
 * @param languages - list of languages
 * @returns {JSX.Element} - form for adding/editing a language
 * @constructor - LanguageForm
 */
export default function LanguageForm({showModal, setShowModal, languageToEdit, getLanguages, languages}) {
    const [name, setName] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    /**
     * Function for setting the form values when editing a language.
     */
    useEffect(() => {
        if (languageToEdit) {
            setName(languageToEdit.language);
        }
    }, [languageToEdit]);

    /**
     * Function for handling the form submit using the REST API (adding or editing language).
     * @param e - event of the form
     */
    function handleSubmit(e) {
        e.preventDefault();
        const language = {language: name};

        // Check if language is too short
        if (name.length < 2) {
            setErrorMessage('Language name must be at least 1 characters long.');
            return;
        }

        if (languageToEdit) {
            // Calls REST API for updating language.
            api.put(`/language/${languageToEdit.id}`, language)
                .then(response => {
                    if (response.status === 200) {
                        resetForm();
                        getLanguages(); // Update the table of languages
                    }
                })
                .catch(error => {
                    if (error.response && error.response.status === 409) {
                        setErrorMessage(error.response.data);
                    } else {
                        console.log(error);
                    }
                });
        } else {
            // Calls REST API for adding language.
            api.post('/language', language)
                .then(response => {
                    if (response.status === 200) {
                        resetForm();
                        getLanguages(); // Update the table of languages
                    }
                })
                .catch(error => {
                    if (error.response && error.response.status === 409) {
                        setErrorMessage(error.response.data);
                    } else {
                        console.log(error);
                    }
                });
        }
    }

    /**
     * Function for resetting the form values and hiding the modal.
     */
    function resetForm() {
        setName('');
        setShowModal(false);
        setErrorMessage('');
    }

    return (
        <Modal show={showModal} onHide={resetForm}>
            <Modal.Header closeButton>
                <Modal.Title>{languageToEdit ? 'Edit Language' : 'Add new language'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Language name</Form.Label>
                        <Form.Control type="text" value={name} onChange={(e) => setName(e.target.value)} isInvalid={!!errorMessage} required />
                        <Form.Control.Feedback type="invalid">{errorMessage}</Form.Control.Feedback>
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        {languageToEdit ? 'Update language' : 'Add language'}
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
}