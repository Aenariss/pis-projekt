/**
 * Form for filling address.
 * @author Lukas Petr <xpetrl06>
 */

import { Col, Form, Row } from "react-bootstrap";

/**
 * Form component for filling address.
 * @param props Component props.
 * @param {String} props.state Name of state.
 * @param {String} props.town Name of town.
 * @param {String} props.postcode Post code number.
 * @param {String} props.street Name of street.
 * @param {String} props.streetNumber Number of house in the street.
 * @param {Function} props.onStateChange Handler for state change.
 * @param {Function} props.onTownChange Handler for town change.
 * @param {Function} props.onPostcodeChange Handler for postcode change.
 * @param {Function} props.onStreetChange Handler for street change.
 * @param {Function} props.onStreetNumberChange Handler for street number change.
 * @returns {JSX.Element} - Address component
 * @constructor Address
 */
export default function Address({
  state,
  town,
  postcode,
  street,
  streetNumber,
  onStateChange,
  onTownChange,
  onPostcodeChange,
  onStreetChange,
  onStreetNumberChange,
}) {
  return (
    <>
      <Row>
        <Form.Group as={Col}>
          <Form.Label htmlFor='state'>State:</Form.Label>
          <Form.Control id='state'
                        type='text'
                        value={state}
                        onChange={e => onStateChange(e.target.value)}/>
        </Form.Group>
      </Row>
      <Row>
        <Form.Group as={Col}>
          <Form.Label htmlFor='town'>Town:</Form.Label>
          <Form.Control id='town'
                        type='text'
                        value={town}
                        onChange={e => onTownChange(e.target.value)} />
        </Form.Group>
        <Form.Group as={Col}>
          <Form.Label htmlFor='postcode'>Postcode:</Form.Label>
          <Form.Control id='postcode'
                        type='text'
                        value={postcode}
                        maxLength={8}
                        onChange={(e) => onPostcodeChange(e.target.value)} />
        </Form.Group>
      </Row>
      <Row>
        <Form.Group as={Col}>
          <Form.Label htmlFor='street'>Street:</Form.Label>
          <Form.Control id='street'
                        type='text'
                        value={street}
                        onChange={e => onStreetChange(e.target.value)}/>
        </Form.Group>
        <Form.Group as={Col}>
          <Form.Label htmlFor='street-num'>Street number:</Form.Label>
          <Form.Control id='street-num'
                        type='text'
                        value={streetNumber}
                        onChange={e => onStreetNumberChange(e.target.value)}/>
        </Form.Group>
      </Row>
    </>
  );
}