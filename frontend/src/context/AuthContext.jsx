import { createContext, useContext, useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(
    localStorage.getItem("token")
  );

  useEffect(() => {
    if (!token) return;

    try {
      const decoded = jwtDecode(token);

      let role = null;

      // ✅ Case 1: role exists
      if (decoded.role) {
        role = decoded.role.replace("ROLE_", "");
      }

      // ✅ Case 2: authorities array
      if (!role && decoded.authorities?.length) {
        role = decoded.authorities[0].replace("ROLE_", "");
      }

      if (!role) {
        throw new Error("Role not found in token");
      }

      setUser({
        email: decoded.sub,
        role,
        name: decoded.fullName,
      });

      localStorage.setItem("token", token);
    } catch (err) {
      console.error("Invalid token", err);
      logout();
    }
  }, [token]);


  const login = (jwtToken) => {
    setToken(jwtToken);
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem("token");
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        login,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
