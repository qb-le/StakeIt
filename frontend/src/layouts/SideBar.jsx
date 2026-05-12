import { Link } from "react-router-dom";
import stakeitLogo from "../assets/stakeit-logo.webp";
import "../design/Sidebar.css";

function Sidebar() {
  return (
    <aside className="sidebar">
      <Link to="/" className="logo-circle">
        <img src={stakeitLogo} alt="StakeIt logo" width="34" height="34" />
      </Link>

      <div className="sidebar-buttons">
        <Link to="/login">login</Link>
        <Link to="/register">register</Link>
      </div>
    </aside>
  );
}

export default Sidebar;