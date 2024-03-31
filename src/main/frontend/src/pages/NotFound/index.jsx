/**
 * Page which will be shown when specified url is not valid.
 * @author Lukas Petr
 */

import {Alert} from "react-bootstrap";

export default function NotFoundPage() {
  return <Alert variant="danger">Page was not found.</Alert>
}