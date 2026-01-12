import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Jobs from "./pages/Jobs";

import RoleRedirect from "./pages/RoleRedirect";
import EmployerDashboard from "./pages/employer/EmployerDashboard";
import JobSeekerDashboard from "./pages/jobseeker/JobSeekerDashboard";

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/jobs" element={<Jobs />} />

        {/* 👇 SMART ROLE ROUTE */}
        <Route path="/dashboard" element={<RoleRedirect />} />

        {/* 👇 REAL DASHBOARDS */}
        <Route
          path="/employer/dashboard"
          element={<EmployerDashboard />}
        />
        <Route
          path="/jobseeker/dashboard"
          element={<JobSeekerDashboard />}
        />
      </Routes>
    </BrowserRouter>
  );
}
