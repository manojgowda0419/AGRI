// Check authentication and load result
document.addEventListener('DOMContentLoaded', function() {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    
    if (!currentUser) {
        window.location.href = 'index.html';
        return;
    }
    
    // Display user email
    const userEmailElement = document.getElementById('userEmail');
    if (userEmailElement) {
        userEmailElement.textContent = currentUser.email;
    }
    
    // Setup logout functionality
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('currentUser');
            window.location.href = 'index.html';
        });
    }
    
    // Load and display the calculation result
    const lastCalculation = JSON.parse(localStorage.getItem('lastCalculation'));
    
    if (!lastCalculation) {
        // If no calculation data, redirect to home
        window.location.href = 'home.html';
        return;
    }
    
    // Display all the details in bold in the main result area
    const resultValueElement = document.getElementById('resultValue');
    if (resultValueElement) {
        const input = lastCalculation.input;
        const substring = lastCalculation.result.substring;
        const length = lastCalculation.result.length;
        
        const detailText = `<strong>Entered string = ${input}</strong><br><strong>Repeating letters = ${substring}</strong><br><strong>Length = ${length}</strong>`;
        resultValueElement.innerHTML = detailText;
    }
    
    // Clear the separate details section since we're showing everything in main area
    const detailsTextElement = document.getElementById('detailsText');
    if (detailsTextElement) {
        detailsTextElement.textContent = '';
    }
    
    // Setup back to home button
    const backToHomeBtn = document.getElementById('backToHome');
    if (backToHomeBtn) {
        backToHomeBtn.addEventListener('click', function() {
            // Clear the last calculation and go back to home
            localStorage.removeItem('lastCalculation');
            window.location.href = 'home.html';
        });
    }
    
    // Optional: Log that user viewed the result
    console.log('Result displayed:', {
        input: lastCalculation.input,
        length: lastCalculation.result.length,
        substring: lastCalculation.result.substring,
        viewedAt: new Date().toISOString()
    });
}); 