import './App.css';
import { RouterProvider } from "react-router-dom";
import rootRouter from "./common/router/rootRouter";
import { useEffect } from 'react';
import { UserProvider } from "./common/context/BasicContext";

const App = () => {
    useEffect(() => {
        document.body.classList.add('px-96', 'py-0', 'bg-white');
    })
    return (
        <UserProvider>

            <RouterProvider router={rootRouter}
                future={{
                    v7_startTransition: true,
                }}
            />


        </UserProvider>
    );
};

export default App;