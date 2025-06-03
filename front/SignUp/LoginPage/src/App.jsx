import {
  createBrowserRouter,
  RouterProvider,
} from "react-router";

import React from "react";
import ReactDOM from "react-dom/client";
import SignUp from "./components/signUp/SignUp";
import SignIn from "./components/signIn/SignIn";
import Navbar from "./components/navbar/Navbar";
import Home from "./pages/home/Home";
import Advantage from "./pages/advantages/Advantage";

const router = createBrowserRouter([
  {
    path: "/home",
    element: <Home/>,
  },
   {
    path: "/signin",
    element: <SignIn/>,
  },
  {
    path: "/signup",
    element: <SignUp/>,
  },

  {path: "/vantagens",
    element: <Advantage/>
  },
  {path: "/n1",
    element: <br/>
  },

]);

const App = () =>{

  return(
    <main>
      <RouterProvider router={router} />
    </main>
  )
}

export default App