/**
 * Component for showing global messages to user.
 * @author Lukas Petr
 */

import { useContext, useEffect, useState } from 'react';
import { Alert, ToastContainer } from 'react-bootstrap';
import { MessageContext } from '../../context/MessageContext';

/** Period of time after which the message will stop showing.  */
const TIMEOUT = 3000;

/**
 * Component for showing message to the user.
 * @component
 */
export default function Message() {
  const [show, setShow] = useState(null);
  const { message} = useContext(MessageContext);
  useEffect(() => {
    setShow(message);
    const timeoutId = setTimeout(() => {
      setShow(null);
    }, TIMEOUT);
    // Cleanup
    return () => clearTimeout(timeoutId);
  }, [message])
  if (! show) {
    return null;
  }
  const {variant, text} = show;
  return (
    <ToastContainer position='top-center'
                    className='p-3'
                    style={{ zIndex: 10 }}>
      <Alert variant={variant}
             style={{fontSize: '20px'}}
             className='px-5 py-3'>
          {text}
      </Alert>
  </ToastContainer>
  );
}