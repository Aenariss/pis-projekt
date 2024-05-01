/**
 * Chart containing income gain from orders in time.
 * @author Lukas Petr
 */
import { useEffect, useState } from 'react';
import { CartesianGrid, XAxis, YAxis, Tooltip, LineChart, Line } from 'recharts';
import { api } from '../../api';

/**
 * Chart containing income gain from orders in time.
 * @param props Component props.
 * @param props.from From date.
 * @param props.to To date.
 * @returns {JSX.Element} - IncomeInTimeChart component
 * @constructor IncomeInTimeChart
 */
export default function IncomeInTimeChart({from, to}) {
    const [statistics, setStatistics] = useState(null);

    useEffect(() => {
      const data = {
        fromDate: from,
        toDate: to,
      }
      api.post('/statistics/incomePerTime', data)
        .then((response) => {
          const data = response.data;
          // transforming date to javascript date
          data.perDay.forEach(item => {
            item.date = new Date(Date.parse(item.date.slice(0, item.date.length - 1)))
          });
          // sorting by date
          data?.perDay?.sort((i1, i2) => i1.date - i2.date)
          // converting to locale date string
          data.perDay.forEach(item => {
            item.date = item.date.toLocaleDateString();
          });
          setStatistics(response.data);
        })
        .catch(() => alert('Error: it was not possible to retrieve statistics!'));
    }, [from, to]);

    if (statistics === null) return null;
    return (
      <div>
          <h3>Income in time</h3>
          <LineChart data={statistics.perDay}
                    height={500} width={800}
                    barCategoryGap={15}
                    margin={{
                      top: 20,
                      right: 80,
                      bottom: 20,
                      left: 50,
                    }}>
            <CartesianGrid strokeDasharray='3 3'/>
            <XAxis dataKey='date' label={{ value: 'Date', position: 'insideBottom', offset: -10}} />
            <YAxis label={{ value: 'Dollars earned', angle: -90, position: 'insideLeft'}} allowDecimals={false}/>
            <Tooltip formatter={(value) => [`${value}`, 'Dollars earned']}/>
            <Line dataKey='occurence'
                  type='monotone'
                  stroke='#8884d8'
                  activeDot={{ r: 8 }}
                  strokeWidth={3}
                  dot={{ strokeWidth: 3}}/>
          </LineChart>
          <div style={{fontSize: '20px'}}>Total earnings in given time range: {statistics.total} $</div>
      </div>
    );
  };