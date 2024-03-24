/**
 * Web aplication, handling routing, auth...
 * @author Lukas Petr
 */

import {BrowserRouter, Route, Routes, useLocation} from 'react-router-dom';
import CategoryManager from './pages/CategoryManager';
import AuthProvider, {AuthContext} from "./context/AuthContext";
import Layout from "./pages/Layout";
import BooksPage from "./pages/Books";
import {useContext, useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import {Modal} from "react-bootstrap";

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<BooksPage />} />
            <Route path="category-manager" element={<CategoryManager />} />
          </Route>
        </Routes>
        <AuthVerify />
      </BrowserRouter>
    </AuthProvider>
  );
}

/**
 * Component, verifies if JWT is not expired on page change.
 * (Probably not the best approach but for now at least something).
 * @component
 */
function AuthVerify() {
  const [showLoggedOut, setShowLoggedOut] = useState(false);
  const location = useLocation();
  const {user, logout} = useContext(AuthContext);
  useEffect(() => {
    if (user) {
      // user is logged in
      const {exp} = jwtDecode(user.token);
      if (exp * 1000 < Date.now()) {
        logout();
        setShowLoggedOut(true);
      }
    }
  }, [location, user, logout]);
  return (
      <Modal show={showLoggedOut} onHide={() => setShowLoggedOut(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Session expired</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>You were logged out because your session expired.</p>
        </Modal.Body>
      </Modal>
    );
}
