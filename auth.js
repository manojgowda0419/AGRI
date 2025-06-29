// Check if user is already logged in
document.addEventListener('DOMContentLoaded', function() {
    const currentUser = localStorage.getItem('currentUser');
    if (currentUser && (window.location.pathname.includes('index.html') || window.location.pathname === '/')) {
        window.location.href = 'home.html';
    }
});



// Show message function
function showMessage(elementId, message, type) {
    const messageElement = document.getElementById(elementId);
    messageElement.textContent = message;
    messageElement.className = `message ${type}`;
    messageElement.style.display = 'block';
    
    // Clear message after 5 seconds
    setTimeout(() => {
        messageElement.style.display = 'none';
    }, 5000);
}



// Login functionality - accepts ANY credentials
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        
        // Basic validation - just need some input
        if (!email) {
            showMessage('loginMessage', 'Please enter an email address!', 'error');
            return;
        }
        
        if (!password) {
            showMessage('loginMessage', 'Please enter a password!', 'error');
            return;
        }
        
        // Accept ANY credentials - no authentication check
        const user = {
            id: Date.now().toString(),
            name: 'User',
            email: email,
            password: password,
            loginAt: new Date().toISOString(),
            calculations: []
        };
        
        // Login always successful
        localStorage.setItem('currentUser', JSON.stringify(user));
        showMessage('loginMessage', 'Login successful! Redirecting...', 'success');
        
        // Redirect to home after 1 second
        setTimeout(() => {
            window.location.href = 'home.html';
        }, 1000);
    });
} 