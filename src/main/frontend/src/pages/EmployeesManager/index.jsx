/**
 * Page, where admin can edit employees.
 * @author Martin Balaz
 */

import NavbarComponent from "./NavbarComponent";
import {useEffect, useState} from "react";
import {api} from "../../api";
import {Table, Form, Button} from "react-bootstrap";


export default function ManageEmployees() {
    const [users, setUsers] = useState([]);
    const [activeLink, setActiveLink] = useState(localStorage.getItem('activeLink') || '#employees');
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        let data = localStorage.getItem('users'); // Handle refreshing the page
        if (data) {
            setUsers(JSON.parse(data));
        } else {
            if (searchQuery !== '') {
                // User is searching for a user
                setUsers(searchQuery);
            } else if (activeLink === '#employees') {
                // Get all employees
                api.get('/users/getEmployees')
                    .then(response => {
                        if (response.status === 200) {
                            setUsers(response.data);
                            localStorage.setItem('users', JSON.stringify(response.data)); // Handle refreshing the page
                        }
                    });
            } else if (activeLink === '#users') {
                // Set users to an empty array
                setUsers([]);
            }
        }
    }, [activeLink, searchQuery]);

    const handleRoleChange = (email, newRole) => {
        // Change role of the user and update the state
        api.post(`/setRole/${newRole}`, { email })
            .then(response => {
                if (response.status === 200) {
                    // Update the user's role in the state
                    setUsers(users.map(user => user.email === email ? { ...user, role: newRole } : user));
                    if (activeLink === '#employees') {
                        // If the new role is 'user', remove the user from the state
                        if (newRole === 'user') {
                            setUsers(users.filter(user => user.email !== email));
                            // Remove the user from the local storage
                            localStorage.setItem('users', JSON.stringify(users.filter(user => user.email !== email)));
                        }
                    }
                }
            });
    };

    const handleEditClick = (user) => {
        // Handle the edit click here
        console.log(`Editing user: ${user.email}`);
    };

    return (
        <div>
            <h2>Manage employees Page</h2>
            <NavbarComponent activeLink={activeLink} setActiveLink={setActiveLink} setSearchQuery={setSearchQuery} />
            <div>
                <br />
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Edit</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map((user, index) => (
                        <tr key={user.id}>
                            <td>{index + 1}</td>
                            <td style={{width: '30%'}}>{user.firstname} {user.surname}</td>
                            <td style={{width: '30%'}}>{user.email}</td>
                            <td style={{width: '20%'}}>
                                <Form.Select aria-label="example" defaultValue={user.role}
                                             onChange={e => handleRoleChange(user.email, e.target.value)}>
                                    <option value="admin">Admin</option>
                                    <option value="employee">Employee</option>
                                    <option value="user">User</option>
                                </Form.Select>
                            </td>
                            <td style={{width: '10%', textAlign: 'center'}}>
                                <Button
                                    style={{width: '100%'}}
                                    onClick={() => handleEditClick(user)}>Edit
                                </Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>
        </div>
    );
}