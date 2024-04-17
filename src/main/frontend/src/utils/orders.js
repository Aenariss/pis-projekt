/**
 * Helper functions for orders.
 * @author Lukas Petr
 */

/**
 * Returns string describing status of order.
 * @param status Status of the order.
 */
export function orderStateToString(status) {
  switch(status) {
    case 'IN_PROGRESS': return 'ordered';
    case 'CONFIRMED': return 'processing';
    case 'PACKED': return 'ready to ship';
    case 'SHIPPED': return 'sent';
    case 'DELIVERED': return 'delivered';
    case 'CANCELED': return 'canceled';
    case 'RETURNED': return 'returned';
    default: return '';
  }
}