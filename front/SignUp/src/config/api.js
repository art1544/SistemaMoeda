const API_BASE_URL = 'http://localhost:8080/api';

export const API_ENDPOINTS = {
  // Autenticação
  LOGIN: `${API_BASE_URL}/auth/login`,
  FORGOT_PASSWORD: `${API_BASE_URL}/auth/forgot-password`,
  RESET_PASSWORD: `${API_BASE_URL}/auth/reset-password`,

  // Registro
  REGISTER_STUDENT: `${API_BASE_URL}/register/student`,
  REGISTER_PROFESSOR: `${API_BASE_URL}/register/professor`,
  REGISTER_COMPANY: `${API_BASE_URL}/register/company`,

  // Perfil
  STUDENT_PROFILE: (id) => `${API_BASE_URL}/profile/student/${id}`,
  PROFESSOR_PROFILE: (id) => `${API_BASE_URL}/profile/professor/${id}`,
  COMPANY_PROFILE: (id) => `${API_BASE_URL}/profile/company/${id}`,

  // Transações
  STUDENT_TRANSACTIONS: (id) => `${API_BASE_URL}/profile/student/${id}/transactions`,
  PROFESSOR_TRANSACTIONS: (id) => `${API_BASE_URL}/profile/professor/${id}/transactions`,
  COMPANY_TRANSACTIONS: (id) => `${API_BASE_URL}/profile/company/${id}/transactions`,

  // Moedas
  TRANSFER_COINS: (professorId) => `${API_BASE_URL}/coins/transfer/professor/${professorId}`,
  REDEEM_ADVANTAGE: (studentId) => `${API_BASE_URL}/coins/redeem/student/${studentId}`,
  VERIFY_REDEMPTION: `${API_BASE_URL}/coins/verify-redemption`,

  // Vantagens
  ADVANTAGES: `${API_BASE_URL}/advantages`,

  // Instituições
  INSTITUTIONS: `${API_BASE_URL}/institutions`,
};

export default API_ENDPOINTS; 