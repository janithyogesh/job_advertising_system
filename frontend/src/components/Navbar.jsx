import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="bg-white shadow px-6 py-4 flex justify-between items-center">
      {/* LEFT */}
      <Link to="/" className="text-xl font-bold text-blue-600">
        JobPortal
      </Link>

      {/* RIGHT */}
      <div className="flex items-center gap-6">
        {/* NOT LOGGED IN */}
        {!user && (
          <>
            <Link
              to="/login"
              className="text-gray-600 hover:text-blue-600"
            >
              Login
            </Link>

            <Link
              to="/register"
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            >
              Register
            </Link>
          </>
        )}

        {/* LOGGED IN */}
        {user && (
          <>
            {/* ROLE BADGE */}
            <span className="text-sm px-3 py-1 rounded-full bg-gray-100 text-gray-700">
              {user.role === "EMPLOYER" ? "Employer" : "Job Seeker"}
            </span>

            {/* JOBS LINK */}
            <Link
              to="/jobs"
              className="text-gray-600 hover:text-blue-600"
            >
              {user.role === "EMPLOYER" ? "Manage Jobs" : "Find Jobs"}
            </Link>

            {/* DASHBOARD */}
            <Link
              to="/dashboard"
              className="text-gray-600 hover:text-blue-600"
            >
              Dashboard
            </Link>

            {/* LOGOUT */}
            <button
              onClick={handleLogout}
              className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
            >
              Logout
            </button>
          </>
        )}
      </div>
    </nav>
  );
}
