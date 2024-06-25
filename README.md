Test Case 1: Registration Test Test Case ID: TC-001 Test Case Name: Should register a new user Preconditions: The user is on the registration page. Steps:

Intercept the GET request to http://localhost:3001/events. Visit http://localhost:3000/. Enter user12a in the text input field. Enter testPassword1 in the password input field. Set the login item in localStorage to user12a. Click the "log in" link. Intercept the GET request to http://localhost:3001/events. Verify that the URL includes http://localhost:3000/profilepage. Expected Result: The user is redirected to the profile page after registration.

Test Case 2: Weight Test Test Case ID: TC-002 Test Case Name: Should create weight Preconditions: The user is on the profile page. Steps:

Intercept the GET request to http://localhost:3001/events. Visit http://localhost:3000/profilepage. Click the profile button. Enter 75 in the weight input field. Check the radio button with the id #man. Click the "Save" button. Expected Result: The weight is saved and associated with the user profile.

Test Case 3: Calendar Functionality Test Test Case ID: TC-003 Test Case Name: Should create and verify an event Preconditions: The user is on the calendar page. Steps:

Visit http://localhost:3000/schedule. Click the "Day" button. Click the "Create" button to create a new workout. Click the "Exercise" button. Click the "press" button. Click the "Create" button to finalize the event. Click the "Month" button to verify the event in the monthly view. Expected Result: The workout event is created and can be seen in the monthly view of the calendar.
