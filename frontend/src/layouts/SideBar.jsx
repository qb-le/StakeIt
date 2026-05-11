import { Link } from "react-router-dom";
import "../design/Sidebar.css";

function Sidebar() {
  return (
    <aside className="sidebar">
      <Link to="/" className="logo-circle">
        <img src="/stakeit-logo.png" alt="StakeIt logo" />
      </Link>

      <div className="sidebar-buttons">
        <Link to="/login">login</Link>
        <Link to="/register">register</Link>
      </div>
    </aside>
  );
}

export default Sidebar;