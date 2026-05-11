import { Outlet } from "react-router-dom";
import Sidebar from "../layouts/SideBar.jsx";
import "../design/MainLayout.css";

function MainLayout() {
  return (
    <div className="app-layout">
      <Sidebar />

      <main className="page-content">
        <Outlet />
      </main>
    </div>
  );
}

export default MainLayout;