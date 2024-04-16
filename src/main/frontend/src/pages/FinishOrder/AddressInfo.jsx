/**
 * Component for filling address when creating new order.
 * @author Lukas Petr
 */

import { useState } from 'react';
import Address from '../../components/UserForm/Adress';
import { Button, Form, Stack } from 'react-bootstrap';

/**
 * Component for filling address when creating new order.
 * @param props.userAddress Address of the user.
 * @param props.deliveryAddress Delivery address of the user.
 * @param {Function} props.onSubmit Handler for submit of the address information,
 * will receive the userAddress and deliveryAddress.
 * @component
 */
export default function AddressInfo({
  userAddress : defaultUserAddress,
  deliveryAddress : defaultDeliveryAddress,
  onSubmit,
}) {
  const [userAddress, setUserAddress] = useState(defaultUserAddress);
  const [deliveryAddress, setDeliveryAddress] = useState(defaultDeliveryAddress);
  
  function handleSubmit(e) {
    e.preventDefault();
    onSubmit(userAddress, deliveryAddress);
  }
  
  return (
    <>
      <Form className='w-50' onSubmit={handleSubmit}>
        <Stack gap={3}>
          <h3>Step 2: Address</h3>
          <h4>Billing address</h4>
          <Address state={userAddress.state}
                   town={userAddress.town}
                   postcode={userAddress.postcode}
                   street={userAddress.street}
                   streetNumber={userAddress.streetNumber}
                   onStateChange={(value) => setUserAddress((original) => ({...original, state: value}))}
                   onTownChange={(value) => setUserAddress((original) => ({...original, town: value}))}
                   onPostcodeChange={(value) => setUserAddress((original) => ({...original, postCode: value}))}
                   onStreetChange={(value) => setUserAddress((original) => ({...original, street: value}))}
                   onStreetNumberChange={(value) => setUserAddress((original) => ({...original, streetNumber: value}))}
                   />
          <h4>Delivery address</h4>
          <Address state={deliveryAddress.state}
                   town={deliveryAddress.town}
                   postcode={deliveryAddress.postcode}
                   street={deliveryAddress.street}
                   streetNumber={deliveryAddress.streetNumber}
                   onStateChange={(value) => setDeliveryAddress((original) => ({...original, state: value}))}
                   onTownChange={(value) => setDeliveryAddress((original) => ({...original, town: value}))}
                   onPostcodeChange={(value) => setDeliveryAddress((original) => ({...original, postCode: value}))}
                   onStreetChange={(value) => setDeliveryAddress((original) => ({...original, street: value}))}
                   onStreetNumberChange={(value) => setDeliveryAddress((original) => ({...original, streetNumber: value}))}/>
        <Button type='submit'>Finish the order</Button>
        </Stack>
      </Form>
    </>
  )
}