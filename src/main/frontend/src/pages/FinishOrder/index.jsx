/**
 * Page for ordering items from cart.
 * @author Lukas Petr
 */

import { useContext, useEffect, useState } from "react";
import { AuthContext } from '../../context/AuthContext';
import { api } from "../../api";

export default function FinishOrder() {
  const {user} = useContext(AuthContext);
  const [userInfo, setUserInfo] = useState(null);
  useEffect(() => {
    // User is logged in, get his info
    if (user) {
      api.get('/user')
        .then((response) => {
          setUserInfo(response.data);
          console.log(response.data)
        })
    }
  },[user]);
  return (
    <>TODO Finish order</>
  );
}