import { useAuth } from "../../context/AuthContext";
import { Navigate } from "react-router-dom";

export default function EmployerDashboard() {
  const { user } = useAuth();

  if (!user || user.role !== "EMPLOYER") {
    return <Navigate to="/login" replace />;
  }

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-4">
        Employer Dashboard
      </h1>
    </div>
  );
}
