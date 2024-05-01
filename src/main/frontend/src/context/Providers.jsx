/**
 * Combines providers to one.
 * @author Lukas Petr
 */
import AuthProvider from './AuthContext';
import CartProvider from './CartContext';
import MessageProvider from './MessageContext';

/**
 * Component which combines all providers to one.
 * @returns {JSX.Element} - Providers component
 * @constructor Providers
 */
export default function Providers({children}) {
    return (
      <AuthProvider>
        <CartProvider>
          <MessageProvider>
            {children}
          </MessageProvider>
        </CartProvider>
      </AuthProvider>
    );
  }