import React, {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";
import { apiPost, setAuthToken } from "../api.js";

const AuthContext = createContext(null);

const AUTH_STORAGE_KEY = "hms_auth";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const raw = localStorage.getItem(AUTH_STORAGE_KEY);
    if (raw) {
      try {
        const parsed = JSON.parse(raw);
        setUser(parsed);
        // Set the auth token when loading from localStorage
        if (parsed.token) {
          setAuthToken(parsed.token);
        }
      } catch {
        localStorage.removeItem(AUTH_STORAGE_KEY);
      }
    }
  }, []);

  const login = async (username, password) => {
    const res = await apiPost("/api/auth/login", { username, password });
    if (res?.token) setAuthToken(res.token);
    const authUser = {
      role: res.role,
      username: res.username,
      token: res.token,
    };
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authUser));
    setUser(authUser);
    return authUser;
  };

  const logout = () => {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    setAuthToken(null);
    setUser(null);
  };

  const value = useMemo(() => ({ user, login, logout }), [user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}

export const Roles = {
  ADMIN: "ADMIN",
  DOCTOR: "DOCTOR",
  STAFF: "STAFF",
};
