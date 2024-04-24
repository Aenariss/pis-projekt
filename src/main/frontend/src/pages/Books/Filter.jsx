/**
 * Filtering of books.
 * @author Lukas Petr <xpetrl06>
 */
import {useCallback, useEffect, useState} from "react";
import {Button, Form, ListGroup, Stack} from "react-bootstrap";
import {useSearchParams} from "react-router-dom";
import {api} from "../../api";
import NumberInput from "./NumberInput";
import { X } from "react-bootstrap-icons";

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
  // Price from
  const [priceFrom, setPriceFrom] = useState('');
  // Price To
  const [priceTo, setPriceTo] = useState('');
  // Is filter invalid
  const [isInvalid, setIsInvalid] = useState(false);

  useEffect(() => {
    // Updating selected items according to url param
    setSelectedCatID(new Set(searchParams.getAll('categoryIds').map(id => Number(id))));
    setSelectedAuthorID(new Set(searchParams.getAll('authorIds').map(id => Number(id))));
    setSelectedLangID(new Set(searchParams.getAll('languageIds').map(id => Number(id))));
    setPriceFrom(searchParams.get('priceFrom') || '');
    setPriceTo(searchParams.get('priceTo') || '');
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
      languageIds: Array.from(selectedLangID),
    };
    if (priceFrom !== '') params.priceFrom = priceFrom;
    if (priceTo !== '') params.priceTo = priceTo;
    setSearchParams(params);
  }
  /**
   * Updates selected ids based on checked id in filter.
   * @param setFunction One of selectedCatID, selectedLangID, selectedAuthorID.
   * @param id Id which was changed.
   */
  const handleChange = useCallback((setFunction, id) => {
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
  },[]);

  /**
   * Clears all filters and reloads the page.
   */
  const handleClear = useCallback(() => {
    setSearchParams();
  },[setSearchParams]);

  /** Clears price - does not reload the page. */
  const handlePriceClear = useCallback(() => {
    setPriceFrom('');
    setPriceTo('');
  }, []);

  return (
    <div >
      <Form className="border border-primary px-2 py-3 rounded-3" onSubmit={handleFilter}>
        <h2>Filter
          <Button variant='outline-primary px-2 py-1 float-end me-1' onClick={handleClear}>
            <X/> Clear
          </Button>
        </h2>
        <PriceFilter priceFrom={priceFrom}
                     onPriceFromChange={setPriceFrom}
                     priceTo={priceTo}
                     onPriceToChange={setPriceTo}
                     onInvalidChange={setIsInvalid}
                     onClear={handlePriceClear}/>
        <FilterList name="Categories"
                    items={categories}
                    getLabel={category => category.name}
                    onItemClick={(id) => handleChange(setSelectedCatID, id)}
                    showTitle={(item) => item.description}
                    selectedIds={selectedCatID}
                    onClear={() => setSelectedCatID(new Set())}/>
        <FilterList name="Languages"
                    items={languages}
                    getLabel={language => language.language}
                    onItemClick={(id) => handleChange(setSelectedLangID, id)}
                    selectedIds={selectedLangID}
                    onClear={() => setSelectedLangID(new Set())}/>
        <FilterList name="Authors"
                    items={authors}
                    getLabel={author => `${author.firstName} ${author.lastName}`}
                    onItemClick={(id) => handleChange(setSelectedAuthorID, id)}
                    selectedIds={selectedAuthorID}
                    onClear={() => setSelectedAuthorID(new Set())}/>
        <Button type="submit"
                className="mt-3 w-100"
                disabled={isInvalid}>
          Filter
        </Button>
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
 * @param props.onClear Called when the filter want to be cleared.
 */
function FilterList({
  name,
  items,
  onItemClick,
  getLabel,
  selectedIds,
  showTitle,
  onClear,
}) {
  return (
    <div className='mt-3'>
      <div className='d-flex justify-content-between'>
        <b>{name}</b>
        <ClearButton onClick={onClear}/>
      </div>
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
    </div>
  );
}

/**
 * Filter for filtering by price.
 * @param props.priceFrom Current set price from.
 * @param props.priceTo Current set price to.
 * @param props.onPriceFromChange Called with new price from on change.
 * @param props.onPriceToChange Called with new price to on change.
 * @param props.onClear Called when the filter want to be cleared.
 */
function PriceFilter({
  priceFrom,
  priceTo,
  onPriceFromChange,
  onPriceToChange,
  onInvalidChange,
  onClear
}) {
  // Checks if prices are invalid - if both are set but from is bigger then to
  const pricesInvalid = priceFrom !== '' && priceTo !== '' && Number(priceFrom) > Number(priceTo);
  useEffect(() => {
    onInvalidChange(pricesInvalid);
  },[pricesInvalid, onInvalidChange]);
  return (
    <>
      <div className='d-flex justify-content-between'>
        <b>Price</b>
        <ClearButton onClick={onClear}/>
      </div>
      <Stack direction='horizontal'>
        <NumberInput label='From:'
                     value={priceFrom}
                     onChange={onPriceFromChange}
                     isInvalid={pricesInvalid}/>
        <NumberInput label='To:'
                     value={priceTo}
                     onChange={onPriceToChange}
                     isInvalid={pricesInvalid}/>
      </Stack>
      {pricesInvalid && (
        <div className='text-danger'>
          Error: from must be lesser than to
        </div>
      )}
    </>
  );
}

/**
 * Button for clearing individual filters.
 * @param props.handleClick Called on click.
 */
function ClearButton({
  onClick
}) {
  return (
    <Button variant='outline-secondary px-1 py-0 me-1 my-1'
            onClick={onClick}>
      <X size='12px'/>
    </Button>
  );
}