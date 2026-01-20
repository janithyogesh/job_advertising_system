import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";

const API_BASE_URL = "http://localhost:8080";

export default function Jobs() {
  const [categories, setCategories] = useState([]);
  const [jobs, setJobs] = useState([]);
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [categoryId, setCategoryId] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadCategories();
  }, []);

  useEffect(() => {
    fetchJobs();
  }, [statusFilter, categoryId, page]);

  useEffect(() => {
    setPage(0);
  }, [statusFilter, categoryId]);

  const loadCategories = async () => {
    try {
      const res = await api.get("/categories");
      setCategories(res.data);
    } catch {
      alert("Failed to load categories");
    }
  };

  const fetchJobs = async () => {
    try {
      setLoading(true);
      const params = {
        status: statusFilter,
        page,
        size: 9,
      };
      if (categoryId) {
        params.categoryId = categoryId;
      }
      const res = await api.get("/jobs", { params });
      setJobs(res.data.jobs);
      setTotalPages(res.data.totalPages);
    } catch {
      alert("Failed to load jobs");
    } finally {
      setLoading(false);
    }
  };

  const resolveImageUrl = (jobImageUrl) => {
    if (!jobImageUrl) return null;
    if (jobImageUrl.startsWith("http")) return jobImageUrl;
    return `${API_BASE_URL}${jobImageUrl}`;
  };

  const canGoBack = useMemo(() => page > 0, [page]);
  const canGoNext = useMemo(() => page + 1 < totalPages, [page, totalPages]);

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-6xl mx-auto p-6">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
          <div>
            <h2 className="text-2xl font-bold">Available Jobs</h2>
            <p className="text-gray-500">
              Browse openings and apply before the deadline.
            </p>
          </div>

          <div className="flex flex-col sm:flex-row gap-3">
            <select
              className="border rounded px-3 py-2"
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="ALL">All Statuses</option>
              <option value="ACTIVE">Active</option>
              <option value="EXPIRED">Expired</option>
            </select>

            <select
              className="border rounded px-3 py-2"
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
            >
              <option value="">All Categories</option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>
        </div>

        {loading ? (
          <p>Loading jobs...</p>
        ) : jobs.length === 0 ? (
          <p className="text-gray-500">No jobs found.</p>
        ) : (
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {jobs.map((job) => (
              <div
                key={job.id}
                className="bg-white border rounded-lg shadow-sm overflow-hidden flex flex-col"
              >
                {resolveImageUrl(job.jobImageUrl) && (
                  <img
                    src={resolveImageUrl(job.jobImageUrl)}
                    alt={job.title}
                    className="h-40 w-full object-cover"
                  />
                )}

                <div className="p-4 flex flex-col flex-1">
                  <h3 className="text-lg font-semibold">{job.title}</h3>
                  <p className="text-gray-600">
                    {job.company} • {job.location}
                  </p>
                  <p className="text-sm text-gray-500 mt-1">
                    {job.employmentType}
                  </p>

                  <div className="mt-3 flex items-center justify-between">
                    <span
                      className={`text-xs px-2 py-1 rounded-full ${
                        job.status === "EXPIRED"
                          ? "bg-red-100 text-red-700"
                          : "bg-green-100 text-green-700"
                      }`}
                    >
                      {job.status}
                    </span>

                    <Link
                      to={`/jobs/${job.id}`}
                      className="text-blue-600 text-sm hover:underline"
                    >
                      View Details
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {totalPages > 1 && (
          <div className="flex justify-center items-center gap-4 mt-8">
            <button
              className="px-4 py-2 border rounded disabled:opacity-50"
              onClick={() => setPage((prev) => prev - 1)}
              disabled={!canGoBack}
            >
              Previous
            </button>
            <span className="text-sm text-gray-600">
              Page {page + 1} of {totalPages}
            </span>
            <button
              className="px-4 py-2 border rounded disabled:opacity-50"
              onClick={() => setPage((prev) => prev + 1)}
              disabled={!canGoNext}
            >
              Next
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
