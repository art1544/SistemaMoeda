import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { Link } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar 
                position="fixed" 
                color="default" 
                sx={{ 
                    backgroundColor: 'white',
                    color: 'black',
                    boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.1)'
                }}
            >
                <Toolbar className="Navbar">
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{ mr: 2 }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        MoedaEstudantil
                    </Typography>
                    <Button color="inherit" component={Link} to="/">Home</Button>
                    <Button color="inherit" component={Link} to="/sobre">Sobre</Button>
                    <Button color="inherit" component={Link} to="/funcionalidades">Funcionalidades</Button> 
                    <Button color="inherit" component={Link} to="/vantagens">Vantagens</Button> 
                </Toolbar>
            </AppBar>
        </Box>
    );
}

export default Navbar
