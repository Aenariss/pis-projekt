/**
 * Component for searching orders by email (uses search params).
 * @author Lukas Petr
 */
import { useEffect, useState } from 'react';
import { Button, Form, FormControl, InputGroup } from 'react-bootstrap';
import { useSearchParams } from 'react-router-dom';

/**
 * Component for searching orders by email (uses search params).
 * @returns {JSX.Element} - SearchByEmail component
 * @constructor SearchByEmail
 */
export default function SearchByEmail() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [email, setEmail] = useState('');

  useEffect(() => {
    const email = searchParams.get('email');
    setEmail(email ? email : '');
  }, [searchParams])

  function handleSearchByEmail(e) {
    e.preventDefault();
    if (email) {
      setSearchParams({email})
    } else {
      setSearchParams();
    }
  }

  return (
    <Form onSubmit={handleSearchByEmail}>
      <Form.Group className='d-flex flex-row align-items-center'
                  controlId='search-by-email'>
        <Form.Label style={{width: '170px'}} className='my-0'>
          Search by email:
        </Form.Label>
        <InputGroup>
          <FormControl type='email'
                      placeholder='email'
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}/>
          <Button variant='outline-success'
                  type='submit'>
            Search orders
          </Button>
        </InputGroup>
      </Form.Group>
  </Form>
  );
}