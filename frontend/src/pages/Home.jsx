import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Home() {
  const { user } = useAuth();

  /**
   * Decide where "Post Job" should go
   */
  const getPostJobLink = () => {
    // ✅ Employer already logged in
    if (user && user.role === "EMPLOYER") {
      return "/employer/dashboard";
    }

    // ✅ Job seeker or not logged in
    // They must register as employer
    return "/register";
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 px-4">
      <h1 className="text-4xl font-bold text-gray-800 mb-4 text-center">
        Find or Post Your Next Job
      </h1>

      <p className="text-gray-600 mb-8 text-center max-w-xl">
        Browse thousands of job opportunities or post a job to find the perfect
        candidate. Get started in seconds.
      </p>

      <div className="flex flex-col sm:flex-row gap-4">
        {/* FIND JOBS — anyone can see */}
        <Link
          to="/jobs"
          className="px-8 py-3 bg-blue-600 text-white rounded-lg font-semibold text-center hover:bg-blue-700 transition"
        >
          Find Jobs
        </Link>

        {/* POST JOB — role aware */}
        <Link
          to={getPostJobLink()}
          className="px-8 py-3 bg-green-600 text-white rounded-lg font-semibold text-center hover:bg-green-700 transition"
        >
          Post Job
        </Link>
      </div>
    </div>
  );
}
