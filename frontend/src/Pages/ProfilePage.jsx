import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "../design/ProfilePage.css";

const BETS_PER_PAGE = 4;

function ProfilePage() {
  const accessToken = localStorage.getItem("accessToken");
  const name = localStorage.getItem("gamblerName");
  const email = localStorage.getItem("gamblerEmail");
  const walletBalance = localStorage.getItem("walletBalance");

  const [ownBets, setOwnBets] = useState([]);
  const [joinedBets, setJoinedBets] = useState([]);

  const [loadingOwnBets, setLoadingOwnBets] = useState(false);
  const [loadingJoinedBets, setLoadingJoinedBets] = useState(false);

  const [ownBetsError, setOwnBetsError] = useState("");
  const [joinedBetsError, setJoinedBetsError] = useState("");

  const [ownBetsPage, setOwnBetsPage] = useState(1);
  const [joinedBetsPage, setJoinedBetsPage] = useState(1);

  useEffect(() => {
    if (!accessToken) return;

    async function fetchOwnBets() {
      setLoadingOwnBets(true);
      setOwnBetsError("");

      try {
        const gamblerId = localStorage.getItem("gamblerId");

        const response = await fetch(`/api/Bets/OwnBets?userId=${gamblerId}`, {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });

        if (!response.ok) {
          const errorText = await response.text();

          throw new Error(
            `Failed to fetch own bets: ${response.status} ${response.statusText} ${errorText}`
          );
        }

        const data = await response.json();
        setOwnBets(data);
        setOwnBetsPage(1);
      } catch (err) {
        setOwnBetsError(err.message);
      } finally {
        setLoadingOwnBets(false);
      }
    }

    async function fetchJoinedBets() {
      setLoadingJoinedBets(true);
      setJoinedBetsError("");

      try {
        const gamblerId = localStorage.getItem("gamblerId");

        const response = await fetch(
          `/api/Bets/JoinedBets?userId=${gamblerId}`,
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );

        if (!response.ok) {
          const errorText = await response.text();

          throw new Error(
            `Failed to fetch joined bets: ${response.status} ${response.statusText} ${errorText}`
          );
        }

        const data = await response.json();
        setJoinedBets(data);
        setJoinedBetsPage(1);
      } catch (err) {
        setJoinedBetsError(err.message);
      } finally {
        setLoadingJoinedBets(false);
      }
    }

    fetchOwnBets();
    fetchJoinedBets();
  }, [accessToken]);

  const ownBetsTotalPages = Math.max(
    1,
    Math.ceil(ownBets.length / BETS_PER_PAGE)
  );

  const joinedBetsTotalPages = Math.max(
    1,
    Math.ceil(joinedBets.length / BETS_PER_PAGE)
  );

  const visibleOwnBets = ownBets.slice(
    (ownBetsPage - 1) * BETS_PER_PAGE,
    ownBetsPage * BETS_PER_PAGE
  );

  const visibleJoinedBets = joinedBets.slice(
    (joinedBetsPage - 1) * BETS_PER_PAGE,
    joinedBetsPage * BETS_PER_PAGE
  );

  function goToPreviousOwnBetsPage() {
    setOwnBetsPage((page) => Math.max(page - 1, 1));
  }

  function goToNextOwnBetsPage() {
    setOwnBetsPage((page) => Math.min(page + 1, ownBetsTotalPages));
  }

  function goToPreviousJoinedBetsPage() {
    setJoinedBetsPage((page) => Math.max(page - 1, 1));
  }

  function goToNextJoinedBetsPage() {
    setJoinedBetsPage((page) => Math.min(page + 1, joinedBetsTotalPages));
  }

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
            <strong>{ownBets.length}</strong>
          </div>

          <div className="profile-stat">
            <span className="stat-label">Joined Bets</span>
            <strong>{joinedBets.length}</strong>
          </div>

          <div className="profile-stat">
            <span className="stat-label">Win Rate</span>
            <strong>Coming soon</strong>
          </div>
        </div>
      </section>

      <section className="profile-bets-section">
        <div className="profile-bets-card">
          <div className="profile-bets-card-header">
            <h2>Own Bets</h2>
            <span>{ownBets.length} total</span>
          </div>

          {loadingOwnBets && (
            <p className="profile-bets-message">Loading own bets...</p>
          )}

          {ownBetsError && <p className="profile-bets-error">{ownBetsError}</p>}

          {!loadingOwnBets && !ownBetsError && ownBets.length === 0 && (
            <p className="profile-bets-message">
              You have not created any bets yet.
            </p>
          )}

          {!loadingOwnBets &&
            !ownBetsError &&
            visibleOwnBets.map((bet) => (
              <div key={bet.id} className="profile-bet-row">
                <div className="profile-bet-info">
                  <strong>{bet.title}</strong>
                  <p>{bet.description}</p>
                </div>

                <span className="profile-bet-price">
                  €{bet.betPrice ?? bet.bet_price}
                </span>
              </div>
            ))}

          {!loadingOwnBets && !ownBetsError && ownBetsTotalPages > 1 && (
            <div className="profile-bets-pagination">
              <button
                type="button"
                onClick={goToPreviousOwnBetsPage}
                disabled={ownBetsPage === 1}
              >
                Previous
              </button>

              <span>
                Page {ownBetsPage} of {ownBetsTotalPages}
              </span>

              <button
                type="button"
                onClick={goToNextOwnBetsPage}
                disabled={ownBetsPage === ownBetsTotalPages}
              >
                Next
              </button>
            </div>
          )}
        </div>

        <div className="profile-bets-card">
          <div className="profile-bets-card-header">
            <h2>Joined Bets</h2>
            <span>{joinedBets.length} total</span>
          </div>

          {loadingJoinedBets && (
            <p className="profile-bets-message">Loading joined bets...</p>
          )}

          {joinedBetsError && (
            <p className="profile-bets-error">{joinedBetsError}</p>
          )}

          {!loadingJoinedBets && !joinedBetsError && joinedBets.length === 0 && (
            <p className="profile-bets-message">
              You have not joined any bets yet.
            </p>
          )}

          {!loadingJoinedBets &&
            !joinedBetsError &&
            visibleJoinedBets.map((bet) => (
              <div key={bet.id} className="profile-bet-row">
                <div className="profile-bet-info">
                  <strong>{bet.title}</strong>
                  <p>{bet.description}</p>
                </div>

                <span className="profile-bet-price">
                  €{bet.betPrice ?? bet.bet_price}
                </span>
              </div>
            ))}

          {!loadingJoinedBets &&
            !joinedBetsError &&
            joinedBetsTotalPages > 1 && (
              <div className="profile-bets-pagination">
                <button
                  type="button"
                  onClick={goToPreviousJoinedBetsPage}
                  disabled={joinedBetsPage === 1}
                >
                  Previous
                </button>

                <span>
                  Page {joinedBetsPage} of {joinedBetsTotalPages}
                </span>

                <button
                  type="button"
                  onClick={goToNextJoinedBetsPage}
                  disabled={joinedBetsPage === joinedBetsTotalPages}
                >
                  Next
                </button>
              </div>
            )}
        </div>
      </section>
    </div>
  );
}

export default ProfilePage;