import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [isLoggedIn, setIsLoggedIn] = useState(
    Boolean(localStorage.getItem("accessToken"))
  );

function login(data) {
  const token = data?.accessToken || data?.accesToken;

  localStorage.setItem("accessToken", token);
  localStorage.setItem("gamblerId", data.gamblerId ?? data.id);
  localStorage.setItem("gamblerName", data.name);
  localStorage.setItem("gamblerEmail", data.email);
  localStorage.setItem("walletBalance", data.walletBalance);

  setIsLoggedIn(true);
}

function logout() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("gamblerId");
  localStorage.removeItem("gamblerName");
  localStorage.removeItem("gamblerEmail");
  localStorage.removeItem("walletBalance");

  setIsLoggedIn(false);
}

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}