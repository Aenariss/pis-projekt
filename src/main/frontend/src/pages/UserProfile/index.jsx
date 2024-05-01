/**
 * User profile page.
 * @author Lukas Petr <xpetrl06>
 */

import { useCallback, useContext, useEffect, useState } from "react";
import { api } from "../../api";
import UserForm from "../../components/UserForm";
import { MessageContext } from "../../context/MessageContext";


/**
 * User profile page component.
 * @returns {JSX.Element} - UserProfilePage component
 * @constructor UserProfilePage
 */
export default function UserProfilePage() {
  const [userInfo, setUserInfo] = useState(null);
  // Message to be shown to user, format {variant, text}
  const {setMessage} = useContext(MessageContext);

  const getUser = useCallback(() => {
    api.get('user')
    .then((response) => {
      const data = response.data;
      const info = {
        firstname: data.firstname,
        surname: data.surname,
        email: data.email,
        phone: data.phone,
        state: data.address.state,
        town: data.address.town,
        street: data.address.street,
        streetNumber: data.address.streetNumber,
        postCode: data.address.postCode,
      };
      setUserInfo(info);
    })
    .catch(() => {
      setMessage({variant: 'danger', text: 'The profile loading was unsuccessful!'})
    })
  }, [setMessage]);

  useEffect(() => {
    getUser()
  },[getUser]);

  const handleSubmit = useCallback((info) => {
    const data = {
      firstname: info.firstname,
      surname: info.surname,
      email: info.email,
      phone: info.phone,
      address: {
        state: info.state,
        town: info.town,
        street: info.street,
        streetNumber: info.streetNumber,
        postCode: info.postCode,
      },
    }
    api.put('user', data)
      .then(() => {
        // success
        // reload user
        setMessage({variant: 'success', text: 'Your changes were saved.'})
        getUser();
      })
      .catch(() => {
        setMessage({variant: 'danger', text: 'Error: the information were not saved!'})
      })
  }, [getUser, setMessage]);

  if (userInfo === null) {
    // loading
    return null;
  }

  return (
    <>
      <UserForm defaultValues={userInfo}
                title='Your profile'
                type='profile'
                onSubmit={handleSubmit}/>
    </>
  );
}
