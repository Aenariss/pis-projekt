/**
 * Wrapper around sites which can be visited only by user with certain role.
 * @author Lukas Petr
 */

import {useContext} from 'react';
import {AuthContext} from '../context/AuthContext';
import {Navigate, Outlet} from 'react-router-dom';
import {Alert} from 'react-bootstrap';

/**
 * Wrapper around sites which can be visited only by user with certain role.
 * @param role Role which can visit the website.
 * @returns {JSX.Element} - ProtectedRoute component
 * @constructor ProtectedRoute
 */
export default function ProtectedRoute({roles}) {
  const {user} = useContext(AuthContext);
  if (roles.includes(user?.role) || user?.role === 'admin') {
    // Show the site only if the user has specified role or if it is admin.
    return <Outlet />;
  }
  // Else redirect to index.
  return (
    <>
      <Alert variant="danger">Error: You do not have permission to visit this site!</Alert>
      <Navigate to="/" />
    </>
  )
}