/**
 * Utility functions for layout.
 * @author Lukas Petr
 */

/**
 * Simple email validator - checks only that the email is in format xxx@xxx.xx
 * @param email Email adderss.
 * @return True if email is valid.
 */
export function isEmailValid(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

/**
 * Checks if the password is valid (has enough letters).
 * Does not check if it is correct.
 * @param password Password.
 * @returns {boolean} True if it valid.
 */
export function isPasswordValid(password) {
  return password.length >= 3;
}