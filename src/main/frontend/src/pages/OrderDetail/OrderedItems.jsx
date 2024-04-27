/**
 * Component for showing ordered items in order detail.
 * @author Lukas Petr
 */

import { Image, Table } from 'react-bootstrap';

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
            return (
              <tr key={item.id}>
                <td width='100px'>
                  <Image src={item?.image}
                         style={{maxHeight: '80px', maxWidth: '80px'}}/>
                </td>
                <td className='w-50'>
                  <div style={{fontSize: '20px'}}>
                    {item?.name}
                  </div>
                  <div>
                    {item?.firstName} {item?.lastName}
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