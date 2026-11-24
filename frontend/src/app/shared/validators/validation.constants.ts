export const ValidationPatterns = {

  EMAIL:
    /^[^\s@]+@[^\s@]+\.[^\s@]+$/,

  SOUTH_AFRICAN_PHONE:
    /^\+27[0-9]{9}$/,

  PASSWORD_COMPLEXITY:
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/

};