import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="max-w-6xl mx-auto px-6 py-20 text-center">
        <h1 className="text-5xl font-extrabold text-gray-900 mb-6">
          Find Your Next Job 🚀
        </h1>

        <p className="text-xl text-gray-600 mb-10">
          Browse jobs, apply instantly, and track your application status.
          Employers can post jobs and manage applicants with ease.
        </p>

        <div className="flex justify-center gap-6">
          <Link
            to="/jobs"
            className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-4 rounded-xl text-lg font-semibold shadow"
          >
            Find Jobs
          </Link>

          <Link
            to="/register"
            className="bg-white border border-gray-300 hover:bg-gray-100 px-8 py-4 rounded-xl text-lg font-semibold shadow"
          >
            Post a Job
          </Link>
        </div>
      </div>
    </div>
  );
}
