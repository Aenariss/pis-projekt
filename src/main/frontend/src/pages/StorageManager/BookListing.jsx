/**
 * List of books for admin to manage storage of books.
 * @author Martin Balaz
 */

import React, {useContext} from 'react';
import {Button, Table} from "react-bootstrap";
import DiscountInput from "./DiscountInput";
import {AuthContext} from "../../context/AuthContext";
import { Link } from 'react-router-dom';

/**
 * List of books for admin/employee to manage storage of books
 * @param books - list of books
 * @param handleEditClick - function for handling edit button click
 * @param handleDeleteClick - function for handling delete button click
 * @param updateBookDiscount - function for updating the discount of the book
 * @returns {Element} - table with books
 * @constructor - BookListing
 */
export default function BookListing({books, handleEditClick, handleDeleteClick, updateBookDiscount}) {
    const {user} = useContext(AuthContext);
    const isAdmin = user?.role === 'admin';

    // Removing duplicates - search can return multiple instances of the same item
    const ids = [];
    const booksCleaned = [];
    books?.forEach(book => {
        if (!ids.includes(book.id)) {
            booksCleaned.push(book);
            ids.push(book.id);
        }
    });

    return (
        <Table striped bordered hover>
            <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Author</th>
                <th>Price</th>
                <th>Sale</th>
                {isAdmin && <th>Discount</th>}
                <th>Stock</th>
                {isAdmin && <th>Edit</th>}
                {isAdmin && <th>Delete</th>}
            </tr>
            </thead>
            <tbody>
            {booksCleaned.map((book, index) => (
                <tr key={index}>
                    <td>{index + 1}</td>
                    <td>
                        <Link as='a'
                              role='button'
                              className='link-dark link-underline link-underline-opacity-0 link-underline-opacity-100-hover'
                              to={`/book/${book?.id}`}>
                            {book.name}
                        </Link>
                    </td>
                    <td>
                        <Link as='a'
                              role='button'
                              className='link-dark link-underline link-underline-opacity-0 link-underline-opacity-100-hover'
                              to={`/?authorIds=${book?.author?.id}`}>
                            {book.author?.firstName} {book.author?.lastName}
                        </Link>
                    </td>
                    <td>{parseFloat(book.price).toFixed(2)}&nbsp;$</td>
                    <td>
                        <div style={{display: "flex"}}>
                            {(book.discount?.discount === 0) || (book.discount?.discount === undefined)
                                ? <><span>{parseFloat(book.price).toFixed(2)}</span><span>&nbsp;$</span></>
                                : <>
                                    <span>{parseFloat(book.price - (book.price * book.discount?.discount / 100)).toFixed(2)}</span><span>&nbsp;$</span></>
                            }
                        </div>
                    </td>
                    {/*TODO: Add stock to the book model*/}
                    {isAdmin &&
                    <td style={{width: "6%"}}>
                        <DiscountInput
                            bookDiscount={book.discount?.discount}
                            productId={book.id}
                            onDiscountChange={(newDiscount) => updateBookDiscount(book.id, newDiscount)}
                        />
                    </td>}
                    <td>{book.stock}</td>
                    {isAdmin &&
                    <td style={{width: "4%"}}>
                        <Button onClick={() => handleEditClick(book)}>Edit</Button>
                    </td>}
                    {isAdmin &&
                    <td style={{width: "5%"}}>
                        <Button variant="danger" onClick={() => handleDeleteClick(book)}>Delete</Button>
                    </td>}
                </tr>
            ))}
            </tbody>
        </Table>
    );
}