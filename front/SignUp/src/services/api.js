import axios from 'axios';
import API_ENDPOINTS from '../config/api';

const api = axios.create({
  baseURL: API_ENDPOINTS.BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar o token de autenticação
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Serviços de Autenticação
export const authService = {
  login: async (credentials) => {
    const response = await api.post(API_ENDPOINTS.LOGIN, credentials);
    return response.data;
  },
  forgotPassword: async (email) => {
    const response = await api.post(API_ENDPOINTS.FORGOT_PASSWORD, { email });
    return response.data;
  },
  resetPassword: async (resetData) => {
    const response = await api.post(API_ENDPOINTS.RESET_PASSWORD, resetData);
    return response.data;
  },
};

// Serviços de Registro
export const registrationService = {
  registerStudent: async (studentData) => {
    const response = await api.post(API_ENDPOINTS.REGISTER_STUDENT, studentData);
    return response.data;
  },
  registerProfessor: async (professorData) => {
    const response = await api.post(API_ENDPOINTS.REGISTER_PROFESSOR, professorData);
    return response.data;
  },
  registerCompany: async (companyData) => {
    const response = await api.post(API_ENDPOINTS.REGISTER_COMPANY, companyData);
    return response.data;
  },
};

// Serviços de Perfil
export const profileService = {
  getStudentProfile: async (id) => {
    const response = await api.get(API_ENDPOINTS.STUDENT_PROFILE(id));
    return response.data;
  },
  getProfessorProfile: async (id) => {
    const response = await api.get(API_ENDPOINTS.PROFESSOR_PROFILE(id));
    return response.data;
  },
  getCompanyProfile: async (id) => {
    const response = await api.get(API_ENDPOINTS.COMPANY_PROFILE(id));
    return response.data;
  },
};

// Serviços de Transações
export const transactionService = {
  getStudentTransactions: async (id) => {
    const response = await api.get(API_ENDPOINTS.STUDENT_TRANSACTIONS(id));
    return response.data;
  },
  getProfessorTransactions: async (id) => {
    const response = await api.get(API_ENDPOINTS.PROFESSOR_TRANSACTIONS(id));
    return response.data;
  },
  getCompanyTransactions: async (id) => {
    const response = await api.get(API_ENDPOINTS.COMPANY_TRANSACTIONS(id));
    return response.data;
  },
};

// Serviços de Moedas
export const coinService = {
  transferCoins: async (professorId, transferData) => {
    const response = await api.post(API_ENDPOINTS.TRANSFER_COINS(professorId), transferData);
    return response.data;
  },
  redeemAdvantage: async (studentId, redeemData) => {
    const response = await api.post(API_ENDPOINTS.REDEEM_ADVANTAGE(studentId), redeemData);
    return response.data;
  },
  verifyRedemption: async (code) => {
    const response = await api.post(API_ENDPOINTS.VERIFY_REDEMPTION, { code });
    return response.data;
  },
};

// Serviços de Vantagens
export const advantageService = {
  getAllAdvantages: async () => {
    const response = await api.get(API_ENDPOINTS.ADVANTAGES);
    return response.data;
  },
  createAdvantage: async (advantageData) => {
    const response = await api.post(API_ENDPOINTS.ADVANTAGES, advantageData);
    return response.data;
  },
};

// Serviços de Instituições
export const institutionService = {
  getAllInstitutions: async () => {
    const response = await api.get(API_ENDPOINTS.INSTITUTIONS);
    return response.data;
  },
};

export default {
  authService,
  registrationService,
  profileService,
  transactionService,
  coinService,
  advantageService,
  institutionService,
}; 