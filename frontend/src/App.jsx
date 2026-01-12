import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route path="/login" element={<Login />} />

        {/* Temporary test route */}
        <Route
          path="/"
          element={
            <div className="text-3xl p-10 text-center">
              Frontend is working 🚀
            </div>
          }
        />

        {/* Example protected route */}
        <Route
          path="/employer"
          element={
            <ProtectedRoute role="EMPLOYER">
              <div className="p-10 text-xl">Employer Dashboard</div>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
