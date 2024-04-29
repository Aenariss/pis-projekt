/**
 * Language Manager page, where admin can manage languages.
 * @author Martin Balaz
 */

import { useState, useEffect } from 'react';
import { Button, Navbar, Container, Nav } from "react-bootstrap";
import LanguageList from './LanguageListing';
import LanguageForm from "./LanguageForm";
import {api} from "../../api";

/**
 * Component for the Language Manager page.
 * @returns {JSX.Element} - Language Manager page
 * @constructor LanguageManager
 */
export default function LanguageManager() {
    const [showModal, setShowModal] = useState(false);
    const [languages, setLanguages] = useState([]);
    const [languageToEdit, setLanguageToEdit] = useState(null);

    /**
     * Function for getting all languages using the REST API.
     */
    function getLanguages() {
        // Fetches list of languages from REST API.
        api.get('/language')
            .then(response => {
                response.data.sort((a, b) => a.language.localeCompare(b.language)); // Languages are in sorted order
                setLanguages(response.data);
            })
            .catch(error => {
                alert(error.response.data); // Display error message
            });
    }

    /**
     * Function for opening pop-up for editing language and setting the language to edit.
     * @param language - language to edit
     */
    function editLanguage(language) {
        setLanguageToEdit(language);
        setShowModal(true);
    }

    /**
     * Function for deleting language using the REST API.
     * @param id - id of the language to delete
     */
    function deleteLanguage(id) {
        // Delete language using the REST API and update the list of languages
        api.delete(`/language/${id}`)
            .then(response => {
                if (response.status === 200) {
                    getLanguages(); // Update table of languages
                }
            })
            .catch(error => {
                alert(error.response.data) // Display error message
            });
    }

    /**
     * On page load, get all languages.
     */
    useEffect(() => {
        getLanguages();
    }, []);

    return (
        <div>
            <Navbar bg="light" variant="light">
                <Container>
                    <Navbar.Brand>Language Manager</Navbar.Brand>
                    <Nav className="justify-content-end" style={{ width: "100%" }}>
                        <Button variant="success" onClick={ () => {
                            setLanguageToEdit(null);
                            setShowModal(true);
                        }}>Add Language</Button>
                    </Nav>
                </Container>
            </Navbar>
            <br/>
            <LanguageList languages={languages} deleteLanguage={deleteLanguage} editLanguage={editLanguage} />
            <LanguageForm showModal={showModal} setShowModal={setShowModal} languageToEdit={languageToEdit} getLanguages={getLanguages} languages={languages} />
        </div>
    );
}