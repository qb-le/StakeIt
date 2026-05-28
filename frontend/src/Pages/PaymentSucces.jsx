import { Link } from "react-router-dom";
import "../design/PaymentPage.css";

function PaymentSuccess() {
  return (
    <div className="payment-page">
      <div className="payment-card">
        <h1>Payment successful</h1>
        <p>
          Your payment was completed. Your bet should now be active.
        </p>

        <Link to="/" className="payment-button">
          Go to bets
        </Link>
      </div>
    </div>
  );
}

export default PaymentSuccess;