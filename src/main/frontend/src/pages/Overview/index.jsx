/**
 * Page, where admin can see overview (graphs, statistics, etc.).
 * @author Lukas Petr
 */

import { useMemo, useState } from 'react';
import MostSoldCategoriesChart from './MostSoldCategoriesChart';
import { Button, ButtonGroup, Col, Container, Navbar, Row } from 'react-bootstrap';
import TopSoldProductsChart from './TopSoldProductsChart';
import SalesInTimeChart from './SalesInTimeChart';
import IncomeInTimeChart from './IncomeInTimeChart';

// For what period we are showing statistics
const THIS_YEAR = 0;
const THIS_MONTH = 1;
const THIS_WEEK = 2;
const THIS_DAY = 3;

// For what period we are showing statistics
const OVERVIEW_KIND_CATEGORIES = 0;
const OVERVIEW_KIND_PRODUCTS = 1;
const OVERVIEW_KIND_ORDERS = 2;
const OVERVIEW_KIND_INCOME = 3;

/**
 * Page component for showing overviews about sells.
 * @returns {JSX.Element} - Overview component
 * @constructor Overview
 */
export default function Overview() {
  // Period of time for which to show the overview
  const [period, setPeriod] = useState(THIS_DAY);
  // Kind of overview to show
  const [kind, setKind] = useState(OVERVIEW_KIND_INCOME);
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

  let Overview = IncomeInTimeChart;
  switch (kind) {
    case OVERVIEW_KIND_CATEGORIES: Overview = MostSoldCategoriesChart; break;
    case OVERVIEW_KIND_PRODUCTS: Overview = TopSoldProductsChart; break;
    case OVERVIEW_KIND_ORDERS: Overview = SalesInTimeChart; break;
    default: break;
  }

  return (
    <Container>
        <Navbar bg="light" variant="light">
          <Container>
            <Navbar.Brand>Sales overview</Navbar.Brand>
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
          <Row>
            <Col xs={3} >
                <ButtonGroup vertical style={{width: '100%'}}>
                  <Button variant='outline-primary'
                          onClick={() => setKind(OVERVIEW_KIND_INCOME)}
                          active={kind === OVERVIEW_KIND_INCOME}>
                    Income
                  </Button>
                  <Button variant='outline-primary'
                          onClick={() => setKind(OVERVIEW_KIND_PRODUCTS)}
                          active={kind === OVERVIEW_KIND_PRODUCTS}>
                    Products
                  </Button>
                  <Button variant='outline-primary'
                          onClick={() => setKind(OVERVIEW_KIND_CATEGORIES)}
                          active={kind === OVERVIEW_KIND_CATEGORIES}>
                    Categories
                  </Button>
                  <Button variant='outline-primary'
                          onClick={() => setKind(OVERVIEW_KIND_ORDERS)}
                          active={kind === OVERVIEW_KIND_ORDERS}>
                    Orders
                  </Button>
                </ButtonGroup>
            </Col>
            <Col xs={9}>
              <Overview from={getDateForAPI(from)} to={getDateForAPI(to)} key={`statistics-${period}`}/>
            </Col>
          </Row>
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
