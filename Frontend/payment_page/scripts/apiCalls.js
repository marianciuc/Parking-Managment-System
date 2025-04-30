const SERVER_URL = "https://marianciuc.works";

const use_mock = true;

const mockSessionDto = {
    id: '550e8400-e29b-41d4-a716-446655440000',
    durationInMinutes: 120,
    status: 'ACTIVE',
    totalPaid: 0,
    currency: 'USD',
    totalAmount: 5.00,
    vehiclePlateNumber: 'ABC123',
    vehicleId: '550e8400-e29b-41d4-a716-446655440001',
    parkingId: '550e8400-e29b-41d4-a716-446655440002',
    vehicleAccessList: 'VIP',
    payment: {
        amount: 100,
        currency: 'USD',
        remainingTime: 30
    },
    tariffName: 'Standard Tariff',
    tariffId: '550e8400-e29b-41d4-a716-446655440003'
};


export async function fetchSessionByPlate(plate) {
    if(use_mock) {
        console.log("Plate " + plate);
        return mockSessionDto;
    }
    try {
        const response = await fetch(`${SERVER_URL}/api/v1/sessions/find-by-plate?plate=${encodeURIComponent(plate)}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Session Data:', data);
        return data;
    } catch (error) {
        console.error('Error fetching session data:', error);
        return null;
    }
}

export async function fetchStripePublicKey() {
    if(use_mock){
        return "pk_test_51Qw8sY2VOTxSrLTXSJj6UzlMlSyfhwNbrhuKRnSDD297B2hJN5itMboQAmrlbEfvNLoJMExNad9hvVs6MSeE2Pji00UzRNG2sN"
    }
    try {

        const requestOptions = {
            method: "GET",
            redirect: "follow"
        };
        let key;

        const response = await fetch("https://marianciuc.works/api/v1/stripe/public-key", requestOptions)
            .then((response) => response.text())
            .then((result) => key = result )
            .catch((error) => console.error(error));
        // const response = await fetch(`${SERVER_URL}/api/v1/stripe/public-key`, {
        //     method: 'GET',
        //     headers: {
        //         'Accept': 'application/json'
        //     }
        // });
        console.log(key);

        console.log(response)
        if (!key) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        console.log('Stripe Public Key:', key);
        return key;
    } catch (error) {
        console.error('Error fetching Stripe public key:', error);
        return null;
    }
}


export async function createPaymentIntent(sessionId, amount, currency) {
    try {
        const response = await fetch(`${SERVER_URL}/api/v1/session-payments/${sessionId}/stripe`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ amount, currency })
        });

        console.log(response)
        
        if (!response.ok) {
            throw new Error(`Error creating PaymentIntent: ${response.status}`);
        }
        

        return response.text(); // Expecting { clientSecret: "..." }
    } catch (error) {
        console.error('Error creating PaymentIntent:', error);
        return null;
    }
}
