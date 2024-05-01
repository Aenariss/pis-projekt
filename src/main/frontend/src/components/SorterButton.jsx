/**
 * Button for sorting.
 * @author Lukas Petr
 */
import { Stack } from 'react-bootstrap';
import { CaretDownFill, CaretUpFill } from 'react-bootstrap-icons';

/**
 * Button for sorting.
 * @param props Component props.
 * @param {Function} props.onSortChange Called when user changes the way
 * how things should be sorted - called with true or false depending where user clicks.
 * @returns {JSX.Element} - SorterButtons component
 * @constructor SorterButtons
 */
export default function SorterButtons({
    onSortChange,
  }) {
    return (
        <Stack>
          <CaretUpFill size='12px'
                       onClick={() => onSortChange(true)}
                       role='button'/>
          <CaretDownFill size='12px'
                         onClick={() => onSortChange(false)}
                         role='button'/>
        </Stack>
    );
  }