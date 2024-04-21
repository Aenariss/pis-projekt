/**
 * React entry point.
 * @author Lukas Petr
 */

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

// Bootstrap
import './bootstrap.scss';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);