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
  // List of all languages
  const [languages, setLanguages] = useState([]);
  // List of all authors
  const [authors, setAuthors] = useState([]);
  // IDs of selected categories.
  const [selectedCatID, setSelectedCatID] = useState([]);
  // IDs of selected language ids.
  const [selectedLangID, setSelectedLangID] = useState([]);
  // IDs of selected author ids.
  const [selectedAuthorID, setSelectedAuthorID] = useState([]);

  // Fetches list of categories for filtering.
  useEffect(() => {
    api.get("/category")
      .then(response => {
        if (response.status === 200) {
          setCategories(response.data);
        }
      });
    api.get("/language")
      .then(response => {
        if (response.status === 200) {
          setLanguages(response.data);
        }
      })
    api.get("/bookauthor")
      .then(response => {
        if (response.status === 200) {
          setAuthors(response.data);
        }
      })
  }, []);

  /**
   * Filters books.
   */
  function handleFilter(e) {
    e.preventDefault();
    const params = {
      categoryIds: selectedCatID,
      authorIds: selectedAuthorID,
      languageIds: selectedLangID
    };
    const query = createSearchParams(params).toString();
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

  /**
   * Handles language change - checked/unchecked.
   * @param e Event
   * @param id Language id.
   */
  function handleLanguageChange(e, id) {
    if (e.target.checked) {
      setSelectedLangID(ids => [...ids, id]);
    } else {
      setSelectedLangID(ids => ids.filter(oldId => oldId !== id));
    }
  }

  /**
   * Handles author change - checked/unchecked.
   * @param e Event
   * @param id Author id.
   */
  function handleAuthorChange(e, id) {
    if (e.target.checked) {
      setSelectedAuthorID(ids => [...ids, id]);
    } else {
      setSelectedAuthorID(ids => ids.filter(oldId => oldId !== id));
    }
  }

  // TODO add more filters
  return (
    <div >
      <Form className="border border-primary px-2 py-3 rounded-3" onSubmit={handleFilter}>
        <h2>Filter</h2>
        <FilterList name="Categories" items={categories} getLabel={category => category.name} onItemClick={handleCategoryChange}/>
        <FilterList name="Languages" items={languages} getLabel={language => language.language}  onItemClick={handleLanguageChange}/>
        <FilterList name="Authors" items={authors} getLabel={author => `${author.firstName} ${author.lastName}`}  onItemClick={handleAuthorChange}/>
        <Button type="submit" className="mt-3 w-100">Filter</Button>
      </Form>
    </div>
  );
}

/**
 *
 * @param name
 * @param items
 * @param onItemClick
 * @param getLabel
 * @returns {JSX.Element}
 * @constructor
 */
function FilterList({name, items, onItemClick, getLabel}) {
  return (
    <Form.Group>
      <Form.Label>{name}</Form.Label>
      <ListGroup className="overflow-auto" style={{maxHeight: "140px"}}>
        {items.map(item => (
          <ListGroup.Item key={item.id}>
            <Form.Check type="checkbox" label={getLabel(item)} onClick={(e) => onItemClick(e, item.id)}/>
          </ListGroup.Item>
        ))}
      </ListGroup>
    </Form.Group>
  );
}