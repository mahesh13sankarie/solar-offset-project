import React, { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authState, setAuthState] = useState({
        isAuthenticated: false,
        userId: null,
        token: null,
        loading: true,
    });

    // Initialize auth state from localStorage on mount
    useEffect(() => {
        const token = localStorage.getItem("token");
        const userId = localStorage.getItem("userId");

        if (token && userId) {
            setAuthState({
                isAuthenticated: true,
                userId,
                token,
                loading: false,
            });
        } else {
            setAuthState({
                isAuthenticated: false,
                userId: null,
                token: null,
                loading: false,
            });
        }
    }, []);

    // Login function
    const login = (userData) => {
        const { token, userId } = userData;

        localStorage.setItem("token", token);
        localStorage.setItem("userId", userId);

        setAuthState({
            isAuthenticated: true,
            userId,
            token,
            loading: false,
        });
    };

    // Logout function
    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");

        setAuthState({
            isAuthenticated: false,
            userId: null,
            token: null,
            loading: false,
        });
    };

    return (
        <AuthContext.Provider
            value={{
                ...authState,
                login,
                logout,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

// Custom hook for using auth context
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
};
