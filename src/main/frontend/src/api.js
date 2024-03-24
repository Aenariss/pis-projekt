/**
 * Exports object for calling REST API.
 * @author Lukas Petr (xpetrl06)
 */

import axios from 'axios';

/**
 * Configured axios for calling REST API.
 *
 * @example
 * api.get('/category').then(response => ...)
 * @type {axios.AxiosInstance}
 */
export const api = axios.create({
  baseURL: 'http://localhost:9089/api/',
});

api.interceptors.request.use((config) => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (user) {
    // If user is logged in, setting authorization header.
    const {token} = user;
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));