/**
 * Context for containing cart content.
 * @author Lukas Petr
 */

import { createContext, useCallback, useState } from "react";

export const CartContext = createContext();

/**
 * Component providing context for containing info about cart content.
 * @returns {JSX.Element} - CartProvider component
 * @constructor CartProvider
 */
export default function CartProvider({children}) {
  // Map id -> amount
  const [items, setItems] = useState(() => {
    const localStorageCart = JSON.parse(localStorage.getItem('cart'));
    return localStorageCart ? new Map(localStorageCart) : new Map();
  });

  /**
   * Adds one amount of item to cart.
   * @param id Id of the item.
   */
  const addOneToCart = useCallback((id) => {
    setItems((items) => {
      const newItems = new Map(items);
      if (newItems.has(id)) {
        // If item is already in the cart, increase amount
        newItems.set(id, newItems.get(id) + 1);
      } else {
        // Item is not in the card add it to it.
        newItems.set(id, 1);
      }
      // add to local storage to be available after reload
      localStorage.setItem('cart', JSON.stringify(Array.from(newItems.entries())));
      return newItems;
    })
  }, []);
  /**
   * Removes one amount of item from the cart.
   * @param id Id of the item.
   */
  const removeOneFromCart = useCallback((id) => {
    setItems((items) => {
      const newItems = new Map(items);
      const amount = newItems.get(id);
      if (amount === 1) {
        // Last item of this id, remove it totally
        newItems.delete(id);
      } else {
        newItems.set(id, amount - 1);
      }
      // Updating local storage
      localStorage.setItem('cart', JSON.stringify(Array.from(newItems.entries())));
      return newItems;
    })
  }, []);

  /**
   * Removes item from the cart.
   * @param id Id of the item.
   */
  const removeFromCart = useCallback((id) => {
    setItems((items) => {
      const newItems = new Map(items);
      newItems.delete(id);
      // Updating local storage
      localStorage.setItem('cart', JSON.stringify(Array.from(newItems.entries())));
      return newItems;
    })
  }, []);

  /**
   * Set amount of item to amount.
   * If the item is not in cart then it adds and sets the amount.
   * If it is then it updates the amount.
   * @param id Id of the book.
   * @param amount Amount to update the amount of the book in the cart.
   */
    const setAmountForId = useCallback((id, amount) => {
      if (amount === 0) removeFromCart(id);
      else {
        setItems((items) => {
          const newItems = new Map(items);
          if (newItems.has(id)) {
            // Item is already in the cart
            newItems.set(id, amount);
          } else {
            // Item is not in the card add it to it.
            newItems.set(id, amount);
          }
          // add to local storage to be available after reload
          localStorage.setItem('cart', JSON.stringify(Array.from(newItems.entries())));
          return newItems;
        })
      }
    },[removeFromCart]);


  /**
   * Clearing cart from all of the items.
   * (used when the order is finished).
   */
  const clearCart = useCallback(() => {
    setItems(new Map());
    localStorage.removeItem('cart');
  }, []);

  /** Returns amount of item with specified id in the cart. */
  const getAmountForId = useCallback((id) => {
    if (items.has(id)) {
      return items.get(id);
    } else {
      return 0;
    }
  },[items]);

  return (
    <CartContext.Provider value={{
      addOneToCart,
      removeOneFromCart,
      removeFromCart,
      items,
      getAmountForId,
      setAmountForId,
      clearCart,
      }}>
      {children}
    </CartContext.Provider>
  )
}