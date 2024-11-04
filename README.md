Instructions for Testing:
Before running the application, please note that an API key is required to successfully make requests to the Spoonacular API. In the ApiClient class, you will find a placeholder for the API_KEY. Testers must input a valid API key in the API_KEY constant before proceeding. 

available key : 2acf8c972dba47449275230ecdf43291

This key can also be obtained by signing up on the Spoonacular API platform. Once the key is in place, the app will be able to authenticate and retrieve recipe data as intended.


# Tsea Africa



## Purpose of the App

Tsea Africa: Your Passport to the Diverse Flavors of Africa is a unique recipe creation app that brings the rich and diverse culinary traditions of Africa to your fingertips. The app allows users to explore and create meals from various African cultures, providing a colorful and immersive experience. Each cultural cuisine is color-coded, offering a vibrant and intuitive way to explore African flavors. The app also includes features to help with grocery shopping, macro counting, and AI-driven assistance to make meal planning easier.

### Key Features

- **Cultural Recipes**: Color-coded sections representing different African cultures help users easily navigate and explore various culinary traditions.
- **Grocery Shopping Function**: Automatically generated shopping lists based on selected recipes, with AI-driven suggestions for efficient grocery shopping.
- **Macro Counter**: Nutritional tracking for each recipe, helping users meet their dietary goals.
- **User-Generated Recipes**: Users can create and share their own recipes, browsing a section of recipes created by others for inspiration.
- **Multilingual Support**: Initial support for Afrikaans and English, ensuring accessibility for a wider audience.

---

## Design Considerations

When designing Tsea Africa, the following design principles and technical considerations were taken into account:

### UI/UX Design
- **User-Focused Layout**: The app was designed to prioritize simplicity and user-friendliness, with intuitive navigation.
- **Consistency**: The app maintains consistent design patterns and interactions across all screens, making it easy for users to familiarize themselves with the app.
- **Responsive Design**: Ensures compatibility and ease of use on a range of devices, from mobile phones to tablets.

### Technical Design
- **Backend Architecture**: The app integrates third-party APIs for nutritional information and AI-driven recommendations, enhancing user experience.
- **Security**: User data is securely stored using Firebase or a similar cloud-based solution.
- **Scalability**: The codebase is modular, making it easier to add features and maintain in future versions.

## CI/CD with GitHub Actions

To streamline our development process, we use GitHub Actions for Continuous Integration and Continuous Deployment (CI/CD). Our CI/CD pipeline is configured to automatically build and test the app when changes are pushed to the repository.

### GitHub Actions Workflow Overview

1. **Automated Builds**: On each commit, the code is automatically built to ensure there are no compilation errors.
2. **Automated Testing**: Unit tests are run to validate that existing functionalities work as expected.
3. **Automated Signing**: Our CI pipeline securely accesses a KeyStore file to sign the application, essential for releasing on Android.
4. **Artifact Storage**: After building, the app's output files are uploaded as artifacts, making it easy for testers to download and review each build.

#### CI/CD Pipeline Details
The GitHub Actions workflow file (`.github/workflows/build.yml`) is configured to:
- Use environment secrets to store sensitive information like passwords and API keys.
- Build the app using Gradle, with environment variables to handle secure signing.
- Store build artifacts for easy access post-build.

---

## Installation Instructions

To install Tsea Africa, follow these steps:
1. Download the latest release from the [Releases](https://github.com/your-username/your-repo/releases) section.
2. Transfer the APK file to your Android device and open it to install.

## Usage

1. **Home Page (Dashboard)**: Upon logging in, users are greeted by their meal plan overview, allowing quick access to current meal plans, grocery lists, and recently viewed recipes.
2. **Meal Plan Creation**: Users can browse color-coded sections based on African cultures to add meals to their plans, adjusting according to dietary preferences and macro requirements.
3. **Recipe Browsing**: Users can explore a wide range of user-generated and curated recipes, filtering by culture, ingredient, or dietary preference.

## Release Notes

### Version 1.0
_Released on: November 1, 2024_

#### New Features and Updates
- **Color-Coded Recipe Sections**: Introduced a vibrant way to navigate through different African cuisines.
- **Grocery Shopping Integration**: Users can now automatically generate shopping lists based on selected recipes.
- **AI-Driven Grocery Planning**: AI suggestions help users optimize their grocery shopping experience.
- **User-Generated Recipes**: Added functionality for users to create and share their own recipes within the app.

#### Innovations Added
- **Macro Counter**: Nutritional tracking feature that helps users monitor their carbohydrate, protein, and fat intake.
- **Multilingual Support**: Initial support for Afrikaans and English, making the app accessible to a broader audience.

### Previous Releases
- **Version 0.1**: Initial prototype focusing on basic recipe browsing and a single cultural cuisine.

---

## Screenshots

- **Home Screen**  
  ![Home Screen](images/home-screen.png)

- **Recipe Browsing**  
  ![Recipe Browsing](images/recipe-browsing.png)

## Future Development Plans

We aim to add the following features in future versions:
- **Expanded Language Support**: Adding more languages to reach a wider audience.
- **Enhanced AI Features**: Further integration of AI to improve user experience in meal planning.
- **Social Features**: Implementing community features to enhance user interaction and recipe sharing.

## Contributing

We welcome contributions! to submit issues, feature requests, or pull requests.

## License

This project is licensed under the MIT License.
