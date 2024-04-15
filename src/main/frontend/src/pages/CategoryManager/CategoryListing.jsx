/**
 * Component for listing of categories.
 * @author Martin Balaz
 */

import {Button, Table} from "react-bootstrap";

/**
 * Component for listing of categories.
 * @param categories - list of categories
 * @param deleteCategory - function for deleting a category
 * @param editCategory - function for editing a category
 * @returns {JSX.Element} - table with categories
 * @constructor - CategoryList
 */
export default function CategoryList({categories, deleteCategory, editCategory}) {
    return (
        <Table striped bordered hover>
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>
            </thead>
            <tbody>
            {categories.map((category, index) => (
                <tr key={category.id}>
                    <td>{category.name}</td>
                    <td>{category.description}</td>
                    <td>
                        <Button variant="primary" onClick={() => editCategory(category)}>Edit</Button>
                    </td>
                    <td>
                        <Button variant="danger" onClick={() => deleteCategory(category.id)}>Delete</Button>
                    </td>
                </tr>
            ))}
            </tbody>
        </Table>
    );
}