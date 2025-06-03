import React, { useState, useEffect } from 'react';
import { Typography, Box, CircularProgress, List, ListItem, ListItemText } from '@mui/material';
import axios from 'axios';
import './TransferHistory.css';

interface TransferHistoryProps {
  userId?: number; // Optional - if not provided, will use logged in user
}

interface TransferItem {
  id: number;
  description: string;
  amount: number;
  date: string;
}

const TransferHistory: React.FC<TransferHistoryProps> = ({ userId }) => {
  const [history, setHistory] = useState<TransferItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTransferHistory = async () => {
      try {
        setLoading(true);
        
        // Replace with your actual API endpoint
        const response = await axios.get(
          userId 
            ? `/localhost:8080/api/transferencias/${userId}/history`
            : `/localhost:8080/api/transferencias/me/history`
        );
        
        // Check if response.data is an array
        if (response.data && Array.isArray(response.data)) {
          setHistory(response.data);
        } 
        // Check if response.data contains an array in a property
        else if (response.data && response.data.transfers && Array.isArray(response.data.transfers)) {
          setHistory(response.data.transfers);
        }
        // If no valid data, use mock data
        else {
          // Mock data for development - remove in production
          const mockData = [
            { id: 1, description: "This is a list item", amount: 50, date: "2023-06-01" },
            { id: 2, description: "Another list item", amount: 30, date: "2023-06-02" },
            { id: 3, description: "Yup, another list item", amount: 20, date: "2023-06-03" },
            { id: 4, description: "Another list item", amount: 100, date: "2023-06-04" },
            { id: 5, description: "This is a list item", amount: 45, date: "2023-06-05" },
            { id: 6, description: "Yup, another list item", amount: 75, date: "2023-06-06" },
            { id: 7, description: "This is a list item", amount: 60, date: "2023-06-07" },
            { id: 8, description: "Another list item", amount: 90, date: "2023-06-08" },
            { id: 9, description: "This is a list item", amount: 25, date: "2023-06-09" }
          ];
          setHistory(mockData);
          console.warn('Using mock transfer history data');
        }
        
        setError(null);
      } catch (err) {
        console.error('Failed to fetch transfer history:', err);
        setError('Failed to load transfer history');
        // Mock data for development - remove in production
        const mockData = [
          { id: 1, description: "This is a list item", amount: 50, date: "2023-06-01" },
          { id: 2, description: "Another list item", amount: 30, date: "2023-06-02" },
          { id: 3, description: "Yup, another list item", amount: 20, date: "2023-06-03" },
          { id: 4, description: "Another list item", amount: 100, date: "2023-06-04" },
          { id: 5, description: "This is a list item", amount: 45, date: "2023-06-05" },
          { id: 6, description: "Yup, another list item", amount: 75, date: "2023-06-06" },
          { id: 7, description: "This is a list item", amount: 60, date: "2023-06-07" },
          { id: 8, description: "Another list item", amount: 90, date: "2023-06-08" },
          { id: 9, description: "This is a list item", amount: 25, date: "2023-06-09" }
        ];
        setHistory(mockData);
      } finally {
        setLoading(false);
      }
    };

    fetchTransferHistory();
  }, [userId]);

  return (
    <Box className="transfer-history-container">
      <Typography variant="h4" className="history-title">
        Transfer History
      </Typography>
      
      {loading ? (
        <CircularProgress size={24} />
      ) : error ? (
        <Typography color="error">{error}</Typography>
      ) : (
        <List className="history-list">
          {Array.isArray(history) ? (
            history.map((item, index) => (
              <ListItem key={item.id} className="history-item">
                <Typography className="item-number">{index + 1}</Typography>
                <ListItemText primary={item.description} className="item-text" />
              </ListItem>
            ))
          ) : (
            <Typography>No transfer history available</Typography>
          )}
        </List>
      )}
    </Box>
  );
};

export default TransferHistory;