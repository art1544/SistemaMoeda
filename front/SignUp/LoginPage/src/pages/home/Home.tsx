import React from "react";
import UserBalance from "../../components/balance/UserBalance";
import Navbar from "../../components/navbar/Navbar";
import NewTransfer from "../../components/transfer/NewTransfer";
import TransferHistory from "../../components/transfer/TranferHistory";
import "./Home.css";

const Home: React.FC = () => {
    return (
        <div>
            <Navbar />
            <div className="home-container with-navbar">
                <div className="balance-section">
                    <UserBalance />
                </div>
                <div className="main-content">
                    <div className="transfer-section">
                        <NewTransfer />
                    </div>
                    <div className="history-section">
                        <TransferHistory />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Home;