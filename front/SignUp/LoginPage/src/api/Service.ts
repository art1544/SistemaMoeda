import axios from "axios";
import { UserLoginProps } from "../components/signIn/SignIn";
import { UserRegisterProps } from "../components/signUp/SignUp";


export const UserLoginService = () => {
    //aysnc  declaracao
    //await  
    const postLogin = async(user:UserLoginProps) => {
        const data = await axios.post<UserLoginProps,string>('/localhost:8080/api/pessoas/signin',{user})
        .then( (response) => {
            // manipula o sucesso da requisição
            console.log(response)
             return response
        })
        .catch(function (error) {
            // manipula erros da requisição
            console.error(error);
        })
        
        return data
    }
    return ({postLogin})
}

export const UserRegisterService = () => {
    const postRegister = async(user: UserRegisterProps) =>{
        const data = await axios.post<UserRegisterProps,string>('/localhost:8080/api/pessoas/signup',{user})
        .then((response) => {
            console.log(response)
            return response
        } )
        .catch(function(error){
            console.error(error)
        })
        return data
    }
    return({postRegister})
}

export const UserTransferList = () => {
    const getUsers = () =>{
        // const data = await axios.get('/localhost:8080/api/pessoas/lista')
        // .then((response) => {
        //     return  response
        // })
        // .catch(function(error){
        //     throw("erro ao carregar dados") 
        // })
        return{user:[{
            id: 1,
            name:"reinaldo",
            tipo:"professor",
            Balance:5000,
        }]}

    }
    return {getUsers}
}
