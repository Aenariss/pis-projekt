/**
 * Page for ordering items from cart.
 * @author Lukas Petr
 */

import { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../../context/AuthContext';
import { api } from '../../api';

import ContactInfo from './ContactInfo';
import AddressInfo from './AddressInfo';
import { CartContext } from '../../context/CartContext';
import { MessageContext } from '../../context/MessageContext';

export default function FinishOrder() {
  const {items, clearCart} = useContext(CartContext);
  const {setMessage} = useContext(MessageContext);
  const {user} = useContext(AuthContext);
  const [orderInfo, setOrderInfo] = useState(null);
  // Steps 0 - contact info, 1 address, 2 order is finished
  const [step, setStep] = useState(0);

  useEffect(() => {
    const info = {
      items: [],
      userAddress: {
        state: '',
        town: '',
        postCode: '',
        street: '',
        streetNumber: '',
      },
      deliveryAddress: {
        state: '',
        town: '',
        postCode: '',
        street: '',
        streetNumber: '',
      },
      userInfo: {
        firstname: '',
        surname: '',
        phone: '',
        email: '',
      }
    };
    // Fix what to do if items changes?
    info.items = Array.from(items)
                      .map(([id, amount]) => ({id, amount}));
    // User is logged in, get his info
    if (user) {
      api.get('/user')
        .then((response) => {
          const {firstname, surname, email, phone} = response.data;
          info.userInfo = {firstname, surname, email, phone};
          info.userAddress = response.data.address;
          info.deliveryAddress = response.data.address;
          setOrderInfo(info);
        })
    } else {
      setOrderInfo(info);
    }
  },[user, items]);

  function handleContactInfoSubmit(userInfo) {
    setOrderInfo((orderInfo) => ({...orderInfo, userInfo}));
    setStep(1);
  }
  function handleAddressSubmit(userAddress, deliveryAddress) {
    const order = {
      items: orderInfo.items,
      userAddress,
      deliveryAddress,
      orderUserInfo: orderInfo.userInfo,
    }
    api.post('/order/create', order)
      .then((response) => {
        // successful order
        clearCart();
        setStep(2);
      })
      .catch((error) => {
        setMessage({variant: 'danger', text: error?.response?.data })
      })

    // todo call api
  }

  let content = null;
  if (orderInfo !== null) {
    switch(step) {
      case 0:
        content = (
          <ContactInfo userInfo={orderInfo.userInfo}
                       onSubmit={handleContactInfoSubmit}/>
        );
        break;
      case 1:
        content = (
          <AddressInfo userAddress={orderInfo.userAddress}
                       deliveryAddress={orderInfo.deliveryAddress}
                       onSubmit={handleAddressSubmit}/>
        );
        break;
      case 2:
        content = (
          <div className='w-50' style={{fontSize: '20px'}}>
            <p style={{fontSize: '25px'}}>Thank you for your order!</p>
            <p>
              Your purchase has been successfully placed. We're now busy processing your order and getting it ready for shipment.
              You'll receive a confirmation email shortly with all the details of your purchase.
            </p>
          </div>

        );
        break;
      default: content = null;
    }
  }

  return (
    <>
      <h2>Making order</h2>
      {content}
    </>
  );
}