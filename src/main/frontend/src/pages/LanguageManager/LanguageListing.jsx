/**
 * Component for displaying a list of languages as buttons with delete buttons next to them.
 * @author Martin Balaz
 */

import { Button, ButtonGroup, Col, Row } from "react-bootstrap";

/**
 * Component for displaying a list of languages as buttons with delete buttons next to them.
 * @param languages - list of languages
 * @param deleteLanguage - function for deleting a language
 * @param editLanguage - function for editing a language
 * @returns {JSX.Element} - grid of languages
 * @constructor LanguageList
 */
export default function LanguageList({languages, deleteLanguage, editLanguage}) {
    return (
        <Row>
            {languages.map((language, index) => (
                <Col md={2} key={language.id}>
                    <ButtonGroup className="mb-3">
                        <Button variant="primary" onClick={() => editLanguage(language)}>{language.language}</Button>
                        <Button variant="danger" onClick={() => deleteLanguage(language.id)}>🗑️</Button>
                    </ButtonGroup>
                </Col>
            ))}
        </Row>
    );
}