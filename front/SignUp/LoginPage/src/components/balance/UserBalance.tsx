import { Typography, Box, CircularProgress } from '@mui/material';
import { useState, useEffect } from 'react';
import axios from 'axios';
import './UserBalance.css';

interface UserBalanceProps {
  userId?: number; // Optional - if not provided, will use logged in user
}

const UserBalance: React.FC<UserBalanceProps> = ({ userId }) => {
  const [balance, setBalance] = useState<number | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchBalance = async () => {
      try {
        setLoading(true);
        
        // Replace with your actual API endpoint
        const response = await axios.get(
          userId 
            ? `/localhost:8080/api/pessoas/${userId}/balance`
            : `/localhost:8080/api/pessoas/me/balance`
        );
        
        if (response.data && (response.data.balance !== undefined || response.data.saldo !== undefined)) {
          setBalance(response.data.balance || response.data.saldo);
        } else {
          // Mock data for development - remove in production
          setBalance(0);
          console.warn('Using mock balance data');
        }
        
        setError(null);
      } catch (err) {
        console.error('Failed to fetch balance:', err);
        setError('Failed to load balance');
        // Mock data for development - remove in production
        setBalance(500);
      } finally {
        setLoading(false);
      }
    };

    fetchBalance();
  }, [userId]);

  return (
    <Box className="user-balance-container">
      {loading ? (
        <CircularProgress size={24} />
      ) : error ? (
        <Typography color="error">{error}</Typography>
      ) : (
        <>
          <Typography variant="h3" component="h1" className="balance-amount">
            M$ {balance?.toLocaleString()}
          </Typography>
          <Typography variant="subtitle1" className="balance-label">
            Your balance
          </Typography>
        </>
      )}
    </Box>
  );
};

export default UserBalance;