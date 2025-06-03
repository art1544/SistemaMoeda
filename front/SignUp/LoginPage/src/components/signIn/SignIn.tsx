import { Button, Container, FormControl, TextField, Typography } from "@mui/material";
import React, { useState } from "react";
import { UserLoginService } from "../../api/Service";


export interface UserLoginProps {
    name: string;
    password: string;
}

const SignIn: React.FC = () => {
    const [userLogin, setUser] = useState<UserLoginProps>({ name: "", password: "" })
    const { postLogin } = UserLoginService()

    const OnSubmitHandler = () => {
        console.log(userLogin)
        postLogin(userLogin)
    }

    return (
        <Container className="form-signin">
            <FormControl style={{ backgroundColor: "white", padding: "4rem", borderRadius: "50px"}}>
                <Typography variant="h4" gutterBottom align="center"> Entrar</Typography>
                <TextField
                    required={true}
                    id="outlined-required"
                    label="Nome"
                    type="text"
                    value={userLogin.name}
                    onChange={event => setUser((prev) => ({
                        ...prev,
                        name: event.target.value
                    }))}

                />
                <br />
                <TextField
                    required
                    type="password"
                    id="outlined-required"
                    label="Senha"
                    value={userLogin.password}
                    onChange={event => setUser((prev) => ({
                        ...prev,
                        password: event.target.value
                    }))}

                />
                <br />
                <Button color="secondary"
                    type="button"
                    onClick={() => OnSubmitHandler()}
                    disabled={!userLogin.name}
                >Entrar
                </Button>
                <Button>
                    <a className="signup" href="/signup">cadastrar</a>
                </Button>
            </FormControl>
        </Container>
    );
};

export default SignIn;