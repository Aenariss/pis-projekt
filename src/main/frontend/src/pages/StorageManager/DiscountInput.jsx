/**
 * Input field for discount value
 * @author Martin Balaz
 */

import React, {useEffect, useRef, useState} from 'react';
import {api} from "../../api";

/**
 * Input field for discount value
 * @param bookDiscount - initial discount value
 * @param productId - id of the product
 * @param onDiscountChange - callback function for updating the discount
 * @returns {Element} - discount input field
 * @constructor - DiscountInput
 */
function DiscountInput({bookDiscount, productId, onDiscountChange}) {
    const [discount, setDiscount] = useState(0);
    const initialDiscount = useRef(0); // Create a ref to store the initial discount value

    /**
     * Set the initial discount value
     * If the discount is undefined, set it to 0
     */
    useEffect(() => {
        if (bookDiscount !== undefined) {
            setDiscount(bookDiscount);
        } else {
            setDiscount(0);
        }
    }, [bookDiscount]);

    /**
     * Function for handling the change of the discount value - just to see price after discount in real-time
     * @param event - event of the input field
     */
    const handleChange = (event) => {
        const value = event.target.value;
        // Range from 0 to 100
        if (value >= 0 && value <= 100) {
            setDiscount(value); // Update the state
            // Calculate the new price and update the ref
            initialDiscount.current = value * initialDiscount.current;
            onDiscountChange(value); // Call the callback function
        }
    };

    /**
     * Function for handling the blur of the discount value (when the input field loses focus)
     * @param event - event of the input field
     */
    const handleBlur = (event) => {
        const value = event.target.value;
        // Range from 0 to 100
        if (value >= 0 && value <= 100) {
            setDiscount(value); // Update the state
            changeDiscount(value); // Call the function for updating the discount
            initialDiscount.current = value; // Update the ref
            onDiscountChange(value); // Call the callback function
        }
    }

    /**
     * Function for updating the discount of the product using the REST API
     * @param value - new discount value
     */
    function changeDiscount(value) {
        // Calls REST API for updating discount.
        api.put(`/productdescription/${productId}/discount/${value}`)
            .then(response => {
                if (response.status === 200) {
                    console.log("Discount updated");
                }
            })
            .catch(error => {
                alert(error.response.data); // Display error message
            });
    }

    return (
        <div style={{display: "flex", alignItems: "center"}}>
            <input
                type="number"
                min="0"
                max="100"
                step="10"
                style={{width: "50px"}}
                value={discount}
                onBlur={handleBlur}
                onChange={handleChange}
            />
            <span style={{marginLeft: "5px"}}>%</span>
        </div>
    );
}

export default DiscountInput; // Export the component