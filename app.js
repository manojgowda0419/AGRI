// Check authentication and load user info
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
});

/**
 * Find the longest substring without repeating characters
 * @param {string} s - Input string
 * @return {object} - Object containing the longest substring and its length
 */
function findLongestSubstring(s) {
    if (!s || s.length === 0) {
        return { substring: "", length: 0 };
    }
    
    let maxLength = 0;
    let maxStart = 0;
    let left = 0;
    let charSet = new Set();
    
    // Sliding window approach
    for (let right = 0; right < s.length; right++) {
        // If character is already in the set, shrink window from left
        while (charSet.has(s[right])) {
            charSet.delete(s[left]);
            left++;
        }
        
        // Add current character to set
        charSet.add(s[right]);
        
        // Update maximum length and starting position
        if (right - left + 1 > maxLength) {
            maxLength = right - left + 1;
            maxStart = left;
        }
    }
    
    const longestSubstring = s.substring(maxStart, maxStart + maxLength);
    return { substring: longestSubstring, length: maxLength };
}

// Log calculation to user's history
function logCalculation(input, result) {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    const allUsers = JSON.parse(localStorage.getItem('agrichainUsers') || '[]');
    
    const calculation = {
        id: Date.now().toString(),
        input: input,
        output: result.length,
        substring: result.substring,
        timestamp: new Date().toISOString()
    };
    
    // Update current user's calculations
    currentUser.calculations = currentUser.calculations || [];
    currentUser.calculations.push(calculation);
    
    // Update user in the users array
    const userIndex = allUsers.findIndex(u => u.id === currentUser.id);
    if (userIndex !== -1) {
        allUsers[userIndex] = currentUser;
    }
    
    // Save back to localStorage
    localStorage.setItem('currentUser', JSON.stringify(currentUser));
    localStorage.setItem('agrichainUsers', JSON.stringify(allUsers));
    
    console.log('Calculation logged:', calculation);
}

// Handle string form submission
const stringForm = document.getElementById('stringForm');
if (stringForm) {
    stringForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const input = document.getElementById('stringInput').value;
        
        if (!input.trim()) {
            alert('Please enter a string!');
            return;
        }
        
        // Calculate longest substring
        const result = findLongestSubstring(input);
        
        // Log the calculation
        logCalculation(input, result);
        
        // Store result for the result page
        localStorage.setItem('lastCalculation', JSON.stringify({
            input: input,
            result: result,
            timestamp: new Date().toISOString()
        }));
        
        // Redirect to result page
        window.location.href = 'result.html';
    });
} 