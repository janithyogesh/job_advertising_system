import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Home() {
  const { user } = useAuth();
  const stats = [
    { label: "Open roles", value: "12,480+" },
    { label: "Companies hiring", value: "1,240" },
    { label: "New hires this month", value: "3,800" },
  ];
  const featuredJobs = [
    {
      title: "Senior UI/UX Designer",
      company: "Nimbus Labs",
      location: "Remote • Europe",
      type: "Full-time",
      salary: "$85k - $110k",
    },
    {
      title: "Data Analyst",
      company: "Brightline Health",
      location: "Austin, TX",
      type: "Hybrid",
      salary: "$70k - $90k",
    },
    {
      title: "Frontend Engineer",
      company: "LiftPay",
      location: "Lagos, NG",
      type: "On-site",
      salary: "₦8M - ₦12M",
    },
  ];
  const categories = [
    { name: "Design", roles: "1.2k roles" },
    { name: "Engineering", roles: "4.4k roles" },
    { name: "Marketing", roles: "860 roles" },
    { name: "Customer Success", roles: "740 roles" },
  ];
  const companies = [
    "Paystack",
    "Stripe",
    "Interswitch",
    "PayPal",
    "Flutterwave",
    "Andela",
  ];
  const testimonials = [
    {
      quote:
        "We filled three engineering roles in under two weeks. The pipeline felt curated and fast.",
      name: "Ada Obi",
      role: "Talent Lead, Orbit Systems",
    },
    {
      quote:
        "The search filters and clean application flow made it simple to apply from my phone.",
      name: "Ibrahim Yusuf",
      role: "Product Designer",
    },
  ];

  /**
   * Decide where "Post Job" should go
   */
  const getPostJobLink = () => {
    // ✅ Employer already logged in
    if (user && user.role === "EMPLOYER") {
      return "/employer/dashboard";
    }

    // ✅ Job seeker or not logged in
    // They must register as employer
    return "/register";
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <section className="bg-gradient-to-br from-blue-50 via-white to-emerald-50">
        <div className="max-w-6xl mx-auto px-6 py-16 grid gap-10 lg:grid-cols-[1.1fr,0.9fr]">
          <div>
            <span className="inline-flex items-center rounded-full bg-blue-100 px-3 py-1 text-xs font-semibold text-blue-700">
              Trusted by fast-growing teams
            </span>
            <h1 className="text-4xl md:text-5xl font-bold text-slate-900 mt-4 leading-tight">
              Find or post your next job with confidence
            </h1>
            <p className="text-slate-600 mt-4 text-lg">
              Explore curated opportunities, track applications, and reach
              candidates faster. Everything you need to hire or get hired in
              one place.
            </p>

            <div className="mt-8 flex flex-col sm:flex-row gap-4">
              <Link
                to="/jobs"
                className="px-8 py-3 bg-blue-600 text-white rounded-lg font-semibold text-center hover:bg-blue-700 transition"
              >
                Find Jobs
              </Link>
              <Link
                to={getPostJobLink()}
                className="px-8 py-3 bg-emerald-600 text-white rounded-lg font-semibold text-center hover:bg-emerald-700 transition"
              >
                Post Job
              </Link>
            </div>

            <div className="mt-10 grid gap-4 sm:grid-cols-3">
              {stats.map((stat) => (
                <div
                  key={stat.label}
                  className="bg-white border border-slate-100 rounded-xl p-4 shadow-sm"
                >
                  <p className="text-2xl font-bold text-slate-900">
                    {stat.value}
                  </p>
                  <p className="text-sm text-slate-500">{stat.label}</p>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-white rounded-2xl shadow-lg border border-slate-100 p-6 flex flex-col gap-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-slate-500">Hiring now</p>
                <p className="text-xl font-semibold text-slate-900">
                  Featured jobs
                </p>
              </div>
              <span className="text-xs bg-slate-100 px-3 py-1 rounded-full text-slate-600">
                Updated daily
              </span>
            </div>

            <div className="space-y-4">
              {featuredJobs.map((job) => (
                <div
                  key={job.title}
                  className="rounded-xl border border-slate-100 p-4 hover:shadow-sm transition"
                >
                  <h3 className="font-semibold text-slate-900">
                    {job.title}
                  </h3>
                  <p className="text-sm text-slate-500">
                    {job.company} • {job.location}
                  </p>
                  <div className="mt-3 flex flex-wrap items-center gap-2 text-xs text-slate-500">
                    <span className="rounded-full bg-slate-100 px-2 py-1">
                      {job.type}
                    </span>
                    <span className="rounded-full bg-emerald-50 px-2 py-1 text-emerald-700">
                      {job.salary}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </section>

      <section className="max-w-6xl mx-auto px-6 py-12">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <h2 className="text-2xl font-bold text-slate-900">
              Popular categories
            </h2>
            <p className="text-slate-500">
              Explore roles by team and discipline.
            </p>
          </div>
          <Link
            to="/jobs"
            className="text-sm font-semibold text-blue-600 hover:text-blue-700"
          >
            View all jobs →
          </Link>
        </div>

        <div className="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {categories.map((category) => (
            <div
              key={category.name}
              className="rounded-2xl border border-slate-100 bg-white p-5 shadow-sm"
            >
              <h3 className="font-semibold text-slate-900">
                {category.name}
              </h3>
              <p className="text-sm text-slate-500">{category.roles}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="bg-white border-y border-slate-100">
        <div className="max-w-6xl mx-auto px-6 py-10">
          <p className="text-xs uppercase tracking-[0.2em] text-slate-400 font-semibold">
            Trusted by
          </p>
          <div className="mt-4 grid gap-4 sm:grid-cols-3 lg:grid-cols-6 text-slate-600 font-semibold">
            {companies.map((company) => (
              <div
                key={company}
                className="bg-slate-50 rounded-xl p-4 text-center"
              >
                {company}
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="max-w-6xl mx-auto px-6 py-12">
        <h2 className="text-2xl font-bold text-slate-900">
          Why teams choose JobPortal
        </h2>
        <div className="mt-6 grid gap-6 lg:grid-cols-2">
          {testimonials.map((item) => (
            <div
              key={item.name}
              className="rounded-2xl border border-slate-100 bg-white p-6 shadow-sm"
            >
              <p className="text-slate-600">“{item.quote}”</p>
              <div className="mt-4">
                <p className="font-semibold text-slate-900">
                  {item.name}
                </p>
                <p className="text-sm text-slate-500">{item.role}</p>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
