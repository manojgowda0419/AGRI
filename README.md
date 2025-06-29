# Agrichain - Longest Substring Calculator

A local web application that calculates the length of the longest substring without repeating characters.

## Features

- **User Authentication**: Register and login with email validation
- **String Processing**: Calculate longest substring without repeating characters
- **User History**: Automatically logs all user inputs and outputs
- **Modern UI**: Browser-like interface matching the provided screenshots
- **Local Storage**: All data stored locally in browser's localStorage

## Files Structure

```
├── index.html          # Login page
├── register.html       # Registration page  
├── home.html          # Main input page (requires login)
├── result.html        # Results display page
├── styles.css         # Styling for all pages
├── auth.js           # Authentication logic
├── app.js            # Main application logic
├── result.js         # Results page logic
└── README.md         # This file
```

## How to Use

### 1. Setup
1. Download all files to a local folder
2. Open `index.html` in any modern web browser (Chrome, Firefox, Safari, Edge)

### 2. Registration
1. Click "Register" link on the login page
2. Fill in your details with a **valid email format** (e.g., user@example.com)
3. Password must be at least 6 characters
4. Click "Register" button

### 3. Login
1. Enter your registered email and password
2. Click "Login" button
3. You'll be redirected to the main application

### 4. Calculate Longest Substring
1. Enter any string in the input field
2. Click "Submit" button
3. View the result (length of longest substring without repeating characters)
4. Click "Back to Home" to perform another calculation

### 5. Logout
- Click "Logout" link in the top-right corner

## Algorithm Details

The application uses the **Sliding Window** technique:
- **Time Complexity**: O(n)
- **Space Complexity**: O(min(m,n)) where m is character set size

### Examples:
- Input: `"abcabcbb"` → Output: `3` (substring: "abc")
- Input: `"bbbbb"` → Output: `1` (substring: "b")
- Input: `"pwwkew"` → Output: `3` (substring: "wke")

## Data Storage

All user data is stored locally in your browser using localStorage:
- **User accounts**: Email, password, registration date
- **Calculation history**: Input strings, outputs, timestamps
- **Session data**: Current logged-in user

## Security Notes

⚠️ **This is a demo application for local use only:**
- Passwords are stored in plain text (not recommended for production)
- No server-side validation
- Data is only stored locally in browser

## Browser Compatibility

- ✅ Chrome (recommended)
- ✅ Firefox
- ✅ Safari
- ✅ Edge
- ✅ Any modern browser with localStorage support

## Troubleshooting

### Common Issues:

1. **"User not found" error**: Make sure you're using the exact email you registered with
2. **Page not loading**: Ensure all files are in the same folder
3. **Login redirect loop**: Clear browser localStorage and re-register
4. **Email validation fails**: Use proper email format (user@domain.com)

### Clear All Data:
```javascript
// Open browser console (F12) and run:
localStorage.clear();
```

## Features Demonstrated

✅ **Email Validation**: Only valid email formats accepted  
✅ **User Registration**: Secure user account creation  
✅ **User Authentication**: Login/logout functionality  
✅ **Data Logging**: All calculations automatically saved  
✅ **Session Management**: Maintains user login state  
✅ **Responsive Design**: Works on desktop and mobile  
✅ **Algorithm Implementation**: Efficient longest substring calculation  

---

**Ready to use!** Simply open `index.html` in your browser to get started. 