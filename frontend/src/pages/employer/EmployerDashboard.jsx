import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import api from "../../api/axios";
import AddJobModal from "./AddJobModal";

export default function EmployerDashboard() {
  const { user } = useAuth();

  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);

  if (!user || user.role !== "EMPLOYER") {
    return <Navigate to="/login" replace />;
  }

  useEffect(() => {
    fetchMyJobs();
  }, []);

  const fetchMyJobs = async () => {
    try {
      const res = await api.get("/employer/jobs");
      setJobs(res.data);
    } catch {
      alert("Failed to load jobs");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">
          Employer Dashboard
        </h1>

        <button
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
          onClick={() => setShowModal(true)}
        >
          + Add Job
        </button>
      </div>

      {loading ? (
        <p>Loading jobs...</p>
      ) : jobs.length === 0 ? (
        <p className="text-gray-500">
          You haven’t posted any jobs yet.
        </p>
      ) : (
        <div className="grid gap-4">
          {jobs.map((job) => (
            <div
              key={job.id}
              className="border rounded p-4 bg-white shadow-sm"
            >
              <h3 className="text-xl font-semibold">
                {job.title}
              </h3>

              <p className="text-gray-600">
                {job.company} • {job.location}
              </p>

              <p className="text-sm text-gray-500">
                Deadline:{" "}
                {new Date(job.deadline).toLocaleString()}
              </p>

              <span
                className={`inline-block mt-2 px-3 py-1 text-sm rounded-full ${
                  job.status === "OPEN"
                    ? "bg-green-100 text-green-700"
                    : "bg-red-100 text-red-700"
                }`}
              >
                {job.status}
              </span>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <AddJobModal
          onClose={() => setShowModal(false)}
          onJobAdded={fetchMyJobs}
        />
      )}
    </div>
  );
}
