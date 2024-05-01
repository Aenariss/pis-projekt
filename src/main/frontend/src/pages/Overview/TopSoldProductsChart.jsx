/**
 * Chart component for showing top 20 sold products.
 * @author Lukas Petr
 */
import { useEffect, useState } from 'react';
import { BarChart, CartesianGrid, XAxis, YAxis, Bar, Tooltip } from 'recharts';
import { api } from '../../api';

/**
 * Chart component for showing top 20 sold products.
 * @param props Component props.
 * @param props.from From date.
 * @param props.to To date.
 * @returns {JSX.Element} - TopSoldProductsChart component
 * @constructor TopSoldProductsChart
 */
export default function TopSoldProductsChart({from, to}) {
    const [statistics, setStatistics] = useState(null);

    useEffect(() => {
      const data = {
        fromDate: from,
        toDate: to,
      }
      api.post('/statistics/mostSoldItems', data)
        .then((response) => {
          const data = response.data;
          data?.perDay?.sort((i1, i2) => i2.occurence - i1.occurence)
          setStatistics(response.data);
        })
        .catch(() => alert('Error: it was not possible to retrieve statistics!'));
    }, [from, to]);

    if (statistics === null) return null;
    return (
      <div>
          <h3>Top sold books</h3>
          <BarChart data={statistics.perDay}
                    height={800} width={1000}
                    layout='vertical'
                    barCategoryGap={15}
                    margin={{
                      top: 20,
                      right: 80,
                      bottom: 20,
                      left: 50,
                    }}>
            <CartesianGrid />
            <XAxis type='number' label={{ value: 'Books sold', position: 'insideBottom', offset: -10}} allowDecimals={false}/>
            <YAxis type='category' dataKey='name'
                   label={{ value: 'Title', angle: -90, position: 'insideLeft', offset: -30}}
                   width={300}/>
            <Tooltip formatter={(value) => [`${value}x`, 'Sold']}/>
            <Bar dataKey='occurence' fill='#8884d8' />
          </BarChart>
      </div>
    );
  };
