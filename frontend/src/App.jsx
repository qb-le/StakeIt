import { Routes, Route } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";
import LandingPage from "./Pages/LandingPage";
import LoginPage from "./Pages/LoginPage";

function App() {
  return (
    <MainLayout>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
    </MainLayout>
  );
}

export default App;