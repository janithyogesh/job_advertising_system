import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import api from "../api/axios";
import { useAuth } from "../context/AuthContext";

const API_BASE_URL = "http://localhost:8080";

export default function JobDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [cvFile, setCvFile] = useState(null);
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    phone: "",
    birthDate: "",
  });

  useEffect(() => {
    fetchJob();
  }, [id]);

  const fetchJob = async () => {
    try {
      setLoading(true);
      const res = await api.get(`/jobs/${id}`);
      setJob(res.data);
    } catch {
      alert("Failed to load job details");
      navigate("/jobs");
    } finally {
      setLoading(false);
    }
  };

  const isExpired = useMemo(() => {
    if (!job?.deadline) return false;
    return new Date(job.deadline) < new Date();
  }, [job]);

  const resolveImageUrl = (jobImageUrl) => {
    if (!jobImageUrl) return null;
    if (jobImageUrl.startsWith("http")) return jobImageUrl;
    return `${API_BASE_URL}${jobImageUrl}`;
  };

  const getErrorMessage = (err) => {
    if (!err) return "Failed to submit application";
    const responseData = err.response?.data;
    if (typeof responseData === "string" && responseData.trim()) {
      return responseData;
    }
    if (responseData?.message) return responseData.message;
    if (responseData?.error) return responseData.error;
    if (err.message) return err.message;
    return "Failed to submit application";
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleApply = async (e) => {
    e.preventDefault();

    if (!user || user.role !== "JOB_SEEKER") {
      alert("Only job seekers can apply");
      return;
    }

    if (isExpired) {
      alert("This job has expired");
      return;
    }

    if (!cvFile) {
      alert("CV file is required");
      return;
    }

    try {
      setSubmitting(true);

      const payload = new FormData();
      payload.append("jobId", job.id);
      payload.append("fullName", form.fullName);
      payload.append("email", form.email);
      payload.append("phone", form.phone);
      payload.append("birthDate", form.birthDate);
      payload.append("cv", cvFile);

      await api.post("/jobseeker/applications", payload);
      alert("Application submitted successfully");
      setForm({
        fullName: "",
        email: "",
        phone: "",
        birthDate: "",
      });
      setCvFile(null);
    } catch (err) {
      alert(getErrorMessage(err));
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="max-w-5xl mx-auto p-6">
        <p>Loading job details...</p>
      </div>
    );
  }

  if (!job) {
    return (
      <div className="max-w-5xl mx-auto p-6">
        <p className="text-gray-500">Job not found.</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-5xl mx-auto p-6">
        <Link to="/jobs" className="text-blue-600 hover:underline text-sm">
          ← Back to Jobs
        </Link>

        <div className="bg-white rounded-lg shadow-sm border mt-4 overflow-hidden">
          {resolveImageUrl(job.jobImageUrl) && (
            <img
              src={resolveImageUrl(job.jobImageUrl)}
              alt={job.title}
              className="w-full h-64 object-cover"
            />
          )}

          <div className="p-6 space-y-4">
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
              <div>
                <h1 className="text-2xl font-bold">{job.title}</h1>
                <p className="text-gray-600">
                  {job.company} • {job.location}
                </p>
              </div>
              <span
                className={`text-xs px-3 py-1 rounded-full ${
                  isExpired
                    ? "bg-red-100 text-red-700"
                    : "bg-green-100 text-green-700"
                }`}
              >
                {isExpired ? "EXPIRED" : "ACTIVE"}
              </span>
            </div>

            <div className="grid gap-3 sm:grid-cols-2">
              <div>
                <p className="text-sm text-gray-500">Employment Type</p>
                <p>{job.employmentType}</p>
              </div>
              <div>
                <p className="text-sm text-gray-500">Category</p>
                <p>{job.categoryName}</p>
              </div>
              <div>
                <p className="text-sm text-gray-500">Salary</p>
                <p>{job.salary || "Not specified"}</p>
              </div>
              <div>
                <p className="text-sm text-gray-500">Deadline</p>
                <p>{new Date(job.deadline).toLocaleString()}</p>
              </div>
            </div>

            <div>
              <p className="text-sm text-gray-500 mb-1">Job Description</p>
              <p className="text-gray-700 whitespace-pre-line">
                {job.description}
              </p>
            </div>

            <div className="border-t pt-4">
              <p className="text-sm text-gray-500 mb-1">Contact</p>
              <p>{job.contactEmail}</p>
              <p>{job.contactPhone}</p>
            </div>
          </div>
        </div>

        <div className="bg-white border rounded-lg shadow-sm mt-6 p-6">
          <h2 className="text-xl font-semibold mb-4">
            Apply for this job
          </h2>

          {!user ? (
            <p className="text-gray-600">
              Please log in as a job seeker to apply.
            </p>
          ) : user.role !== "JOB_SEEKER" ? (
            <p className="text-gray-600">
              Only job seekers can submit applications.
            </p>
          ) : (
            <form onSubmit={handleApply} className="space-y-4">
              <div className="grid gap-4 md:grid-cols-2">
                <input
                  name="fullName"
                  placeholder="Full Name"
                  className="border rounded px-3 py-2"
                  value={form.fullName}
                  onChange={handleChange}
                  required
                />
                <input
                  name="email"
                  type="email"
                  placeholder="Email"
                  className="border rounded px-3 py-2"
                  value={form.email}
                  onChange={handleChange}
                  required
                />
                <input
                  name="phone"
                  placeholder="Phone"
                  className="border rounded px-3 py-2"
                  value={form.phone}
                  onChange={handleChange}
                  required
                />
                <input
                  name="birthDate"
                  type="date"
                  className="border rounded px-3 py-2"
                  value={form.birthDate}
                  onChange={handleChange}
                  required
                />
              </div>

              <input
                type="file"
                accept=".pdf,.docx,image/*"
                onChange={(e) => setCvFile(e.target.files[0])}
                required
              />

              <button
                type="submit"
                disabled={submitting || isExpired}
                className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
              >
                {isExpired ? "Applications Closed" : "Submit Application"}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
