import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../design/CreateBetPage.css";

function CreateBetPage() {
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [betPrice, setBetPrice] = useState("");
  const [betEndsAt, setBetEndsAt] = useState("");

  const [betOptions, setBetOptions] = useState(["", ""]);

  const [error, setError] = useState("");

  function handleOptionChange(index, value) {
    const updatedOptions = [...betOptions];
    updatedOptions[index] = value;
    setBetOptions(updatedOptions);
  }

  function addOption() {
    setBetOptions([...betOptions, ""]);
  }

  function removeOption(index) {
    if (betOptions.length <= 2) {
      setError("A bet needs at least 2 options.");
      return;
    }

    const updatedOptions = betOptions.filter((_, optionIndex) => optionIndex !== index);
    setBetOptions(updatedOptions);
  }

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

    const cleanedOptions = betOptions
      .map((option) => option.trim())
      .filter((option) => option.length > 0);

    if (cleanedOptions.length < 2) {
      setError("Please enter at least 2 valid bet options.");
      return;
    }

    const hasDuplicateOptions =
      new Set(cleanedOptions.map((option) => option.toLowerCase())).size !==
      cleanedOptions.length;

    if (hasDuplicateOptions) {
      setError("Bet options must be unique.");
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
          betOptions: cleanedOptions,
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
              min="0.01"
              max="99.99"
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

          <div className="form-group">
            <label>Bet options</label>

            {betOptions.map((option, index) => (
              <div className="bet-option-row" key={index}>
                <input
                  type="text"
                  placeholder={
                    index === 0
                      ? "Yes"
                      : index === 1
                      ? "No"
                      : `Option ${index + 1}`
                  }
                  value={option}
                  onChange={(e) => handleOptionChange(index, e.target.value)}
                  required
                />

                <button
                  type="button"
                  className="remove-option-button"
                  onClick={() => removeOption(index)}
                  disabled={betOptions.length <= 2}
                >
                  Remove
                </button>
              </div>
            ))}

            <button
              type="button"
              className="add-option-button"
              onClick={addOption}
            >
              + Add option
            </button>
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