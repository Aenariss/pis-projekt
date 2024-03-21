import {BrowserRouter, Route, Routes} from 'react-router-dom';
import CategoryManager from './pages/CategoryManager';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="category-manager" element={<CategoryManager />} />
      </Routes>
    </BrowserRouter>
  );
}
