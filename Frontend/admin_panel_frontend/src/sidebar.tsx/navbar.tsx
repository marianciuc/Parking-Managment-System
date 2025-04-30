"use client"

import {Bell as NotificationsIcon, LogOut as LogOutIcon, User as UserIcon} from "lucide-react";
import React, {useContext, useEffect} from "react";
import {Context} from "@/providers/ClientContextProvider";
import {AdministratorDomain} from "@/interfaces";
import {toast} from "sonner";

const Navbar = () => {
    const [user, setUser] = React.useState<AdministratorDomain | null>(null);
    const {userStore} = useContext(Context);

    useEffect(() => {
        const fetchUser = async () => {
            userStore.fetchAuthenticatedUser().then((message) => {
                if (message) {
                    toast.error("Error! You have entered incorrect login or password.", {description: message.message});
                } else {
                    setUser(userStore.authenticatedUser)
                }
            })
        }
        if (user == null) fetchUser();
    }, []);

    if (user == null) return null;

    return <header className="bg-white shadow p-4 flex items-center justify-between">
        <div className="text-lg font-bold">Панель управления</div>
        <div className="flex items-center gap-6">
            {/* Уведомления */}
            <button
                className="relative text-gray-600 hover:text-gray-800 focus:outline-none"
                aria-label="Уведомления"
            >
                <NotificationsIcon className="w-6 h-6" />
                <span className="absolute top-0 right-0 inline-block w-2 h-2 transform translate-x-1/2 -translate-y-1/2 bg-red-500 rounded-full"></span>
            </button>

            {/* Информация о пользователе */}
            <div className="flex items-center gap-4">
                <div className="flex flex-col text-right">
                    <span className="text-sm font-medium text-gray-700">{user.firstname} {user.lastname}</span>
                    <span className="text-xs text-gray-500">{user.id}</span>
                </div>
                <UserIcon className="w-8 h-8 text-gray-600" />
            </div>

            {/* Выход */}
            <button
                className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-red-500 rounded hover:bg-red-600"
                // onClick={() => {
                //     console.log("Выход");
                // }}
            >
                <LogOutIcon className="w-4 h-4" />
                Выйти
            </button>
        </div>
    </header>
}

export default Navbar;