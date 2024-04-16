/**
 * Context for containing cart content.
 * @author Lukas Petr
 */

import { createContext, useCallback, useState } from "react";

export const CartContext = createContext();

/**
 * Component providing context for containing info about cart content.
 * @component
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

  /** Returns amount of item with specified id in the cart. */
  const getAmountForId = useCallback((id) => {
    if (items.has(id)) {
      return items.get(id);
    } else {
      return 0;
    }
  },[items]);

  return (
    <CartContext.Provider value={{addOneToCart, removeOneFromCart, removeFromCart, items, getAmountForId}}>
      {children}
    </CartContext.Provider>
  )
}