/**
 * Component for showing ordered items in order detail.
 * @author Lukas Petr
 */

import { Image, Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';

/**
 * Component for showing ordered items in order detail.
 * @param props.items Ordered items.
 * @component
 */
export default function OrderedItems({items}) {
  return (
    <div className='mt-3'>
      <h3>Ordered items:</h3>
      <Table className='w-100' striped>
        <thead>
          <tr>
            <th></th>
            <th>Title</th>
            <th>Amount</th>
            <th>Price</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item) => {
            const book = item?.productDescription;
            return (
              <tr key={item.id}>
                <td width='100px'>
                  <Image src={book?.image}
                         style={{maxHeight: '80px', maxWidth: '80px'}}/>
                </td>
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
                <td style={{fontSize: '20px'}} >
                  {item.quantity}
                </td>
                <td>
                  <div style={{fontSize: '20px'}}>{(item.price).toFixed(2)} $</div>
                  <div className='text-muted'>{item.pricePerPiece.toFixed(2)} $ / item</div>
                </td>
              </tr>
            );
          })}
        </tbody>
      </Table>
    </div>
  )
}