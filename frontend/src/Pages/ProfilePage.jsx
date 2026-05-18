import { Link } from "react-router-dom";
import "../design/ProfilePage.css";

function ProfilePage() {
  const accessToken = localStorage.getItem("accessToken");
  const name = localStorage.getItem("gamblerName");
  const email = localStorage.getItem("gamblerEmail");
  const walletBalance = localStorage.getItem("walletBalance");

  if (!accessToken) {
    return (
      <div className="profile-page">
        <section className="auth-required-card">
          <h1>You need an account</h1>
          <p>
            Log in or register first to view your profile, wallet, bets, and
            stats.
          </p>

          <div className="auth-required-actions">
            <Link to="/login">Login</Link>
            <Link to="/register" className="secondary-auth-link">
              Register
            </Link>
          </div>
        </section>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <section className="profile-card">
        <div className="profile-header">
          <h1>Your Profile</h1>
          <p>Track your bets, wins, wallet, and stats.</p>
        </div>

        <div className="profile-stats">
          <div className="profile-stat">
            <span className="stat-label">Name</span>
            <strong>{name && name !== "null" ? name : "Unknown"}</strong>
          </div>

          <div className="profile-stat">
            <span className="stat-label">Email</span>
            <strong>{email ?? "Unknown"}</strong>
          </div>

          <div className="profile-stat">
            <span className="stat-label">Wallet Balance</span>
            <strong>€{walletBalance ?? "0"}</strong>
          </div>

          <div className="profile-stat">
            <span className="stat-label">Bets Created</span>
            <strong>Coming soon</strong>
          </div>

          <div className="profile-stat">
            <span className="stat-label">Joined Bets</span>
            <strong>Coming soon</strong>
          </div>

          <div className="profile-stat">
            <span className="stat-label">Win Rate</span>
            <strong>Coming soon</strong>
          </div>
        </div>
      </section>
    </div>
  );
}

export default ProfilePage;