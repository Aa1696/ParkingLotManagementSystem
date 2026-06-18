const API_BASE = (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') 
    ? 'http://localhost:8080' 
    : window.location.origin;

// DOM Elements
const loginSection = document.getElementById('login-section');
const dashboardSection = document.getElementById('dashboard-section');
const loginForm = document.getElementById('login-form');
const loginBtn = document.getElementById('login-btn');
const loginError = document.getElementById('login-error');
const logoutBtn = document.getElementById('logout-btn');

const capacityContainer = document.getElementById('capacity-container');
const entryForm = document.getElementById('entry-form');
const entryResult = document.getElementById('entry-result');
const exitForm = document.getElementById('exit-form');
const exitResult = document.getElementById('exit-result');

const paymentModal = document.getElementById('payment-modal');
const paymentForm = document.getElementById('payment-form');
const paymentAmount = document.getElementById('payment-amount');
const paymentInvoiceId = document.getElementById('payment-invoice-id');
const paymentResult = document.getElementById('payment-result');
const cancelPaymentBtn = document.getElementById('cancel-payment-btn');

// State
let jwtToken = localStorage.getItem('jwtToken');

// Initialization
function init() {
    if (jwtToken) {
        showDashboard();
    } else {
        showLogin();
    }
}

// Utility: API Fetch with Auth
async function apiFetch(endpoint, options = {}) {
    if (!options.headers) options.headers = {};
    if (jwtToken) {
        options.headers['Authorization'] = `Bearer ${jwtToken}`;
    }
    options.headers['Content-Type'] = 'application/json';

    const response = await fetch(`${API_BASE}${endpoint}`, options);
    if (response.status === 401) {
        // Token expired or invalid
        logout();
        throw new Error('Unauthorized');
    }
    
    if (!response.ok) {
        let errMessage = response.statusText;
        try {
            const errData = await response.json();
            if(errData.message) errMessage = errData.message;
        } catch(e) {}
        throw new Error(errMessage);
    }
    
    return response.json();
}

// UI Navigation
function showLogin() {
    loginSection.classList.remove('hidden');
    dashboardSection.classList.add('hidden');
}

function showDashboard() {
    loginSection.classList.add('hidden');
    dashboardSection.classList.remove('hidden');
    loadCapacity();
}

function logout() {
    jwtToken = null;
    localStorage.removeItem('jwtToken');
    showLogin();
}

function showSpinner(btn) {
    btn.querySelector('.btn-text').classList.add('hidden');
    btn.querySelector('.spinner').classList.remove('hidden');
    btn.disabled = true;
}

function hideSpinner(btn, text) {
    btn.querySelector('.btn-text').classList.remove('hidden');
    btn.querySelector('.spinner').classList.add('hidden');
    btn.disabled = false;
}

// Event Listeners

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    loginError.classList.add('hidden');
    showSpinner(loginBtn);
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const data = await apiFetch('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
        
        jwtToken = data.token;
        localStorage.setItem('jwtToken', jwtToken);
        showDashboard();
    } catch (err) {
        loginError.textContent = err.message || 'Login failed';
        loginError.classList.remove('hidden');
    } finally {
        hideSpinner(loginBtn);
    }
});

logoutBtn.addEventListener('click', logout);

// Capacity
async function loadCapacity() {
    try {
        // Fetch lot 1 capacity
        const data = await apiFetch('/api/capacity/lot');
        capacityContainer.innerHTML = `
            <div class="stat-box">
                <div class="count">${data.totalSpots}</div>
                <div class="label">Total Spots</div>
            </div>
            <div class="stat-box">
                <div class="count">${data.occupiedSpots}</div>
                <div class="label">Occupied</div>
            </div>
            <div class="stat-box">
                <div class="count" style="color: var(--success-color)">${data.availableSpots}</div>
                <div class="label">Available</div>
            </div>
        `;
    } catch (err) {
        capacityContainer.innerHTML = `<div class="error-text">Failed to load capacity</div>`;
    }
}

// Entry Gate
entryForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = entryForm.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.textContent = 'Processing...';
    entryResult.classList.add('hidden');

    const gateId = document.getElementById('gateId').value;
    const payload = {
        licensePlate: document.getElementById('licensePlate').value,
        vehicleType: document.getElementById('vehicleType').value,
        ownerName: document.getElementById('ownerName').value || null,
        email: document.getElementById('email').value || null
    };

    try {
        const ticket = await apiFetch(`/api/entry/${gateId}`, {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        
        entryResult.textContent = `Ticket Created Successfully!\nTicket ID: ${ticket.ticketId}\nSpot: ${ticket.spotNumber}\nEntry Time: ${new Date(ticket.entryTime).toLocaleString()}`;
        entryResult.className = 'result-box';
        entryForm.reset();
        document.getElementById('gateId').value = gateId;
        loadCapacity(); // Refresh capacity
    } catch (err) {
        entryResult.textContent = `Error: ${err.message}`;
        entryResult.className = 'result-box error';
    } finally {
        btn.disabled = false;
        btn.textContent = 'Generate Ticket';
        entryResult.classList.remove('hidden');
    }
});

// Global state for exit process
let currentExitTicketId = null;
let currentExitEmail = null;

// Exit Gate
exitForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = exitForm.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.textContent = 'Calculating...';
    exitResult.classList.add('hidden');

    const gateId = document.getElementById('exitGateId').value;
    const ticketId = document.getElementById('ticketId').value;
    currentExitTicketId = ticketId;

    try {
        const invoice = await apiFetch(`/api/exit/${gateId}/ticket/${ticketId}`, {
            method: 'POST'
        });
        
        currentExitEmail = invoice.email;
        
        exitResult.textContent = `Invoice Generated!\nInvoice ID: ${invoice.invoiceId}\nAmount Due: $${invoice.totalAmount.toFixed(2)}\nDuration: ${Math.floor(invoice.durationMinutes/60)}h ${invoice.durationMinutes%60}m`;
        exitResult.className = 'result-box';
        
        // Open payment modal
        paymentInvoiceId.value = invoice.invoiceId;
        paymentAmount.textContent = `$${invoice.totalAmount.toFixed(2)}`;
        paymentResult.classList.add('hidden');
        paymentModal.classList.remove('hidden');
        
    } catch (err) {
        exitResult.textContent = `Error: ${err.message}`;
        exitResult.className = 'result-box error';
    } finally {
        btn.disabled = false;
        btn.textContent = 'Process Exit';
        exitResult.classList.remove('hidden');
    }
});

// Payment Processing
paymentForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = paymentForm.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.textContent = 'Processing...';
    paymentResult.classList.add('hidden');

    const invoiceId = paymentInvoiceId.value;
    const paymentMethod = document.getElementById('payment-method').value;

    try {
        const paymentRes = await apiFetch(`/api/payments/process`, {
            method: 'POST',
            body: JSON.stringify({
                invoiceId: invoiceId,
                ticketId: currentExitTicketId,
                paymentMethod: paymentMethod,
                email: currentExitEmail
            })
        });
        
        paymentResult.textContent = `Payment Successful!\nTransaction ID: ${paymentRes.transactionId}\nStatus: ${paymentRes.status}`;
        paymentResult.className = 'result-box';
        
        setTimeout(() => {
            paymentModal.classList.add('hidden');
            exitForm.reset();
            exitResult.classList.add('hidden');
            loadCapacity(); // Refresh capacity
        }, 3000);
        
    } catch (err) {
        paymentResult.textContent = `Error: ${err.message}`;
        paymentResult.className = 'result-box error';
    } finally {
        btn.disabled = false;
        btn.textContent = 'Confirm Payment';
        paymentResult.classList.remove('hidden');
    }
});

cancelPaymentBtn.addEventListener('click', () => {
    paymentModal.classList.add('hidden');
});

// Start
init();
