import { useEffect, useMemo, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import api from "../../api/axios";
import { useAuth } from "../../context/AuthContext";

export default function JobSeekerDashboard() {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user) return;
    const loadApplications = async () => {
      try {
        setLoading(true);
        const res = await api.get("/jobseeker/applications");
        setApplications(res.data);
      } catch {
        setError("Unable to load your applications right now.");
      } finally {
        setLoading(false);
      }
    };

    loadApplications();
  }, [user]);

  const stats = useMemo(() => {
    const total = applications.length;
    const interviews = applications.filter((application) =>
      application.status?.toLowerCase().includes("interview")
    ).length;

    return [
      { label: "Applications", value: total },
      { label: "Interviews", value: interviews },
      { label: "Saved jobs", value: 0 },
    ];
  }, [applications]);

  if (!user || user.role !== "JOB_SEEKER") {
    return <Navigate to="/login" replace />;
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-6xl mx-auto p-6 space-y-6">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
          <div>
            <h1 className="text-3xl font-bold text-slate-900">
              Welcome back, {user.name || "Job Seeker"}
            </h1>
            <p className="text-slate-500">
              Track applications, saved roles, and personalized
              recommendations.
            </p>
          </div>
          <Link
            to="/jobs"
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
          >
            Browse jobs
          </Link>
        </div>

        <div className="grid gap-4 md:grid-cols-3">
          {stats.map((stat) => (
            <div
              key={stat.label}
              className="rounded-2xl border border-slate-100 bg-white p-5 shadow-sm"
            >
              <p className="text-2xl font-bold text-slate-900">
                {stat.value}
              </p>
              <p className="text-sm text-slate-500">{stat.label}</p>
            </div>
          ))}
        </div>

        <div className="grid gap-6 lg:grid-cols-2">
          <div className="bg-white rounded-2xl border border-slate-100 shadow-sm p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Recent applications
            </h2>
            {loading ? (
              <p className="text-sm text-slate-500">
                Loading applications...
              </p>
            ) : error ? (
              <p className="text-sm text-red-600">{error}</p>
            ) : applications.length === 0 ? (
              <p className="text-sm text-slate-500">
                You haven't applied to any jobs yet.
              </p>
            ) : (
              <div className="space-y-4">
                {applications.map((application) => (
                  <div
                    key={application.applicationId}
                    className="flex flex-col gap-2 border border-slate-100 rounded-xl p-4 sm:flex-row sm:items-center sm:justify-between"
                  >
                    <div>
                      <p className="font-semibold text-slate-900">
                        {application.role}
                      </p>
                      <p className="text-sm text-slate-500">
                        {application.company} •{" "}
                        {new Date(application.appliedAt).toLocaleDateString()}
                      </p>
                    </div>
                    <span className="text-xs font-semibold px-3 py-1 rounded-full bg-emerald-50 text-emerald-700 w-fit">
                      {application.status}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="bg-white rounded-2xl border border-slate-100 shadow-sm p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Saved jobs
            </h2>
            <p className="text-sm text-slate-500">
              Save roles to revisit them later. Start exploring new openings.
            </p>
            <Link
              to="/jobs"
              className="mt-4 inline-flex text-sm font-semibold text-blue-600 hover:text-blue-700"
            >
              Browse more roles →
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
