import { useEffect, useState } from "react";
import "../design/LandingPage.css";

const BETS_PER_PAGE = 10;

function LandingPage() {
  const [bets, setBets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    async function fetchBets() {
      try {
        const response = await fetch("/api/Bets/AllBets");

        if (!response.ok) {
          throw new Error(`Failed to fetch bets: ${response.status}`);
        }

        const contentType = response.headers.get("content-type");

        if (!contentType || !contentType.includes("application/json")) {
          throw new Error("Backend did not return JSON. Check /api proxy.");
        }

        const data = await response.json();
        setBets(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }

    fetchBets();
  }, []);

  const totalPages = Math.ceil(bets.length / BETS_PER_PAGE);

  const startIndex = (currentPage - 1) * BETS_PER_PAGE;
  const currentBets = bets.slice(startIndex, startIndex + BETS_PER_PAGE);

  function goToPreviousPage() {
    setCurrentPage((page) => Math.max(page - 1, 1));
  }

  function goToNextPage() {
    setCurrentPage((page) => Math.min(page + 1, totalPages));
  }

  return (
    <div className="landing-page">
      <section className="hero-cards">
        <div className="info-card">
          <h2>Make your own bets</h2>
          <p>Register and create fun bets with your friends.</p>
        </div>

        <div className="info-card">
          <h2>Referral invite</h2>
          <p>Get 2% of each win from your friends.</p>
        </div>
      </section>

      <section className="bet-preview">
        <h2 className="bets-title">Bets</h2>

        {loading && <p className="bets-message">Loading bets...</p>}

        {error && <p className="bets-error">{error}</p>}

        {!loading && !error && bets.length === 0 && (
          <p className="bets-message">No bets found.</p>
        )}

        {!loading &&
          !error &&
          currentBets.map((bet, index) => (
            <div
              key={bet.id}
              className={`bet-row ${index % 2 === 0 ? "light" : "dark"}`}
            >
              <div className="bet-info">
                <span className="bet-title">{bet.title}</span>
                <span className="bet-description">{bet.description}</span>
              </div>

              <span className="bet-price">
                €{bet.betPrice ?? bet.bet_price}
              </span>
            </div>
          ))}

        {!loading && !error && bets.length > BETS_PER_PAGE && (
          <div className="pagination">
            <button
              type="button"
              onClick={goToPreviousPage}
              disabled={currentPage === 1}
            >
              Previous
            </button>

            <span>
              Page {currentPage} of {totalPages}
            </span>

            <button
              type="button"
              onClick={goToNextPage}
              disabled={currentPage === totalPages}
            >
              Next
            </button>
          </div>
        )}
      </section>
    </div>
  );
}

export default LandingPage;