/**
 * Cart page.
 * @author Lukas Petr <xpetrl06>
 */

import { useContext, useEffect, useState } from "react";
import { CartContext } from "../../context/CartContext";
import { Button, Image, Table } from "react-bootstrap";
import { api } from "../../api";
import { X } from "react-bootstrap-icons";
import { Link, useNavigate } from "react-router-dom";
import AmountChanger from "../../components/Cart/AmountChanger";

/**
 * Cart page component.
 * @component
 */
export default function UserCartPage() {
  const navigate = useNavigate();
  const {
    items,
    getAmountForId,
    removeFromCart
  } = useContext(CartContext);
  const [books, setBooks] = useState([]);

  useEffect(() => {
    api.get('/productdescription')
      .then((response) => {
        const allBooks = response.data;
        // filtering - getting only books which are in the cart
        const books = allBooks.filter(book => items.has(book.id));
        // Counting current price for the books.
        books.forEach(book => {
          book.currentPrice = book.price;
          if (book?.discount?.discount) {
            book.currentPrice = book.price *((100-book.discount.discount)/100);
          }
        })
        setBooks(books);
      });
  }, [items]);
  // counting total price
  const totalPrice = books.map(book => (
    getAmountForId(book.id) * book.currentPrice
  )).reduce((total, price) => (total + price), 0);

  function handleDeleteItem(id) {
    removeFromCart(id);
    setBooks(books => books.filter(book => book.id !== id));
  }

  return (
    <>
      <h2>Your cart</h2>
      { books.length === 0
        ? (
          <div className='display-5 text-center text-muted mt-5'>
            Your cart is empty
          </div>
        ) : (
        <>
          <Table striped>
            <thead>
              <tr>
                <th></th>
                <th>Title</th>
                <th>Amount</th>
                <th>Price</th>
              </tr>
            </thead>
            <tbody>
              {books.map(book => {
                const amount = getAmountForId(book.id);
                return (
                  <tr key={book.id}>
                    <td><Image src={book?.image} height='150px'/></td>
                    <td className='w-50'>
                      <div style={{fontSize: '20px'}}>
                        <Link as='a'
                              role='button'
                              className='link-dark link-underline link-underline-opacity-0 link-underline-opacity-100-hover'
                              to={`/book/${book.id}`}>
                              {book?.name}
                        </Link>
                      </div>
                      <div>
                        <Link as='a'
                              role='button'
                              className='link-dark link-underline link-underline-opacity-0 link-underline-opacity-100-hover'
                              to={`/?authorIds=${book?.author?.id}`}>
                          {book.author?.firstName} {book.author?.lastName}
                        </Link>
                      </div>
                    </td>
                    <td>
                      <AmountChanger bookId={book?.id} availableQuantity={book?.availableQuantity} />
                      </td>
                    <td>
                      <div style={{fontSize: '20px'}}>{(amount * book.currentPrice).toFixed(2)} $</div>
                      <div  className='text-muted'>{book.currentPrice.toFixed(2)} $ / item</div>
                    </td>
                    <td>
                      <Button className='p-1'
                              variant='danger'
                              onClick={() => handleDeleteItem(book.id)}>
                        <X size={25}/>
                      </Button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </Table>
          <div className='d-flex'>
            <span className='display-6'>Total price: {totalPrice.toFixed(2)} $</span>
            <Button className='ms-auto px-5'
                    size='lg'
                    onClick={() => navigate('/finish-order')}>
              Continue to order
            </Button>
          </div>
        </>
      )}
    </>
  );
}
