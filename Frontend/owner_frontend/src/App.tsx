import './App.css'

import Login from "@/pages/Login.tsx";
import Sidebar from "@/components/sidebar/sidebar.tsx";

import MainPage from "@/pages/MainPage.tsx";
import {useContext, useEffect, useState} from "react";

import {Context} from "@/main.tsx";
import {observer} from "mobx-react-lite";
import {IUser} from "@/models/common"

import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import NotFoundPage from "@/pages/404notFound.tsx";
import CreateParking from "@/pages/CreateParking.tsx";
import Dashboard from "@/pages/Dashboard.tsx";
import Finances from "@/pages/Finances.tsx";
import WhiteAndBlackLists from "@/pages/WhiteAndBlackLists.tsx";
import Subscriptions from "@/pages/Subscriptions.tsx";
import ParkingSettings from "@/pages/ParkingSettings.tsx";
import Reviews from "@/pages/Reviews.tsx";
import Header from "@/components/header/header";
import Sessions from "@/pages/Sessions.tsx";
import Registration from "@/pages/Registration.tsx";
import ProfilePage from "@/pages/ProfilePage.tsx";
import {TooltipProvider} from "@/components/ui/tooltip.tsx";
import BigLoader from "@/components/ui/big-loader.tsx";
import AccountVerification from "@/components/dialogs/AccountVerification.tsx";
import AddNewBankAccount from "@/pages/AddNewBankAccount.tsx";


export const LayoutWithSidebar = () => (
    <div className="flex min-h-screen w-full">
        <TooltipProvider>
            <div className="fixed left-0 top-0 z-40">
                <Sidebar/>
            </div>

            <div
                className="fixed left-0 top-0 pl-[100px] w-full h-[92px] border-none shadow  opacity-100 z-30 bg-transparent">
                <Header/>
            </div>
            {/* Main Content */}
            <div className="container mx-auto flex items-center justify-center pl-[100px]"> {/* Adjust margin-left based on Sidebar width */}
                <Outlet/>
            </div>
            <span className="absolute text-gray-400 right-2 bottom-2 ">© 2025 HandyParking</span>

        </TooltipProvider>
    </div>
);


const router = createBrowserRouter([
    {
        path: '/',
        element: <LayoutWithSidebar/>,
        errorElement: <NotFoundPage/>,
        children: [
            {path: '/', element: <MainPage/>},
            {path: '/profile/*', element: <ProfilePage/>},
            {path: '/profile/verification', element: <AccountVerification/>},
            {path: '/profile/add-new-bank-account', element: <AddNewBankAccount/>},
            {path: '/finances/*', element: <Finances/>},
            {path: '/parking/create', element: <CreateParking/>},
            {path: '/parking/:parkingId/dashboard/*', element: <Dashboard/>},
            {path: '/finances', element: <Finances/>},
            {path: '/parking/:parkingId/access-list/*', element: <WhiteAndBlackLists/>},
            {path: '/parking/:parkingId/tariffs', element: <Subscriptions/>},
            {path: '/parking/:parkingId/settings/*', element: <ParkingSettings/>},
            {path: '/parking/:parkingId/reviews', element: <Reviews/>},
            {path: '/parking/:parkingId/sessions', element: <Sessions/>},
        ],
    },
]);


function App() {

    const {userStore} = useContext(Context);
    const [user, setUser] = useState<IUser>({} as IUser);

    useEffect(() => {
        console.log("Use effect call" + userStore.user)
        if (userStore.user && Object.keys(userStore.user).length > 0) return;
        userStore.validateAuthentication().then((isAuthenticated) => {
            console.log("auth " + isAuthenticated);
            if (isAuthenticated) {
                console.log(userStore)
                if (userStore.user && Object.keys(userStore.user).length > 0) {
                    setUser(userStore.user);
                }
            }
        });
    }, [userStore, userStore.user, userStore.isAuth]);


    if (userStore.isLoading) {
        return (<BigLoader/>)
    }

    if (!userStore.isAuth) {
        console.log("Not authenticated");
        return (
            <div className="w-[600px]">
                <Login/>
            </div>
        )
    }

    console.log(userStore.user)

    if (user.isRegistrationCompleted) {
        console.log(user);
        return (
            console.log(user),
                <div className="min-w-full min-h-full items-center justify-center flex ">
                    <RouterProvider router={router}/>
                </div>
        )
    }

    return (
        <div className="w-[600px]">
            <Registration/>
        </div>
    )
}

const ObserverApp = observer(App);

export default ObserverApp;
