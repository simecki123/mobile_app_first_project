# Mobile App First Project

This is my first project for the "Mobile Application Development" course at the university.

## Functionality

This mobile app is developed using Kotlin in Android Studio. It allows users to search for GIFs using the Giphy API and displays them along with some details about the user who uploaded the GIF. Here's a brief overview of the app's functionality:

1. **Search GIFs:**
   - Users can enter a search term in the provided search bar.
   - The app uses the Giphy API to fetch a GIF related to the search term.

2. **Display GIFs:**
   - The app displays the searched GIFs in a grid layout.
   - It shows up to 5 GIFs at a time.

3. **Display GIF Names:**
   - The names of the displayed GIFs are shown below each GIF.

4. **Display User Details:**
   - Details about the user who uploaded the GIF are extracted from the Giphy API.
   - These details include avatar URL, banner image, profile URL, username, display name, description, Instagram URL, website URL, and verification status.

5. **Navigate to Details:**
   - Users can click on each displayed GIF to view more details.
   - The app opens a new activity, `GifDetailsActivity`, to show detailed information about the selected GIF and user.

6. **Stack-like Behavior:**
   - The app follows a stack-like behavior for storing GIF details.
   - The latest details are added to the top of the stack.

7. **View Details Activity:**
   - The `GifDetailsActivity` displays the details of a selected GIF.
   - It shows details such as avatar URL, banner image, profile URL, username, display name, description, Instagram URL, website URL, and verification status.

8. **Dynamic View Binding:**
   - Views are dynamically bound based on the number of GIFs fetched.
   - ImageView and TextView IDs are programmatically determined.

9. **Campfire:**
   - Simple view so you can rest by the campfire.

## How to Use

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/simecki123/mobile_app_first_project.git

