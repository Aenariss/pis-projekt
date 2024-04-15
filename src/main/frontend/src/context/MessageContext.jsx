/**
 * Context for global showing of messages to user.
 * @author Lukas Petr
 */
import { createContext, useState } from "react";

export const MessageContext = createContext();

/**
 * Message provider component for setting messages which should be shown to user.
 * @component
 */
export default function MessageProvider({children}) {
  /**
   * Message to be shown to user, format {variant, text}
   * message.text Text of the message.
   * message.variant One of 'primary' | 'secondary' | 'success' | 'danger' |
   * 'warning' | 'info' | 'dark' | 'light' (see bootstrap alert for more info).
   */
  const [message, setMessage] = useState(null);
  return (
    <MessageContext.Provider value={{message, setMessage}}>
      {children}
    </MessageContext.Provider>
  );
}