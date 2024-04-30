/**
 * Page, where admin can see overview (graphs, statistics, etc.).
 * @author Lukas Petr
 */

import { useMemo, useState } from 'react';
import MostSoldCategoriesChart from './MostSoldCategoriesChart';
import { Button, ButtonGroup, Container, Navbar } from 'react-bootstrap';
import TopSoldProductsChart from './TopSoldProductsChart';

// For what period we are showing statistics
const THIS_YEAR = 0;
const THIS_MONTH = 1;
const THIS_WEEK = 2;
const THIS_DAY = 3;

export default function Overview() {
  const [period, setPeriod] = useState(THIS_DAY);
  // Setting from date according to chosen period
  const from = useMemo(() => {
    const date = new Date();
    switch (period) {
      case THIS_WEEK:
        // Which day of the week
        const day = date.getDay();
        // Set to monday
        date.setDate(date.getDate() - day + (day === 0 ? -6 : 1))
        break;
      case THIS_MONTH:
        date.setDate(1);
        break;
      case THIS_YEAR:
        date.setMonth(0);
        date.setDate(1);
        break;
      default: break;
    }
    date.setUTCHours(0,0,0,0);
    return date;
  }, [period]);

  const to = useMemo(() => {
    const date = new Date();
    date.setHours(23,59,59);
    return date;
  }, []);

  return (
    <Container>
        <Navbar bg="light" variant="light">
          <Container>
            <Navbar.Brand>Sells overview</Navbar.Brand>
            <div>From {from.toLocaleDateString()} to {to.toLocaleDateString()}</div>
            <ButtonGroup>
              <Button variant='outline-primary' active={period === THIS_DAY} onClick={() => setPeriod(THIS_DAY)}>
                Today
              </Button>
              <Button variant='outline-primary' active={period === THIS_WEEK} onClick={() => setPeriod(THIS_WEEK)}>
                This week
              </Button>
              <Button variant='outline-primary' active={period === THIS_MONTH} onClick={() => setPeriod(THIS_MONTH)}>
                This month
              </Button>
              <Button variant='outline-primary' active={period === THIS_YEAR} onClick={() => setPeriod(THIS_YEAR)}>
                This year
              </Button>
            </ButtonGroup>
          </Container>
        </Navbar>
        <Container className='mt-2'>
          <MostSoldCategoriesChart from={getDateForAPI(from)} to={getDateForAPI(to)} key={`category-statistics-${period}`}/>
          <TopSoldProductsChart from={getDateForAPI(from)} to={getDateForAPI(to)} key={`top-products-${period}`}/>
        </Container>
    </Container>
  );
}

/**
 * Returns date which API uses from javascript date.
 */
function getDateForAPI(date) {
  return date.toISOString().split('.')[0];
}
