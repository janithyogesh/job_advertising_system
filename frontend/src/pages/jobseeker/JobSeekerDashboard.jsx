import { useAuth } from "../../context/AuthContext";
import { Navigate } from "react-router-dom";

export default function JobSeekerDashboard() {
  const { user } = useAuth();
  const applications = [
    {
      role: "Product Designer",
      company: "Nimbus Labs",
      status: "Interview scheduled",
      date: "Sep 02, 2024",
    },
    {
      role: "Frontend Engineer",
      company: "LiftPay",
      status: "Application sent",
      date: "Aug 28, 2024",
    },
    {
      role: "Data Analyst",
      company: "Brightline Health",
      status: "Offer",
      date: "Aug 19, 2024",
    },
  ];
  const savedJobs = [
    {
      title: "Growth Marketing Lead",
      company: "Pulse Media",
      location: "Remote",
      type: "Full-time",
    },
    {
      title: "Customer Success Manager",
      company: "BambooHR",
      location: "Dubai, UAE",
      type: "Hybrid",
    },
  ];

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
          <button className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700">
            Update profile
          </button>
        </div>

        <div className="grid gap-4 md:grid-cols-3">
          {[
            { label: "Applications", value: "12" },
            { label: "Interviews", value: "4" },
            { label: "Saved jobs", value: "8" },
          ].map((stat) => (
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
            <div className="space-y-4">
              {applications.map((application) => (
                <div
                  key={`${application.role}-${application.company}`}
                  className="flex items-center justify-between border border-slate-100 rounded-xl p-4"
                >
                  <div>
                    <p className="font-semibold text-slate-900">
                      {application.role}
                    </p>
                    <p className="text-sm text-slate-500">
                      {application.company} • {application.date}
                    </p>
                  </div>
                  <span className="text-xs font-semibold px-3 py-1 rounded-full bg-emerald-50 text-emerald-700">
                    {application.status}
                  </span>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-white rounded-2xl border border-slate-100 shadow-sm p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Saved jobs
            </h2>
            <div className="space-y-4">
              {savedJobs.map((job) => (
                <div
                  key={job.title}
                  className="flex flex-col gap-2 border border-slate-100 rounded-xl p-4"
                >
                  <div>
                    <p className="font-semibold text-slate-900">
                      {job.title}
                    </p>
                    <p className="text-sm text-slate-500">
                      {job.company} • {job.location}
                    </p>
                  </div>
                  <span className="text-xs font-semibold px-3 py-1 rounded-full bg-blue-50 text-blue-700 w-fit">
                    {job.type}
                  </span>
                </div>
              ))}
            </div>
            <button className="mt-4 text-sm font-semibold text-blue-600 hover:text-blue-700">
              Browse more roles →
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
