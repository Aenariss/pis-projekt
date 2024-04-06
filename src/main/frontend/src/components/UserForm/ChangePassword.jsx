import {Button, Form, Modal} from "react-bootstrap";

export default function ChangePassword() {
  return (
    <Modal>
      <Modal.Header closeButton>
        <Modal.Title>Password change</Modal.Title>
      </Modal.Header>
      <Form>
        <Modal.Body>
          <Form.Group>
            <Form.Label>Current password</Form.Label>
            <Form.Control type="password"/>
          </Form.Group>
          <Form.Group>
            <Form.Label>New password</Form.Label>
            <Form.Control type="password"/>
          </Form.Group>
          <Form.Group>
            <Form.Label>Retype the new password</Form.Label>
            <Form.Control type="password"/>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button type="submit">Change the password</Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}