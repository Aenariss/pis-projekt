/**
 * Page for keeping track of storage of books.
 * @author Martin Balaz
 */

import React, { useState, useEffect } from 'react';
import {api} from "../../api";
import StorageNavbarComponent from "./NavbarComponent";
import {useSearchParams} from "react-router-dom";
import BookListing from "./BookListing";

/**
 * Component for the Storage Manager page
 * @returns {Element} - Storage Manager page
 * @constructor - StorageManager
 */
export default function StorageManager() {
    const [books, setBooks] = useState([]);
    const [searchParams, setSearchParams] = useSearchParams()
    const [bookToEdit, setBookToEdit] = useState(null);

    /**
     * Function for updating the discount of a book in real-time
     * @param bookId - id of the book
     * @param newDiscount - new discount
     */
    const updateBookDiscount = (bookId, newDiscount) => {
        const updatedBooks = books.map(book => {
            // Find the book with the given id and update its discount
            if (book.id === bookId) {
                return {
                    ...book,
                    discount: {
                        ...book.discount,
                        discount: newDiscount
                    }
                };
            }
            return book;
        });

        setBooks(updatedBooks); // Update the state
    };

    /**
     * Function for getting all books using the REST API
     */
    function getBooks() {
        api.get('/productdescription')
            .then(response => {
                // sort books by name
                response.data.sort((a, b) => a.name.localeCompare(b.name));
                setBooks(response.data);
            });
    }

    /**
     * Function for getting books based on the search query
     * If there is no query, get all books
     * otherwise search for books based on the query
     */
    useEffect(() => {
        let query = searchParams.get('query');
        if (query) {
            // User is searching for a book
            api.post('/productdescription/search', {query})
                .then(response => {
                    setBooks(response.data);
                });
        } else getBooks();
    }, [searchParams]);

    /**
     * Function for handling the edit click
     * @param book - book to edit
     */
    const handleEditClick = (book) => {
        setBookToEdit(book);
    };

    /**
     * Function for handling the delete click
     * @param book - book to delete
     */
    const handleDeleteClick = (book) => {
        api.delete(`/productdescription/${book.id}`)
            .then(response => {
                if (response.status === 200) {
                    // Remove the book from the list
                    setBooks(books.filter(b => b.id !== book.id));
                }
            });
    }

    return (
        <div>
            <StorageNavbarComponent books={books} getBooks={getBooks} bookToEdit={bookToEdit} setBookToEdit={setBookToEdit} />
            <br />
            <BookListing books={books} handleEditClick={handleEditClick} handleDeleteClick={handleDeleteClick} updateBookDiscount={updateBookDiscount} />
        </div>
    );
}