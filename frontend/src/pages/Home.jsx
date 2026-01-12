import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-6xl mx-auto py-20 text-center">
        <h1 className="text-4xl font-bold mb-4">
          Find Your Next Job Opportunity
        </h1>

        <p className="text-gray-600 mb-8">
          Browse jobs from companies, shops, and individuals.
        </p>

        <div className="flex justify-center gap-4">
          <Link
            to="/jobs"
            className="bg-blue-600 text-white px-6 py-3 rounded"
          >
            Browse Jobs
          </Link>

          <Link
            to="/register"
            className="border border-blue-600 text-blue-600 px-6 py-3 rounded"
          >
            Create Account
          </Link>
        </div>
      </div>
    </div>
  );
}
