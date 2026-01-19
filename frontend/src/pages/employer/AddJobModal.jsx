import { useEffect, useState } from "react";
import api from "../../api/axios";

export default function AddJobModal({ onClose, onJobAdded }) {
  const [categories, setCategories] = useState([]);

  const [form, setForm] = useState({
    title: "",
    description: "",
    company: "",
    location: "",
    employmentType: "FULL_TIME",
    salary: "",
    contactEmail: "",
    contactPhone: "",
    categoryId: "",
    deadline: "",
  });

  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      const res = await api.get("/categories");

      setCategories(res.data.filter((c) => c.active));
    } catch {
      alert("Failed to load categories");
    }
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!image) {
      alert("Job image is required");
      return;
    }

    try {
      setLoading(true);

      const data = new FormData();
      Object.keys(form).forEach((key) =>
        data.append(key, form[key])
      );
      data.append("image", image);

      await api.post("/employer/jobs", data);

      alert("Job posted successfully");
      onJobAdded();
      onClose();
    } catch (err) {
      alert(err.response?.data || "Failed to post job");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white w-full max-w-2xl p-6 rounded shadow-lg overflow-y-auto max-h-[90vh]">
        <h2 className="text-2xl font-bold mb-4">
          Post a New Job
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            name="title"
            placeholder="Job Title"
            className="w-full border p-2 rounded"
            onChange={handleChange}
            required
          />

          <textarea
            name="description"
            placeholder="Job Description"
            className="w-full border p-2 rounded"
            rows={4}
            onChange={handleChange}
            required
          />

          <input
            name="company"
            placeholder="Company Name"
            className="w-full border p-2 rounded"
            onChange={handleChange}
            required
          />

          <input
            name="location"
            placeholder="Location"
            className="w-full border p-2 rounded"
            onChange={handleChange}
            required
          />

          <select
            name="employmentType"
            className="w-full border p-2 rounded"
            onChange={handleChange}
          >
            <option value="FULL_TIME">Full Time</option>
            <option value="PART_TIME">Part Time</option>
            <option value="CONTRACT">Contract</option>
          </select>

          <input
            name="salary"
            placeholder="Salary (optional)"
            className="w-full border p-2 rounded"
            onChange={handleChange}
          />

          <input
            name="contactEmail"
            placeholder="Contact Email"
            type="email"
            className="w-full border p-2 rounded"
            onChange={handleChange}
            required
          />

          <input
            name="contactPhone"
            placeholder="Contact Phone"
            className="w-full border p-2 rounded"
            onChange={handleChange}
            required
          />

          <select
            name="categoryId"
            className="w-full border p-2 rounded"
            onChange={handleChange}
            required
          >
            <option value="">Select Category</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>

          <input
            type="datetime-local"
            name="deadline"
            className="w-full border p-2 rounded"
            onChange={handleChange}
            required
          />

          <input
            type="file"
            accept="image/*"
            onChange={(e) => setImage(e.target.files[0])}
            required
          />

          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border rounded"
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={loading}
              className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
            >
              {loading ? "Posting..." : "Post Job"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
