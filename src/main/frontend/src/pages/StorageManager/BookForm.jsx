/**
 * Pop-up form for adding/editing a book to the storage.
 * @author Martin Balaz
 */

import React, {useState, useEffect} from 'react';
import {api} from '../../api';
import {Button, Modal, Form, Col, Row, Dropdown} from "react-bootstrap";

/**
 * Custom toggle for dropdown for selecting categories
 * from https://react-bootstrap.netlify.app/docs/components/dropdowns
 */
const CustomToggle = React.forwardRef(({ children, onClick }, ref) => (
    <a
        href=""
        ref={ref}
        onClick={(e) => {
            e.preventDefault();
            onClick(e);
        }}
    >
        {children}
        &#x25bc;
    </a>
));

/**
 * Custom menu for dropdown for selecting categories
 * from https://react-bootstrap.netlify.app/docs/components/dropdowns
 */
const CustomMenu = React.forwardRef(
    ({ children, style, className, 'aria-labelledby': labeledBy }, ref) => {
        return (
            <div
                ref={ref}
                style={{...style, maxHeight: '10rem', overflow: 'auto', paddingLeft: '1rem', paddingRight: '1rem'}}
                className={className}
                aria-labelledby={labeledBy}
            >
                <ul className="list-unstyled">
                    {React.Children.toArray(children)}
                </ul>
            </div>
        );
    },
);

/**
 * Form for adding/editing a book to the storage
 * @param showModal - boolean for showing the modal
 * @param setShowModal - function for setting the modal
 * @param bookToEdit - book to edit
 * @param getBooks - function for getting books
 * @param books - list of books
 * @param setBookToEdit - function for setting the book to edit
 * @returns {Element} - form for adding/editing a book
 * @constructor - BookForm
 */
export default function BookForm({showModal, setShowModal, bookToEdit, getBooks, books, setBookToEdit}) {
    const [errorMessage, setErrorMessage] = useState('');
    const [name, setName] = useState('');
    const [author, setAuthor] = useState({firstName: '', lastName: ''});
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [ISBN, setISBN] = useState('');
    const [pages, setPages] = useState('');
    const [image, setImage] = useState('');

    const [allCategories, setAllCategories] = useState([]); // All available categories
    const [selectedCategories, setSelectedCategories] = useState([]); // Selected categories

    const [allLanguages, setLanguages] = useState([]); // All available languages
    const [defaultLanguageId, setDefaultLanguageId] = useState(null); // Default language id
    const [language, setLanguage] = useState(''); // Selected language

    /**
     * Function for getting all categories using the REST API
     */
    function getCategories() {
        api.get('/category')
            .then(response => {
                if (response.status === 200) {
                    response.data.sort((a, b) => a.name.localeCompare(b.name)); // Categories are in sorted order
                    setAllCategories(response.data); // Set the categories for the dropdown menu
                }
            })
            .catch(error => console.log(error));
    }

    /**
     * Function for getting all languages using the REST API
     * Sets the default language to English
     */
    function getLanguages() {
        // Fetches list of languages from REST API.
        api.get('/language')
            .then(response => {
                if (response.status === 200) {
                    response.data.sort((a, b) => a.language.localeCompare(b.language)); // Languages are in sorted order
                    setLanguages(response.data); // Set the languages for the dropdown menu

                    // Find the id of the English language and set it as the default language
                    const englishLanguage = response.data.find(language => language.language === 'English');
                    if (englishLanguage) {
                        setDefaultLanguageId(englishLanguage.id);
                    }
                }
            })
            .catch(error => console.log(error));
    }

    /**
     * Function for adding a book to the storage using the REST API
     * @param book - book to add
     */
    function addBook(book) {
        // Calls REST API for adding book.
        api.post('/productdescription', book)
            .then(response => {
                if (response.status === 200) {
                    resetForm(); // Reset the form
                    getBooks(); // Fetch the updated list of books to update the table
                }
            })
            .catch(error => console.log(error));
    }

    /**
     * Function for editing a book in the storage using the REST API
     * @param book - book to edit
     */
    function editBook(book) {
        // Calls REST API for updating book.
        api.put(`/productdescription/${bookToEdit.id}`, book)
            .then(response => {
                if (response.status === 200) {
                    resetForm();
                    getBooks(); // Fetch the updated list of books to update the table
                }
            })
            .catch(error => console.log(error));
    }

    /**
     * Function for getting all categories and languages when the component mounts
     * If admin got here through edit button, set the form fields to the values of the given book
     */
    useEffect(() => {
        getCategories();
        getLanguages();

        if (bookToEdit) {
            setShowModal(true);
            setName(bookToEdit.name);
            setAuthor({firstName: bookToEdit?.author?.firstName, lastName: bookToEdit?.author?.lastName});
            setDescription(bookToEdit.description);
            setPrice(bookToEdit.price);
            setISBN(bookToEdit.ISBN);
            setPages(bookToEdit.pages);
            setSelectedCategories(bookToEdit.categories);
            setLanguage(bookToEdit?.language?.id);
            setImage(bookToEdit.image);
        } else {
            // If there's no book to edit, set the language to the id of the English language
            setLanguage(defaultLanguageId);
        }
    }, [bookToEdit, defaultLanguageId]);

    /**
     * Function for handling the change of the selected categories
     * @param event - event of the checkbox
     */
    const handleCategoryChange = (event) => {
        const value = parseInt(event.target.value);
        if (event.target.checked) {
            // Add the category to the selected categories if it's checked and not already in the list
            setSelectedCategories([...selectedCategories, allCategories.find(category => category.id === value)]);
        } else {
            // Remove the category from the selected categories if it's unchecked
            setSelectedCategories(selectedCategories.filter(category => category.id !== value));
        }
    };

    /**
     * Function for handling submit of the form
     * @param e - event of the form
     */
    function handleSubmit(e) {
        e.preventDefault();
        const selectedLanguage = allLanguages.find(lang => lang.id === language); // Find the selected language

        /**
         * Book object to be added/edited
         */
        const book = {
            name,
            author: {
                firstName: author.firstName,
                id: bookToEdit ? bookToEdit.author.id : undefined, // Include the id if editing a book
                lastName: author.lastName
            },
            description,
            price: parseFloat(price),
            ISBN,
            pages: parseInt(pages),
            categories: selectedCategories,
            language: selectedLanguage,
            image
        };

        // TODO add more validation for the form fields

        // Check if book with this ISBN already exists
        if (books.some(b => b.ISBN.toLowerCase() === ISBN.toLowerCase() && (!bookToEdit || b.id !== bookToEdit.id))) {
            setErrorMessage('Book with this ISBN already exists.');
            return;
        }

        // Call correct function based on whether the user is editing or adding a book
        if (bookToEdit) editBook(book)
        else addBook(book)
    }

    /**
     * Function for resetting the form
     * Resets all the form fields and closes the modal
     */
    function resetForm() {
        setDescription('');
        setName('');
        setAuthor({firstName: '', lastName: ''});
        setPrice('');
        setISBN('');
        setPages('');
        setLanguage(defaultLanguageId); // Reset the language to the default language
        setSelectedCategories([]);
        setImage('');
        setShowModal(false);
        setBookToEdit(null);
        setErrorMessage('');
    }

    return (
        <Modal show={showModal} onHide={resetForm}>
            <Modal.Header closeButton>
                <Modal.Title>{bookToEdit ? 'Edit book' : 'Add book'}</Modal.Title>
            </Modal.Header>
            <Modal.Body style={{ maxHeight: 'calc(100vh - 210px)', overflowY: 'auto' }}>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Book name</Form.Label>
                        <Form.Control type="text" value={name} onChange={e => setName(e.target.value)} required/>
                    </Form.Group>
                    <Row>
                        <Form.Group as={Col} className="mb-3">
                            <Form.Label>Author</Form.Label>
                            <Form.Control type="text" placeholder="First name" value={author.firstName}
                                          onChange={e => setAuthor({...author, firstName: e.target.value})} required/>
                        </Form.Group>
                        <Form.Group as={Col} className="mb-3">
                            <Form.Label>&nbsp;</Form.Label>
                            <Form.Control type="text" placeholder="Last name" value={author.lastName}
                                          onChange={e => setAuthor({...author, lastName: e.target.value})} required/>
                        </Form.Group>
                    </Row>
                    <Form.Group className="mb-3">
                        <Form.Label>Description</Form.Label>
                        <Form.Control as="textarea" value={description} onChange={e => setDescription(e.target.value)}
                                      required/>
                    </Form.Group>
                    <Row>
                        <Form.Group as={Col} className="mb-3">
                            <Form.Label>Price ($)</Form.Label>
                            <Form.Control type="number" value={price} onChange={e => setPrice(e.target.value)} required/>
                        </Form.Group>
                        <Form.Group as={Col} className="mb-3">
                            <Form.Label>ISBN</Form.Label>
                            <Form.Control type="text" value={ISBN} onChange={e => setISBN(e.target.value)} required/>
                            <Form.Text className="text-danger">{errorMessage}</Form.Text>
                        </Form.Group>
                    </Row>
                    <Dropdown>
                        <Dropdown.Toggle as={CustomToggle} id="dropdown-custom-components">
                            Select Categories
                        </Dropdown.Toggle>

                        <Dropdown.Menu as={CustomMenu}>
                            {allCategories.map(category => (
                                <Form.Check
                                    key={category.id}
                                    type="checkbox"
                                    id={`category-${category.id}`}
                                    label={category.name}
                                    value={category.id}
                                    onChange={handleCategoryChange}
                                    checked={selectedCategories.some(selectedCategory => selectedCategory.id === category.id)} // Set checked to true if the category is in selectedCategories
                                />
                            ))}
                        </Dropdown.Menu>
                    </Dropdown>
                    <br/>
                    <Row>
                        <Form.Group as={Col} className="mb-3">
                            <Form.Label>Pages</Form.Label>
                            <Form.Control type="number" value={pages} onChange={e => setPages(e.target.value)} required/>
                        </Form.Group>
                        <Form.Group as={Col} className="mb-3">
                            <Form.Label>Language</Form.Label>
                            <Form.Select value={language} onChange={e => setLanguage(Number(e.target.value))} required>
                                {allLanguages.map(language => (
                                    <option key={language.id} value={language.id}>{language.language}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                    </Row>
                    <Form.Group className="mb-3">
                        <Form.Label>Image</Form.Label>
                        <Form.Control type="text" placeholder="URL" value={image} onChange={e => setImage(e.target.value)}/>
                    </Form.Group>
                    <Button variant="primary" type="submit" onSubmit={handleSubmit}>
                        {bookToEdit ? 'Edit' : 'Add'}
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
}