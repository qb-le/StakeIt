import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import stakeitLogo from "../assets/stakeit-logo.webp";
import "../design/Sidebar.css";

function Sidebar() {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useAuth();

  function handleLogout() {
    logout();
    navigate("/login");
  }

  return (
    <aside className="sidebar">
      <div className="sidebar-top">
        <Link to="/" className="logo-circle">
          <img src={stakeitLogo} alt="StakeIt logo" width="34" height="34" />
        </Link>

        {isLoggedIn && (
          <Link to="/profile" className="profile-sidebar-button">
            👤
          </Link>
        )}
      </div>

      <div className="sidebar-buttons">
        {!isLoggedIn ? (
          <>
            <Link to="/login">login</Link>
            <Link to="/register">register</Link>
          </>
        ) : (
          <button type="button" onClick={handleLogout}>
            logout
          </button>
        )}
      </div>
    </aside>
  );
}

export default Sidebar;