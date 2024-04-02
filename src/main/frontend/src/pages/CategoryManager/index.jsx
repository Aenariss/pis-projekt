import {useEffect, useState} from 'react';
import {api} from '../../api';

export default function CategoryManager() {
  const [categories, setCategories] = useState([]);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  function getCategories() {
    // Fetches list of categories from REST API.
    api.get('/category')
    .then(response => {
        // todo handling of errors
        setCategories(response.data);
    })
    .catch(error => console.log(error));
  }
  function addCategory(e) {
    // Calls REST API for adding category.
    e.preventDefault();
    api.post('/category', {name, description})
      .then(response => {
          if (response.status === 200) {
          setDescription('');
          setName('');
          getCategories();
          }
      })
      .catch(error => console.log(error));
  }

  // On load of page fetch list of categories.
  useEffect(() => {
    getCategories();
  }, []);

  return (
    <div>
      <h2>Categories</h2>
      <div>
      {
        categories.map(category => (<div key={category.id}>{category.name}: {category.description}</div>))
      }
      </div>
      <h2>Add new category</h2>
      <form onSubmit={addCategory}>
        <label>Category name: </label>
        <input type="text" value={name} onChange={(e) => setName(e.target.value)}></input>
        <br/>
        <label>Description: </label>
        <input type="text" value={description} onChange={(e) => setDescription(e.target.value)}></input>
        <br/>
        <input type="submit" value="Add category"/>
      </form>
    </div>
  );
}