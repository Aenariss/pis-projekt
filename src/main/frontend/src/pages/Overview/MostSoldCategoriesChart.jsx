/**
 * Chart containing numbers of sold items by categories.
 * @author Lukas Petr
 */
import { useEffect, useState } from 'react';
import { BarChart, CartesianGrid, XAxis, YAxis, Bar, Tooltip } from 'recharts';
import { api } from '../../api';

/**
 * Chart containing numbers of sold items by categories.
 * @param props.from From date.
 * @param props.to To date.
 */
export default function MostSoldCategoriesChart({from, to}) {
    const [statistics, setStatistics] = useState(null);

    useEffect(() => {
      const data = {
        fromDate: from,
        toDate: to,
      }
      api.post('/statistics/mostSoldCategories', data)
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
          <h3>Sold books by categories</h3>
          <BarChart data={statistics.perDay}
                    height={500} width={800}
                    layout='vertical'
                    barCategoryGap={15}
                    margin={{
                      top: 20,
                      right: 80,
                      bottom: 20,
                      left: 50,
                    }}>
            <CartesianGrid />
            <XAxis type='number' label={{ value: 'Books bought', position: 'insideBottom', offset: -10}}/>
            <YAxis type='category' dataKey='name'
                   label={{ value: 'Category', angle: -90, position: 'insideLeft', offset: -30}}
                   width={120}/>
            <Tooltip formatter={(value) => [`${value}x`, 'Bought']}/>
            <Bar dataKey='occurence' fill='#8884d8' />
          </BarChart>
      </div>
    );
  };