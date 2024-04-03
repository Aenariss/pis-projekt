/**
 * Filtering of books.
 * @author Lukas Petr <xpetrl06>
 */
import {useEffect, useState} from "react";
import {Button, Form, ListGroup} from "react-bootstrap";
import {createSearchParams, useNavigate} from "react-router-dom";
import {api} from "../../api";

/**
 * Component for filtering of books.
 * @component
 */
export default function Filter() {
  const navigate = useNavigate();
  // List of all categories
  const [categories, setCategories] = useState([]);
  // IDs of selected categories.
  const [selectedCatID, setSelectedCatID] = useState([]);

  // Fetches list of categories for filtering.
  useEffect(() => {
    api.get("/category")
      .then(response => {
        if (response.status === 200) {
          setCategories(response.data);
        }
      })
  }, []);

  /**
   * Filters books.
   */
  function handleFilter(e) {
    e.preventDefault();
    const query = createSearchParams({categoryIds: selectedCatID}).toString();
    navigate({pathname: '/', search: `?${query}`});
  }

  /**
   * Handles category change - checked/unchecked.
   * @param e Event
   * @param id Category id.
   */
  function handleCategoryChange(e, id) {
    if (e.target.checked) {
      // category was checked add it to list
      setSelectedCatID(ids => [...ids, id]);
    } else {
      // category was unchecked remove it from list
      setSelectedCatID(ids => ids.filter(oldId => oldId !== id));
    }
  }
  // TODO add more filters
  return (
    <div >
      <Form className="border border-primary px-2 py-3 rounded-3" onSubmit={handleFilter}>
        <h2>Filter</h2>
        <Form.Group>
          <Form.Label>Categories</Form.Label>
          <ListGroup className="overflow-auto" style={{maxHeight: "140px"}}>
            {categories.map(category => (
              <ListGroup.Item key={category.id}>
                <Form.Check type="checkbox" label={category.name} onClick={(e) => handleCategoryChange(e, category.id)}/>
              </ListGroup.Item>
            ))}
          </ListGroup>
        </Form.Group>
        <Button type="submit" className="mt-3 w-100">Filter</Button>
      </Form>
    </div>
  );
}