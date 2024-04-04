/**
 * Component for searching for books.
 * @author Lukas Petr <xpetrl06>
 */
import {Button, Form, InputGroup} from "react-bootstrap";
import {useState} from "react";
import {createSearchParams, useNavigate} from "react-router-dom";

/**
 * Component for searching for books.
 */
export default function Search() {
  const [input, setInput] = useState('');
  const navigate = useNavigate();

  function search(e) {
    e.preventDefault();
    navigate({pathname: '/', search: `?${createSearchParams({query: input})}`});
  }
  return (
    <Form className="mx-5" onSubmit={(e) => search(e)}>
      <InputGroup>
        <Form.Control type="text" data-bs-theme="light" placeholder="Search by book name" value={input} onChange={(e) => setInput(e.target.value)}/>
        <Button type="submit">Search</Button>
      </InputGroup>
    </Form>
  );
}