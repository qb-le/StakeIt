import Sidebar from "../layouts/SideBar.jsx";
import "../design/MainLayout.css";

function MainLayout({ children }) {
  return (
    <div className="app-layout">
      <Sidebar />

      <main className="main-content">
        {children}
      </main>
    </div>
  );
}

export default MainLayout;