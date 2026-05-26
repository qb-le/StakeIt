import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../design/LandingPage.css";

function LandingPage() {
  const navigate = useNavigate();

  const [bets, setBets] = useState([]);
  const [joinedBetIds, setJoinedBetIds] = useState([]);

  const [selectedBet, setSelectedBet] = useState(null);
  const [loadingSelectedBet, setLoadingSelectedBet] = useState(false);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [joiningBetId, setJoiningBetId] = useState(null);

  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  function handleCreateBetClick() {
    const accessToken = localStorage.getItem("accessToken");

    if (!accessToken) {
      navigate("/login");
      return;
    }

    navigate("/create-bet");
  }

  function formatDate(dateValue) {
    if (!dateValue) return "Unknown";

    return new Date(dateValue).toLocaleString("en-GB", {
      day: "2-digit",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  }

  async function fetchBets(page) {
    setLoading(true);
    setError("");

    try {
      const accessToken = localStorage.getItem("accessToken");

      const response = await fetch(`/api/Bets/GetBetsPerPage?page=${page}`, {
        headers: {
          Authorization: accessToken ? `Bearer ${accessToken}` : "",
        },
      });

      if (!response.ok) {
        throw new Error(
          `Failed to fetch bets: ${response.status} ${response.statusText}`
        );
      }

      const data = await response.json();

      setBets(data.bets ?? []);
      setTotalPages(data.totalPages ?? 1);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function fetchJoinedBetsForButtons() {
    const accessToken = localStorage.getItem("accessToken");
    const gamblerId = localStorage.getItem("gamblerId");

    if (!accessToken || !gamblerId) return;

    try {
      const response = await fetch(`/api/Bets/JoinedBets?userId=${gamblerId}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) return;

      const data = await response.json();
      const ids = data.map((bet) => bet.id);

      setJoinedBetIds(ids);
    } catch (err) {
      console.error("Failed to fetch joined bets:", err);
    }
  }

  async function handleReadBet(betId) {
    setLoadingSelectedBet(true);
    setError("");

    try {
      const response = await fetch(`/api/Bets/ReadBet?betId=${betId}`);

      if (!response.ok) {
        const errorText = await response.text();

        throw new Error(
          `Failed to read bet: ${response.status} ${response.statusText} ${errorText}`
        );
      }

      const data = await response.json();
      setSelectedBet(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoadingSelectedBet(false);
    }
  }

  async function handleJoinBet(betId) {
    const accessToken = localStorage.getItem("accessToken");
    const gamblerId = localStorage.getItem("gamblerId");

    if (!accessToken) {
      navigate("/login");
      return;
    }

    if (!gamblerId) {
      setError("Could not find user id. Please log in again.");
      return;
    }

    setJoiningBetId(betId);
    setError("");

    try {
      const response = await fetch(
        `/api/Bets/JoinBet?betId=${betId}&userId=${gamblerId}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      if (!response.ok) {
        const errorText = await response.text();

        throw new Error(
          `Failed to join bet: ${response.status} ${response.statusText} ${errorText}`
        );
      }

      setJoinedBetIds((currentIds) => {
        if (currentIds.includes(betId)) {
          return currentIds;
        }

        return [...currentIds, betId];
      });
    } catch (err) {
      setError(err.message);
    } finally {
      setJoiningBetId(null);
    }
  }

  useEffect(() => {
    fetchBets(currentPage);
    fetchJoinedBetsForButtons();
  }, [currentPage]);

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

          <button
            type="button"
            className="create-bet-button"
            onClick={handleCreateBetClick}
          >
            Create Bet
          </button>
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
          bets.map((bet, index) => {
            const alreadyJoined = joinedBetIds.includes(bet.id);
            const isJoining = joiningBetId === bet.id;

            return (
              <div
                key={bet.id}
                className={`bet-row clickable-bet-row ${
                  index % 2 === 0 ? "light" : "dark"
                } ${index === 0 ? "first-bet-row" : ""} ${
                  index === bets.length - 1 ? "last-bet-row" : ""
                }`}
                onClick={() => handleReadBet(bet.id)}
              >
                <div className="bet-main-line">
                  <span className="bet-title">{bet.title}</span>

                  <span className="bet-price">
                    €{bet.betPrice ?? bet.bet_price}
                  </span>

                  <span className="bet-end-date">
                    Ends: {formatDate(bet.betEndsAt ?? bet.bet_ends_at)}
                  </span>
                </div>

                <button
                  type="button"
                  className={`join-bet-button ${alreadyJoined ? "joined" : ""}`}
                  onClick={(event) => {
                    event.stopPropagation();
                    handleJoinBet(bet.id);
                  }}
                  disabled={alreadyJoined || isJoining}
                >
                  {alreadyJoined
                    ? "Joined already"
                    : isJoining
                    ? "Joining..."
                    : "Join Bet"}
                </button>
              </div>
            );
          })}

        {!loading && !error && totalPages > 1 && (
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

      {(selectedBet || loadingSelectedBet) && (
        <div
          className="bet-detail-overlay"
          onClick={() => setSelectedBet(null)}
        >
          <div
            className="bet-detail-modal"
            onClick={(event) => event.stopPropagation()}
          >
            {loadingSelectedBet && (
              <p className="bets-message">Loading bet details...</p>
            )}

            {!loadingSelectedBet && selectedBet && (
              <>
                <div className="bet-detail-header">
                  <h2>{selectedBet.title}</h2>

                  <button
                    type="button"
                    className="close-detail-button"
                    onClick={() => setSelectedBet(null)}
                  >
                    ✕
                  </button>
                </div>

                <div className="bet-detail-grid">
                  <div>
                    <span>Price</span>
                    <strong>
                      €{selectedBet.betPrice ?? selectedBet.bet_price}
                    </strong>
                  </div>

                  <div>
                    <span>Ends at</span>
                    <strong>
                      {formatDate(
                        selectedBet.betEndsAt ?? selectedBet.bet_ends_at
                      )}
                    </strong>
                  </div>

                  <div>
                    <span>Status</span>
                    <strong>{selectedBet.status ?? "Unknown"}</strong>
                  </div>
                </div>

                <div className="bet-detail-description">
                  <h3>Description</h3>
                  <p>{selectedBet.description || "No description provided."}</p>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default LandingPage;