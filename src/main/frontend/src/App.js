/**
 * Web aplication, handling routing, auth...
 * @author Lukas Petr
 */

import {BrowserRouter, Route, Routes, useLocation} from 'react-router-dom';
import CategoryManager from './pages/CategoryManager';
import Layout from "./pages/Layout";
import BooksPage from "./pages/Books";
import EmployeesManager from './pages/EmployeesManager';
import OrdersManager from './pages/OrdersManager';
import StorageManager from "./pages/StorageManager";
import Overview from "./pages/Overview";
import {useContext, useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import {Modal} from "react-bootstrap";
import NotFoundPage from "./pages/NotFound";
import ProtectedRoute from "./pages/ProtectedRoute";
import BookDetailPage from "./pages/BookDetail";
import UserProfilePage from "./pages/UserProfile";
import UserCartPage from "./pages/UserCart";
import UserOrdersPage from "./pages/UserOrders";
import Register from './pages/Register';
import Providers from './context/Providers';
import { AuthContext } from './context/AuthContext';
import FinishOrder from './pages/FinishOrder';
import EditUser from './pages/EditUser';
import OrderDetail from './pages/OrderDetail';

/** Time remaining for JWT expiration in which we should try to renew the JWT token. */
const RENEW_AT_REMAINING_TIME = 600000; // (10 min)

export default function App() {
  return (
    <Providers>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<BooksPage />} />
            <Route path="/book/:bookId" element={<BookDetailPage />} />
            <Route path="/cart" element={<UserCartPage />}></Route>
            <Route path="/register" element={<Register />}></Route>
            <Route path="/finish-order" element={<FinishOrder />}></Route>
            <Route path="/order/:orderId" element={<OrderDetail />}></Route>
            <Route element={<ProtectedRoute roles={["user", "employee"]} />} >
              <Route path="/profile" element={<UserProfilePage />}></Route>
              <Route path="/my-orders" element={<UserOrdersPage />}></Route>
            </Route>
            <Route element={<ProtectedRoute roles={["employee"]} />} >
              <Route path="orders-manager" element={<OrdersManager />} />
              <Route path="storage-manager" element={<StorageManager />} />
            </Route>
            <Route element={<ProtectedRoute roles={["admin"]} />} >
              <Route path="category-manager" element={<CategoryManager />} />
              <Route path="employees-manager" element={<EmployeesManager />} />
              <Route path="overview" element={<Overview />} />
              <Route path="orders-manager" element={<OrdersManager />} />
              <Route path="storage-manager" element={<StorageManager />} />
              <Route path="user/:userId" element={<EditUser />} />
            </Route>
            <Route path="*" element={<NotFoundPage/>} />
          </Route>
        </Routes>
        <AuthVerify />
      </BrowserRouter>
    </Providers>
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
        renewToken(() => {
          // renewal was unsuccessful
          logout();
          setShowLoggedOut(true);
        });
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

