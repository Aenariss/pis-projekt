/**
 * Page for showing details about customers/employees and editing them.
 * @author Lukas Petr <xpetrl06>
 */

import { useCallback, useContext, useEffect, useState } from "react";
import { api } from "../../api";
import UserForm from "../../components/UserForm";
import { MessageContext } from "../../context/MessageContext";
import { useParams } from "react-router-dom";


/**
 * Page for showing details about customers/employees and editing them.
 * @returns {JSX.Element} - EditUser component
 * @constructor EditUser
 */
export default function EditUser() {
  const {userId} = useParams();
  const [userInfo, setUserInfo] = useState(null);
  // Message to be shown to user, format {variant, text}
  const {setMessage} = useContext(MessageContext);

  const getUser = useCallback(() => {
    api.get(`user/${userId}`)
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
      setMessage({variant: 'danger', text: 'Error: The profile of the user was not possible to load!'})
    })
  }, [setMessage, userId]);

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
    api.put(`user/${userId}`, data)
      .then(() => {
        // success
        // reload user
        setMessage({variant: 'success', text: 'Changes were saved.'})
        getUser();
      })
      .catch(() => {
        setMessage({variant: 'danger', text: 'Error: the information were not saved!'})
      })
  }, [getUser, setMessage, userId]);

  if (userInfo === null) {
    // loading
    return null;
  }

  return (
    <>
      <UserForm defaultValues={userInfo}
                title={`User ${userInfo.firstname} ${userInfo.surname}`}
                type='edit'
                userId={userId}
                onSubmit={handleSubmit}/>
    </>
  );
}
