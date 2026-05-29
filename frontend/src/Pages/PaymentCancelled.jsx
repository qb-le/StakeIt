import { Link } from "react-router-dom";
import "../design/PaymentPage.css";

function PaymentCancelled() {
  return (
    <div className="payment-page">
      <div className="payment-card">
        <h1>Payment cancelled</h1>
        <p>
          Your payment was cancelled, so the bet was not activated.
        </p>

        <Link to="/create-bet" className="payment-button">
          Try again
        </Link>
      </div>
    </div>
  );
}

export default PaymentCancelled;