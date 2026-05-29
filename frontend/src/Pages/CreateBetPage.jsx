import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../design/CreateBetPage.css";

function CreateBetPage() {
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [betPrice, setBetPrice] = useState("");
  const [betEndsAt, setBetEndsAt] = useState("");
  const [error, setError] = useState("");

async function handleSubmit(e) {
  e.preventDefault();

  const gamblerId = localStorage.getItem("gamblerId");
  const accessToken = localStorage.getItem("accessToken");

  if (!accessToken) {
    navigate("/login");
    return;
  }

  if (!gamblerId) {
    setError("Could not find user id. Please log in again.");
    return;
  }

  setError("");

  try {
    const response = await fetch(`/api/Bets/CreateBet?gamblerId=${gamblerId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify({
        title,
        description,
        betPrice: Number(betPrice),
        betEndsAt: new Date(betEndsAt).toISOString(),
      }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Failed to create bet");
    }

    const data = await response.json();

    if (!data.checkoutUrl) {
      throw new Error("No Stripe checkout URL returned");
    }

    window.location.href = data.checkoutUrl;
  } catch (err) {
    setError(err.message);
  }
}

  return (
    <div className="create-bet-page">
      <div className="create-bet-card">
        <div className="create-bet-header">
          <h1>Create a Bet</h1>
          <p>Start a new bet and let others join.</p>
        </div>

        <form onSubmit={handleSubmit} className="create-bet-form">
          <div className="form-group">
            <label>Title</label>
            <input
              type="text"
              placeholder="Will Arsenal win?"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Description</label>
            <textarea
              placeholder="Describe what people are betting on..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Bet price</label>
            <input
              type="number"
              step="0.01"
              placeholder="5.00"
              value={betPrice}
              onChange={(e) => setBetPrice(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Bet ends at</label>
            <input
              type="datetime-local"
              value={betEndsAt}
              onChange={(e) => setBetEndsAt(e.target.value)}
              required
            />
          </div>

          {error && <p className="create-bet-error">{error}</p>}

          <div className="create-bet-actions">
            <button
              type="button"
              className="create-bet-cancel"
              onClick={() => navigate("/")}
            >
              Cancel
            </button>

            <button type="submit" className="create-bet-submit">
              Continue to Payment
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default CreateBetPage;