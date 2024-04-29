/**
 * Input field for stock value
 * @author Martin Balaz
 */

import React, {useEffect, useRef, useState} from 'react';
import {api} from "../../api";

/**
 * Input field for stock value
 * @param bookStock - initial stock value
 * @param productId - id of the product
 * @param onStockChange - callback function for updating the stock
 * @returns {Element} - stock input field
 * @constructor - StockInput
 */
function StockInput({bookStock, productId}) {
    const [stock, setStock] = useState(0);
    const initialStock = useRef(0); // Create a ref to store the initial stock value

    /**
     * Set the initial stock value
     * If the stock is undefined, set it to 0
     */
    useEffect(() => {
        if (bookStock !== undefined) {
            setStock(bookStock);
        } else {
            setStock(0);
        }
    }, [bookStock]);

    /**
     * Function for handling the change of the stock value - just to see stock in real-time
     * @param event - event of the input field
     */
    const handleChange = (event) => {
        const value = event.target.value;
        // Value must be a positive number
        if (value >= 0) {
            setStock(value); // Update the state
        }
    };

    /**
     * Function for updating the stock of a product using the REST API
     * @param value - new stock value
     */
    const changeStock = (value) => {
        api.put(`/productdescription/${productId}/${stock}`)
            .then(response => {
                if (response.status === 200) {
                    console.log('Stock updated');
                }
            })
            .catch(error => {
                alert(error.response.data); // Display error message
            });
    }

    /**
     * Function for handling the blur of the stock value (when the input field loses focus)
     * @param event - event of the input field
     */
    const handleBlur = (event) => {
        const value = event.target.value;
        // Value must be a positive number
        if (value >= 0) {
            setStock(value); // Update the state
            changeStock(value); // Call the function for updating the stock
            initialStock.current = value; // Update the ref
        }
    };

    return (
        <input
            type='number'
            value={stock}
            style={{width: "50px"}}
            onChange={handleChange}
            onBlur={handleBlur}
        />
    );
}

export default StockInput;