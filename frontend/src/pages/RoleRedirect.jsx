import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function RoleRedirect() {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (user.role === "EMPLOYER") {
    return <Navigate to="/employer/dashboard" replace />;
  }

  if (user.role === "JOB_SEEKER") {
    return <Navigate to="/jobseeker/dashboard" replace />;
  }

  // fallback safety
  return <Navigate to="/login" replace />;
}
