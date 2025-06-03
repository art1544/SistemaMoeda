import React, { useState, useEffect } from 'react';
import { Typography, Button, Box, CircularProgress, Pagination } from '@mui/material';
import axios from 'axios';
import './Rewards.css';

interface Reward {
  id: number;
  title: string;
  description: string;
  price: number;
}

const Rewards: React.FC = () => {
  const [rewards, setRewards] = useState<Reward[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(1);
  const rewardsPerPage = 3;

  useEffect(() => {
    const fetchRewards = async () => {
      try {
        setLoading(true);
        
        // Replace with your actual API endpoint
        const response = await axios.get('/localhost:8080/api/vantagens');
        
        if (response.data && Array.isArray(response.data)) {
          setRewards(response.data);
        } else if (response.data && response.data.vantagens && Array.isArray(response.data.vantagens)) {
          // If API returns data inside a "vantagens" property
          setRewards(response.data.vantagens);
        } else {
          // Mock data for development - remove in production
          const mockData = [
            { id: 1, title: 'Lorem ipsum dolor sit amet, consectetur', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 1000 },
            { id: 2, title: 'Lorem ipsum dolor sit amet, consectetur', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 2000 },
            { id: 3, title: 'Lorem ipsum dolor sit amet, consectetur', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 700 },
            { id: 4, title: 'Another reward option', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 1500 },
            { id: 5, title: 'Special discount', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 3000 },
          ];
          setRewards(mockData);
          console.warn('Using mock reward data');
        }
      } catch (err) {
        console.error('Failed to fetch rewards:', err);
        setError('Failed to load rewards');
        
        // Mock data for development - remove in production
        const mockData = [
          { id: 1, title: 'Lorem ipsum dolor sit amet, consectetur', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 1000 },
          { id: 2, title: 'Lorem ipsum dolor sit amet, consectetur', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 2000 },
          { id: 3, title: 'Lorem ipsum dolor sit amet, consectetur', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vestibulum mauris ut diam vulputate, nec scelerisque magna maximus.', price: 700 },
        ];
        setRewards(mockData);
      } finally {
        setLoading(false);
      }
    };

    fetchRewards();
  }, []);

  // Calculate current page's rewards with safety checks
  const indexOfLastReward = page * rewardsPerPage;
  const indexOfFirstReward = indexOfLastReward - rewardsPerPage;
  const currentRewards = Array.isArray(rewards) ? rewards.slice(indexOfFirstReward, indexOfLastReward) : [];
  
  // Change page
  const handlePageChange = (_event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };

  const handleClaimReward = (rewardId: number) => {
    console.log(`Claiming reward with ID: ${rewardId}`);
    // Add your API call to claim the reward
  };

  return (
    <div className="rewards-container">
      <Typography variant="h4" className="rewards-title">
        Claim Rewards
      </Typography>
      
      {loading ? (
        <Box display="flex" justifyContent="center" padding={4}>
          <CircularProgress />
        </Box>
      ) : error ? (
        <Typography color="error" align="center">{error}</Typography>
      ) : rewards.length === 0 ? (
        <Typography align="center">No rewards available</Typography>
      ) : (
        <>
          <div className="rewards-list">
            {currentRewards.map((reward) => (
              <div key={reward.id} className="reward-card">
                <div className="reward-image-placeholder"></div>
                <Typography variant="h6" className="reward-title">
                  {reward.title}
                </Typography>
                <Typography variant="body2" className="reward-description">
                  {reward.description}
                </Typography>
                <Typography variant="h5" className="reward-price">
                  M$ {reward.price}
                </Typography>
                <Button 
                  variant="contained" 
                  className="claim-button"
                  onClick={() => handleClaimReward(reward.id)}
                >
                  Button
                </Button>
              </div>
            ))}
          </div>
          
          {/* Only show pagination if we have more than one page */}
          {rewards.length > rewardsPerPage && (
            <Box display="flex" justifyContent="center" marginTop={4}>
              <Pagination 
                count={Math.ceil(rewards.length / rewardsPerPage)} 
                page={page} 
                onChange={handlePageChange} 
                color="primary" 
              />
            </Box>
          )}
        </>
      )}
    </div>
  );
};

export default Rewards;