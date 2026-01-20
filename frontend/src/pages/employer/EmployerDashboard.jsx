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
  const [selectedJobId, setSelectedJobId] = useState(null);
  const [applications, setApplications] = useState([]);
  const [loadingApplications, setLoadingApplications] = useState(false);

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

  const fetchApplications = async (jobId) => {
    try {
      setLoadingApplications(true);
      setSelectedJobId(jobId);
      const res = await api.get(`/employer/jobs/${jobId}/applications`);
      setApplications(res.data);
    } catch {
      alert("Failed to load applications");
    } finally {
      setLoadingApplications(false);
    }
  };

  const downloadCv = async (applicationId) => {
    try {
      const res = await api.get(
        `/employer/applications/${applicationId}/cv`,
        {
          responseType: "blob",
        }
      );

      const disposition = res.headers["content-disposition"];
      let fileName = `cv-${applicationId}`;

      if (disposition) {
        const match = disposition.match(/filename="(.+)"/);
        if (match?.[1]) {
          fileName = match[1];
        }
      }

      const blob = new Blob([res.data]);
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch {
      alert("Failed to download CV");
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

              <div className="mt-4">
                <button
                  className="text-blue-600 text-sm hover:underline"
                  onClick={() => fetchApplications(job.id)}
                >
                  View Applicants
                </button>
              </div>

              {selectedJobId === job.id && (
                <div className="mt-4 border-t pt-4">
                  {loadingApplications ? (
                    <p className="text-sm text-gray-500">
                      Loading applications...
                    </p>
                  ) : applications.length === 0 ? (
                    <p className="text-sm text-gray-500">
                      No applications yet.
                    </p>
                  ) : (
                    <div className="space-y-3">
                      {applications.map((application) => (
                        <div
                          key={application.applicationId}
                          className="border rounded p-3 bg-gray-50"
                        >
                          <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-2">
                            <div>
                              <p className="font-medium">
                                {application.applicantName}
                              </p>
                              <p className="text-sm text-gray-600">
                                {application.applicantEmail} •{" "}
                                {application.applicantPhone}
                              </p>
                              <p className="text-sm text-gray-500">
                                Birth date:{" "}
                                {new Date(
                                  application.applicantBirthDate
                                ).toLocaleDateString()}
                              </p>
                            </div>
                            <div className="text-sm text-gray-600">
                              Applied:{" "}
                              {new Date(
                                application.appliedAt
                              ).toLocaleString()}
                            </div>
                          </div>
                          <div className="flex items-center justify-between mt-2">
                            <span className="text-xs px-2 py-1 rounded-full bg-blue-100 text-blue-700">
                              {application.status}
                            </span>
                            <button
                              className="text-sm text-blue-600 hover:underline"
                              onClick={() =>
                                downloadCv(application.applicationId)
                              }
                            >
                              Download CV
                            </button>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
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
