const readline = require('readline');

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

// Create readline interface for user input
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// Ask user for input
rl.question('Enter a string: ', (input) => {
    const result = findLongestSubstring(input);
    console.log(`The longest substring without repeating letters for "${input}" is "${result.substring}", for which the length is ${result.length}.`);
    rl.close();
}); 