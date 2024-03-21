import {BrowserRouter, Route, Routes} from 'react-router-dom';
import CategoryManager from './pages/CategoryManager';
import Layout from './pages/Layout';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="category-manager" element={<CategoryManager />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
