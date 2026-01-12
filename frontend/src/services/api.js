import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

// ✅ Attach token ONLY for protected routes
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  // ❌ Do NOT attach token for auth routes
  if (
    token &&
    !config.url.includes("/auth/login") &&
    !config.url.includes("/auth/register")
  ) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default api;
