import { Button, FormControl, MenuItem, Select, TextField, Typography } from "@mui/material";
import { useState, useEffect } from "react";
import { UserTransferList } from "../../api/Service";
import "./NewTransfer.css";

const NewTransfer: React.FC = () => {
    const [student, setStudent] = useState("");
    const [reason, setReason] = useState("");
    const [amount, setAmount] = useState("");
    const [studentList, setStudentList] = useState<any[]>([]);
    const {getUsers} = UserTransferList()
    const {user} = getUsers()
   
    
    // Fetch users on component mount
    useEffect(() => {
        try {
            const response = getUsers();
            // Check if the response has users or data property
            if (response.user) {
                setStudentList(response.user);
            } else if (response.user) {
                setStudentList(response.user);
            } else {
                console.error("Unexpected API response format", response);
            }
        } catch (error) {
            console.error("Error fetching students:", error);
        }
    }, []);
    
    // Reasons for transfer
    const reasons = [
        { id: 1, label: "Bom comportamento" },
        { id: 2, label: "Notas Boas" },
        { id: 3, label: "Amizade" },
        { id: 4, label: "Otimo aluno" }
    ];

    const handleTransfer = () => {
        // Handle transfer logic
        console.log({ student, reason, amount });
    };

    return (
        <div className="new-transfer-container">
            <Typography variant="h4" className="transfer-heading">
                Make New Transfer
            </Typography>
            
            <div className="transfer-form">
                <div className="select-row">
                    <FormControl className="select-field">
                        <Select
                            displayEmpty
                            value={student}
                            onChange={(e) => setStudent(e.target.value)}
                            renderValue={(selected) => {
                                if (selected === "") {
                                    return <em>Enviar para quem?</em>;
                                }
                                return selected;
                            }}
                        >
                            {studentList.map((user) => (
                                <MenuItem key={user.id} value={user.name || user.nome}>
                                    {user.name || user.nome}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    
                    <FormControl className="select-field">
                        <Select
                            displayEmpty
                            value={reason}
                            onChange={(e) => setReason(e.target.value)}
                            renderValue={(selected) => {
                                if (selected === "") {
                                    return <em>Motivo</em>;
                                }
                                return selected;
                            }}
                        >
                            {reasons.map((option) => (
                                <MenuItem key={option.id} value={option.label}>
                                    {option.label}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </div>
                
                <TextField
                    className="amount-field"
                    placeholder="quantidade"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                />
                
                <Button 
                    variant="contained" 
                    className="transfer-button"
                    onClick={handleTransfer}
                >
                    Tranferir
                </Button>
            </div>
        </div>
    );
};

export default NewTransfer;
