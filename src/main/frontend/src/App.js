/**
 * Web aplication, handling routing, auth...
 * @author Lukas Petr
 */

import {BrowserRouter, Route, Routes, useLocation} from 'react-router-dom';
import CategoryManager from './pages/CategoryManager';
import AuthProvider, {AuthContext} from "./context/AuthContext";
import Layout from "./pages/Layout";
import BooksPage from "./pages/Books";
import EmployeesManager from './pages/EmployeesManager';
import OrdersManager from './pages/OrdersManager';
import StorageManager from "./pages/StorageManager";
import BookAdd from './pages/BookAdd';
import Overview from "./pages/Overview";
import {useContext, useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import {Modal} from "react-bootstrap";
import NotFoundPage from "./pages/NotFound";
import ProtectedRoute from "./pages/ProtectedRoute";

/** Time remaining for JWT expiration in which we should try to renew the JWT token. */
const RENEW_AT_REMAINING_TIME = 600000; // (10 min)

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<BooksPage />} />
            <Route element={<ProtectedRoute role="employee" />} >
              <Route path="orders-manager" element={<OrdersManager />} />
              <Route path="storage-manager" element={<StorageManager />} />
            </Route>
            <Route element={<ProtectedRoute role="admin" />} >
              <Route path="category-manager" element={<CategoryManager />} />
              <Route path="employees-manager" element={<EmployeesManager />} />
              <Route path="book-add" element={<BookAdd />} />
              <Route path="overview" element={<Overview />} />
              <Route path="orders-manager" element={<OrdersManager />} />
              <Route path="storage-manager" element={<StorageManager />} />
            </Route>
            <Route path="*" element={<NotFoundPage/>} />
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
  const {user, logout, renewToken} = useContext(AuthContext);
  useEffect(() => {
    if (user) {
      // user is logged in
      const {exp} = jwtDecode(user.token);
      const remainingTime = exp * 1000 - Date.now();
      if (remainingTime < 0) {
        // Token expired
        logout();
        setShowLoggedOut(true);
      } else if (remainingTime < RENEW_AT_REMAINING_TIME) {
        // No much time left before JWT expires, try to renew it.
        renewToken();
      }
    }
  }, [location, user, logout, renewToken]);
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

