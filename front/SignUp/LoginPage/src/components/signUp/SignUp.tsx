import { Button, Container, FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, TextField, Typography } from "@mui/material";
import React, { ChangeEvent, useState } from "react";
import { UserRegisterService } from "../../api/Service";


export interface UserRegisterProps {
    name: string;
    email: string;
    cpf: string;
    password: string;
    tipo: string;
}
const SignUp: React.FC = () => {
    const [userRegister, setUserRegister] = useState<UserRegisterProps>({ name: "", email: "", cpf: "", password: "", tipo: "professor" })
    const { postRegister } = UserRegisterService();
    const OnSubmitHandler = () => {
        console.log(userRegister)
        postRegister(userRegister)
    }
    const handleChange = (event: ChangeEvent<Omit<HTMLInputElement, "value"> & { value: string; }> | (Event & { target: { value: string; name: string; }; })) => {
        console.log(event)
        setUserRegister((prev) => ({
            ...prev,
            tipo: event.target.value
        }));
    };

    return (
        <Container className="form-signin">
            <FormControl style={{ backgroundColor: "white", padding: "4rem", borderRadius: "40px" }}>
                <Typography variant="h4" gutterBottom align="center"> Registrar-se</Typography>
                <TextField
                    required
                    type="text"
                    id="outlined-required"
                    label="Nome"
                    value={userRegister.name}
                    onChange={event => setUserRegister((prev) => ({
                        ...prev,
                        name: event.target.value
                    }))}
                /><br />
                <TextField
                    required
                    type="text"
                    id="outlined-required"
                    label="email"
                    value={userRegister.email}
                    onChange={event => setUserRegister((prev) => ({
                        ...prev,
                        email: event.target.value
                    }))}
                /><br />
                <TextField
                    required
                    type="text"
                    id="outlined-required"
                    label="cpf"
                    value={userRegister.cpf}
                    onChange={event => setUserRegister((prev) => ({
                        ...prev,
                        cpf: event.target.value
                    }))}
                /><br />
                <TextField
                    required
                    type="text"
                    id="outlined-required"
                    label="Senha"
                    value={userRegister.password}
                    onChange={event => setUserRegister((prev) => ({
                        ...prev,
                        password: event.target.value
                    }))}
                /><br />
                <Select
                    labelId="Tipo"
                    id="Tipo"
                    value={userRegister.tipo}
                    label="Tipo"
                    onChange={(event) => handleChange(event)}
                >
                    <MenuItem value={"professor"}>Professor</MenuItem>
                    <MenuItem value={"aluno"}>Aluno</MenuItem>
                </Select>
                <br />
                <Button color="secondary"
                    type="button"
                    onClick={() => OnSubmitHandler()}
                    disabled={!userRegister.cpf || !userRegister.name || !userRegister.email || !userRegister.password}
                >Registrar
                </Button>
            </FormControl>
        </Container>
    )
}
export default SignUp
