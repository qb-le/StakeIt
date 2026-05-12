import { useState } from "react";
import { Link } from "react-router-dom";


function LoginPage() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");

  function handleChange(event) {
    const { name, value } = event.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    try {
      const response = await fetch("/api/Auth/Login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error(`Login failed: ${response.status}`);
      }

      const data = await response.json();

      console.log("Login success:", data);

      // Later you can save token here:
      // localStorage.setItem("token", data.token);

    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="login-page">
      <section className="login-card">
        <div className="login-header">
          <h1>Welcome back</h1>
          <p>Log in to create bets, join challenges, and track your wins.</p>
        </div>

        <form className="login-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          {error && <p className="login-error">{error}</p>}

          <button type="submit" className="login-button">
            Login
          </button>
        </form>

        <p className="login-footer">
          Don&apos;t have an account?{" "}
          <Link to="/register">Register</Link>
        </p>
      </section>
    </div>
  );
}

export default LoginPage;