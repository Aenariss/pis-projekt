/**
 * Filtering of books.
 * @author Lukas Petr <xpetrl06>
 */
import {useEffect, useState} from "react";
import {Button, Form, ListGroup} from "react-bootstrap";
import {useSearchParams} from "react-router-dom";
import {api} from "../../api";

/**
 * Component for filtering of books.
 * @component
 */
export default function Filter() {
  const [searchParams, setSearchParams] = useSearchParams();
  // List of all categories
  const [categories, setCategories] = useState([]);
  // List of all languages
  const [languages, setLanguages] = useState([]);
  // List of all authors
  const [authors, setAuthors] = useState([]);
  // IDs of selected categories.
  const [selectedCatID, setSelectedCatID] = useState(new Set());
  // IDs of selected language ids.
  const [selectedLangID, setSelectedLangID] = useState(new Set());
  // IDs of selected author ids.
  const [selectedAuthorID, setSelectedAuthorID] = useState(new Set());
  useEffect(() => {
    // Updating selected items according to url param
    setSelectedCatID(new Set(searchParams.getAll('categoryIds').map(id => Number(id))));
    setSelectedAuthorID(new Set(searchParams.getAll('authorIds').map(id => Number(id))));
    setSelectedLangID(new Set(searchParams.getAll('languageIds').map(id => Number(id))));
  }, [searchParams]);

  // Fetches list of categories for filtering.
  useEffect(() => {
    Promise.all([
      api.get("/category"),
      api.get("/language"),
      api.get("/bookauthor")
    ]).then(([categoryResponse, languageResponse, authorResponse]) => {
        setCategories(categoryResponse.data);
        setLanguages(languageResponse.data);
        setAuthors(authorResponse.data);
    });
  }, []);

  /**
   * Filters books.
   */
  function handleFilter(e) {
    e.preventDefault();
    const params = {
      categoryIds: Array.from(selectedCatID),
      authorIds: Array.from(selectedAuthorID),
      languageIds: Array.from(selectedLangID)
    };
    setSearchParams(params);
  }
  /**
   * Updates selected ids based on checked id in filter.
   * @param setFunction One of selectedCatID, selectedLangID, selectedAuthorID.
   * @param id Id which was changed.
   */
  function handleChange(setFunction, id) {
    setFunction(ids => {
      const newIds = new Set(ids);
      // If id was selected then remove it, otherwise add it.
      if (ids.has(id)) {
        newIds.delete(id);
      } else {
        newIds.add(id);
      }
      return newIds;
    });
  }

  return (
    <div >
      <Form className="border border-primary px-2 py-3 rounded-3" onSubmit={handleFilter}>
        <h2>Filter</h2>
        <FilterList name="Categories"
                    items={categories}
                    getLabel={category => category.name}
                    onItemClick={(id) => handleChange(setSelectedCatID, id)}
                    showTitle={(item) => item.description}
                    selectedIds={selectedCatID}/>
        <FilterList name="Languages"
                    items={languages}
                    getLabel={language => language.language}
                    onItemClick={(id) => handleChange(setSelectedLangID, id)}
                    selectedIds={selectedLangID}/>
        <FilterList name="Authors"
                    items={authors}
                    getLabel={author => `${author.firstName} ${author.lastName}`}
                    onItemClick={(id) => handleChange(setSelectedAuthorID, id)}
                    selectedIds={selectedAuthorID}/>
        <Button type="submit" className="mt-3 w-100">Filter</Button>
      </Form>
    </div>
  );
}

/**
 * List of options to filter.
 * @param props.name Name of the filter.
 * @param props.items Possible items to select.
 * @param props.onItemsClick Handler called with item id.
 * @param props.getLabel Function which returns label for given item.
 * @param props.selectedIds Set of selected ids.
 * @param props.showTitle Function which returns title for a book or undefined.
 */
function FilterList({
  name,
  items,
  onItemClick,
  getLabel,
  selectedIds,
  showTitle,
}) {
  return (
    <Form.Group>
      <Form.Label>{name}</Form.Label>
      <ListGroup className="overflow-auto"
                 style={{maxHeight: "140px"}}>
        {items.map(item => (
            <label className="list-group-item" key={`label-${name}-${item.id}`}
                   title={showTitle?.(item)}>
              <input className="form-check-input me-1"
                     type="checkbox"
                     checked={selectedIds.has(item.id)}
                     onChange={() => onItemClick(item.id)}/>
              {getLabel(item)}
            </label>
        ))}
      </ListGroup>
    </Form.Group>
  );
}