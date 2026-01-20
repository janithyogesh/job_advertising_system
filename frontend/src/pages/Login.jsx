import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const res = await api.post("/auth/login", {
        email,
        password,
      });

      login(res.data.token);

      navigate("/dashboard");
    } catch (err) {
      setError(
        err.response?.data?.message || "Invalid credentials"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 px-4 py-10">
      <div className="w-full max-w-4xl bg-white rounded-2xl shadow-xl overflow-hidden grid md:grid-cols-2">
        <form
          onSubmit={handleSubmit}
          className="p-8 md:p-10"
        >
          <h2 className="text-2xl font-bold text-slate-900 mb-2">
            Welcome back
          </h2>
          <p className="text-sm text-slate-500 mb-6">
            Sign in to manage postings or apply to your next role.
          </p>

          {error && (
            <div className="mb-3 text-red-600 text-sm">
              {error}
            </div>
          )}

          <label className="text-sm font-medium text-slate-600">
            Email address
          </label>
          <input
            type="email"
            placeholder="you@company.com"
            className="w-full p-2 border rounded-lg mb-4 mt-2"
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label className="text-sm font-medium text-slate-600">
            Password
          </label>
          <input
            type="password"
            placeholder="Enter your password"
            className="w-full p-2 border rounded-lg mb-6 mt-2"
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50 font-semibold"
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        <div className="bg-gradient-to-br from-blue-600 to-emerald-500 text-white p-8 md:p-10 flex flex-col justify-between">
          <div>
            <p className="uppercase text-xs tracking-[0.2em] text-blue-100">
              Demo access
            </p>
            <h3 className="text-2xl font-semibold mt-3">
              Try JobPortal with sample accounts
            </h3>
            <p className="text-sm text-blue-100 mt-2">
              Use these credentials to explore employer and job seeker
              experiences.
            </p>
          </div>

          <div className="mt-8 space-y-4 text-sm">
            <div className="rounded-xl bg-white/15 p-4">
              <p className="text-blue-100 text-xs uppercase">
                Employer login
              </p>
              <p className="font-semibold">test1@g.com</p>
              <p className="text-blue-100">Password: test123</p>
            </div>
            <div className="rounded-xl bg-white/15 p-4">
              <p className="text-blue-100 text-xs uppercase">
                Job seeker login
              </p>
              <p className="font-semibold">seeker1@g.com</p>
              <p className="text-blue-100">Password: seeker123</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
