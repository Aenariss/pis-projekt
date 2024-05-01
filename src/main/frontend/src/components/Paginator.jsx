/**
 * Component for going to another page.
 * @author Lukas Petr
 */
import { Pagination } from 'react-bootstrap';

/**
 * Component for showing number of pages and going to another page.
 * @param props Component props.
 * @param {number} props.pages Amount of pages.
 * @param {number} props.activePage Number of current page.
 * @param {Function} props.activePage Callback for setting page number.
 * @returns {JSX.Element} - Paginator component
 * @constructor Paginator
 */
export default function Paginator ({
  pages,
  activePage,
  setActivePage,
}) {
  return (
    <Pagination className='justify-content-center'>
        <Pagination.First onClick={() => setActivePage(1)}
                          disabled={ activePage === 1 }/>
        <Pagination.Prev onClick={() => setActivePage(activePage - 1)}
                         disabled={ activePage === 1 }/>
        {new Array(pages).fill().map((_,index) => {
          const page = index + 1;
          return (
            <Pagination.Item key={`paginator${page}`} active={page === activePage}
                             onClick={() => setActivePage(page)}>
              {page}
            </Pagination.Item>
          );
      })}
      <Pagination.Next onClick={() => setActivePage(activePage + 1)}
                       disabled={activePage === pages}/>
      <Pagination.Last onClick={() => setActivePage(pages)}
                       disabled={activePage === pages}/>
    </Pagination>
  )
}