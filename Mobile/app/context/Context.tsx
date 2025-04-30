import React, { createContext, useContext, useState, useEffect } from "react";
import UserStore from "@/app/store/UserStore";

const AuthContext = createContext<UserStore | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [userStore] = useState(new UserStore());

    useEffect(() => {
        const checkAuth = async () => {
            const isAuthenticated = await userStore.validateAuthentication();
            if (!isAuthenticated) {
                console.log("User is not authenticated");
            } else {
                console.log("User is authenticated");
            }
        };

        checkAuth();
    }, [userStore]);

    return (
        <AuthContext.Provider value={userStore}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};
