/**
 * Component for showing changes made to product (book).
 * @author Lukas Petr
 */

import { useContext, useEffect, useMemo, useState } from 'react';
import { Table } from 'react-bootstrap';
import { AuthContext } from '../../context/AuthContext';
import { api } from '../../api';

/**
 * Component for showing changes made to product (book).
 * @param props Component props.
 * @param props.bookId Id of the book.
 * @returns {JSX.Element} - BookEditHistory component
 * @constructor BookEditHistory
 */
export default function BookEditHistory({
  bookId
}) {
  const {user} = useContext(AuthContext);
  const [modifications, setModifications] = useState(null);
  // Only employees and admins can see modifications.
  const showModifications = useMemo(() => {
    return user?.role === 'admin' || user?.role === 'employee';
  }, [user]);


  useEffect(() => {
    if (showModifications) {
      api.get(`/productdescription/${bookId}/evidences`)
        .then(response => {
          const data = response.data;
          // Converting modification dates to js type
          data?.forEach((mod) => {
            mod.modificationDate = new Date(mod?.modificationDate);
          });
          // Sort modification date from the latest to oldest
          data?.sort((mod1, mod2) => (
            mod2.modificationDate - mod1.modificationDate
          ));
          setModifications(response.data);
        })
        .catch(() => {});
    }
  }, [showModifications, bookId]);

  if (! showModifications) return null;
  if (modifications === null || modifications.length <= 0) return null;

  // Note contains lot of classes to make the table scrollable
  return (
    <div className='mt-5'>
      <h3>History of changes</h3>
      <div className="container">
        <Table bordered hover className='w-100'>
          <thead className='d-block'>
            <tr className='d-block'>
              <th className='d-block float-start col-sm-3 bg-secondary border-secondary text-light'>
                Time
              </th>
              <th className='d-block float-start col-sm-5 bg-secondary border-secondary text-light'>
                Description
              </th>
              <th className='d-block float-start col-sm-4 bg-secondary border-secondary text-light'>
                Author
              </th>
            </tr>
          </thead>
          <tbody style={{maxHeight: '250px', overflowY: 'auto'}}
                className='d-block w-100'>
              {modifications.map((mod, index) => (
                <tr key={index} className='d-block'>
                  <td className='d-block float-start col-sm-3'>
                    {mod?.modificationDate.toLocaleString()}
                  </td>
                  <td className='d-block float-start col-sm-5'>
                    {mod?.changeDescription}
                  </td>
                  <td className='d-block float-start col-sm-4'>
                    {mod?.firstname} {mod?.surname} ({mod?.email})
                  </td>
                </tr>
              ))}
            </tbody>
        </Table>
      </div>
    </div>
  );
}
