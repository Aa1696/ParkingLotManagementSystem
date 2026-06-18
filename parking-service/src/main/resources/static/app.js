const API_BASE = '/api'; // Using relative path since it's served from the same host

document.addEventListener('DOMContentLoaded', () => {
    fetchCapacity();
    setInterval(fetchCapacity, 5000); // Poll capacity every 5 seconds
    setInterval(fetchNotifications, 3000); // Poll notifications every 3 seconds

    const form = document.getElementById('entryForm');
    form.addEventListener('submit', handleEntry);

    document.getElementById('btnNewEntry').addEventListener('click', () => {
        document.getElementById('ticketResult').classList.add('hidden');
        document.getElementById('entryForm').classList.remove('hidden');
        document.getElementById('entryForm').reset();
    });

    const exitForm = document.getElementById('exitForm');
    if (exitForm) {
        exitForm.addEventListener('submit', handleExit);
    }

    document.getElementById('closeEmailModal').addEventListener('click', () => {
        document.getElementById('emailModal').classList.add('hidden');
    });
});

let lastKnownEmails = [];

async function fetchNotifications() {
    try {
        const response = await fetch(`${API_BASE}/notifications/recent`);
        if (!response.ok) return;
        const emails = await response.json();
        
        // Check if there's a new email that wasn't in our last fetch
        if (emails.length > 0) {
            const latest = emails[0];
            const isNew = !lastKnownEmails.find(e => e.timestamp === latest.timestamp);
            if (isNew && lastKnownEmails.length > 0) {
                // Show modal!
                document.getElementById('emailContent').innerHTML = 
                    `<strong>To:</strong> ${latest.to}\n<strong>Subject:</strong> ${latest.subject}\n\n${latest.text}`;
                document.getElementById('emailModal').classList.remove('hidden');
            }
            lastKnownEmails = emails;
        }
    } catch (e) {
        // ignore errors for notification polling
    }
}

async function fetchCapacity() {
    try {
        const response = await fetch(`${API_BASE}/capacity/lot`);
        if (!response.ok) throw new Error('Failed to fetch capacity');
        const data = await response.json();
        
        const container = document.getElementById('capacityStats');
        container.innerHTML = `
            <div class="stat-box">
                <div class="stat-title">Total Spots</div>
                <div class="stat-value">${data.totalSpots}</div>
            </div>
            <div class="stat-box">
                <div class="stat-title">Available</div>
                <div class="stat-value available">${data.availableSpots}</div>
            </div>
            <div class="stat-box">
                <div class="stat-title">Occupied</div>
                <div class="stat-value ${data.availableSpots === 0 ? 'full' : ''}">${data.occupiedSpots}</div>
            </div>
        `;
    } catch (error) {
        console.error(error);
        // Fallback for demo if API isn't running yet
        const container = document.getElementById('capacityStats');
        container.innerHTML = `<div class="stat-box loading" style="color:var(--danger)">API Unavailable</div>`;
    }
}

async function handleEntry(e) {
    e.preventDefault();
    
    const licensePlate = document.getElementById('licensePlate').value;
    const vehicleType = document.getElementById('vehicleType').value;
    const ownerName = document.getElementById('ownerName').value;
    const email = document.getElementById('email').value;

    const btnText = document.querySelector('.btn-text');
    const loader = document.querySelector('.loader');
    
    btnText.classList.add('hidden');
    loader.classList.remove('hidden');

    try {
        // Assume Gate 1 for UI entry
        const response = await fetch(`${API_BASE}/entry/1`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ licensePlate, vehicleType, ownerName, email })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Failed to generate ticket');
        }

        const ticket = await response.json();
        
        // Show result
        document.getElementById('entryForm').classList.add('hidden');
        document.getElementById('ticketResult').classList.remove('hidden');
        
        document.getElementById('resTicketId').textContent = ticket.ticketId;
        document.getElementById('resSpot').textContent = ticket.spotNumber;
        document.getElementById('resFloor').textContent = `Floor ${ticket.floorNumber}`;
        document.getElementById('resTime').textContent = new Date(ticket.entryTime).toLocaleString();
        
        showToast('Ticket Generated Successfully!', 'success');
        fetchCapacity(); // Refresh capacity
        
    } catch (error) {
        console.error(error);
        showToast(error.message, 'error');
    } finally {
        btnText.classList.remove('hidden');
        loader.classList.add('hidden');
    }
}

function showToast(message, type) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type}`;
    
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

async function handleExit(e) {
    e.preventDefault();
    
    const ticketId = document.getElementById('exitTicketId').value;
    const btnText = document.querySelector('#btnExitSubmit .btn-text');
    const loader = document.querySelector('#btnExitSubmit .loader');
    
    btnText.classList.add('hidden');
    loader.classList.remove('hidden');

    try {
        // Assume Gate 2 for UI exit
        const exitResponse = await fetch(`${API_BASE}/exit/2/ticket/${ticketId}`, {
            method: 'POST'
        });

        if (!exitResponse.ok) {
            const errorText = await exitResponse.text();
            throw new Error(errorText || 'Failed to process exit');
        }

        const invoice = await exitResponse.json();
        
        showToast('Invoice generated. Redirecting to payment...', 'success');
        
        // Call payment-service to create Checkout Session
        const paymentResponse = await fetch(`${API_BASE}/payments/create-checkout-session`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                invoiceId: String(invoice.invoiceId),
                amount: invoice.totalAmount,
                currency: 'usd',
                ticketId: String(ticketId),
                email: invoice.email // Pass email retrieved from backend
            })
        });

        if (!paymentResponse.ok) {
            const errorText = await paymentResponse.text();
            throw new Error(errorText || 'Failed to initiate payment');
        }

        const paymentData = await paymentResponse.json();
        
        // Redirect to Stripe
        window.location.href = paymentData.url;
        
    } catch (error) {
        console.error(error);
        showToast(error.message, 'error');
    } finally {
        btnText.classList.remove('hidden');
        loader.classList.add('hidden');
    }
}
