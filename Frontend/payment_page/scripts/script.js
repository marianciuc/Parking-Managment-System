import {fetchSessionByPlate, fetchStripePublicKey, createPaymentIntent} from './apiCalls.js';




function convertMinutes(totalMinutes) {
    const days = Math.floor(totalMinutes / (24 * 60));
    const hours = Math.floor((totalMinutes % (24 * 60)) / 60);
    const minutes = totalMinutes % 60;

    if(!hours && !days){
        return  `${minutes} minutes`;
    }
    if(!days){
        return  `${hours} hours, ${minutes} minutes`
    }
    
    return `${days} days, ${hours} hours, ${minutes} minutes`;
}


let stripe; // Global Stripe instance
let elements; // Global Elements instance
let clientSecret; // Store client secret globally

// infoPlate.textContent = "ABC";
// infoTime.textContent = "DGE";
// infoAmount.textContent = "DD";
// infoCurrency.textContent = "USD";

// sessionInfo.style.display = "block";

// Handle plate form submission
plateForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const plateNumber = document.getElementById("plateNumber").value.trim();

    if (plateNumber) {
        fetchSessionByPlate(plateNumber).then(session => {
            if (session) {
                infoPlate.textContent = session.vehiclePlateNumber;
                infoTime.textContent = convertMinutes(session.durationInMinutes);
                infoAmount.textContent = session.totalAmount;
                infoCurrency.textContent = session.currency;
                sessionInfo.style.display = "block";

                fetchStripePublicKey().then(publicKey => {
                    if (publicKey) {
                        stripe = Stripe(publicKey); // Ensure we use the same instance
                        
                        createPaymentIntent(session.id, session.payment.amount, session.payment.currency.toUpperCase())
                        .then(secret => {
                            clientSecret = secret; // Store clientSecret globally
                            const appearance = { theme: 'flat' };
                            
                            elements = stripe.elements({ clientSecret, appearance }); // Create elements once
                            
                            const paymentElement = elements.create('payment');
                            paymentElement.mount('#payment-element');
                            confirmPayment.style.display = "block";
                        });
                    }
                });
            } else {
                console.error("No session data available");
            }
        });
    } else {
        alert("Please enter a valid plate number.");
    }
});

// Handle Stripe Payment Confirmation
document.getElementById("confirmPayment").addEventListener("click", async () => {
    if (!stripe || !elements) {
        alert("Payment system not initialized. Please try again.");
        return;
    }

    const { paymentIntent, error } = await stripe.confirmPayment({
        elements,
        confirmParams: {
            return_url: "https://localhost:5173/success.html", // Change to your success page
        }
    });

    if (error) {
        console.error("Payment failed:", error.message);
        alert("Payment failed. Please try again.");
    } else if (paymentIntent && paymentIntent.status === "succeeded") {
        console.log("Payment successful:", paymentIntent);
        alert("Payment successful!");
        // window.location.href = "https://localhost:5173/success.html";
    }
});
