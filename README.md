Instructions for Testing:
Before running the application, please note that an API key is required to successfully make requests to the Spoonacular API. In the ApiClient class, you will find a placeholder for the API_KEY. Testers must input a valid API key in the API_KEY constant before proceeding. 

available key : 2acf8c972dba47449275230ecdf43291

This key can also be obtained by signing up on the Spoonacular API platform. Once the key is in place, the app will be able to authenticate and retrieve recipe data as intended.




# Tsea Africa

### **Purpose of the App**
_Tsea Africa: Your Passport to the Diverse Flavors of Africa_ is a unique recipe creation app that brings the rich and diverse culinary traditions of Africa to your fingertips. The app allows users to explore and create meals from various African cultures, providing a colorful and immersive experience. Each cultural cuisine is color-coded, offering a vibrant and intuitive way to explore African flavors. The app also includes features to help with grocery shopping, macro counting, and AI-driven assistance to make meal planning easier.

---

### **Key Features**
- **Cultural Recipes**: Color-coded sections representing different African cultures help users easily navigate and explore various culinary traditions, from North African tagines to South African braais.
- **Grocery Shopping Function**: Automatically generated shopping lists based on selected recipes, with AI-driven suggestions to streamline grocery shopping and save time.
- **Macro Counter**: Nutritional tracking for each recipe, allowing users to monitor their intake of macronutrients such as carbohydrates, proteins, and fats, and helping them meet their dietary goals.
- **User-Generated Recipes**: Users can create and share their own recipes, and browse a dedicated section featuring recipes created by others for inspiration and community engagement.
- **Multilingual Support**: Initial support for Afrikaans and English ensures accessibility for a broader audience, promoting inclusivity in culinary exploration.

---

## **Design Considerations**

### **UI/UX Design**
- **User-Focused Layout**: The app was designed with a focus on simplicity and user-friendliness, ensuring an intuitive navigation experience that caters to users of all ages and tech-savviness.
- **Consistency**: The app maintains consistent design patterns and interactions across all screens, making it easy for users to familiarize themselves with the app and feel confident while navigating.
- **Responsive Design**: The app ensures compatibility and ease of use on a range of devices, from mobile phones to tablets, providing a seamless experience across platforms.

### **Technical Design**
- **Backend Architecture**: The app integrates third-party APIs for nutritional information and AI-driven recommendations, enhancing user experience and providing accurate, up-to-date information.
- **Security**: User data is securely stored using Firebase or a similar cloud-based solution, prioritizing user privacy and data protection.
- **Scalability**: The codebase is modular, facilitating easier feature additions and maintenance in future versions to keep the app evolving with user needs.

---

## **CI/CD with GitHub Actions**
To streamline our development process, we use GitHub Actions for Continuous Integration and Continuous Deployment (CI/CD). This setup allows us to maintain high-quality code and deliver updates to users efficiently.

### **GitHub Actions Workflow Overview**
- **Automated Builds**: On each commit, the code is automatically built to ensure there are no compilation errors, providing immediate feedback to developers.
- **Automated Testing**: Unit tests are executed to validate that existing functionalities work as expected, ensuring robustness and reliability.
- **Automated Signing**: Our CI pipeline securely accesses a KeyStore file to sign the application, which is essential for releasing on Android and maintaining security standards.
- **Artifact Storage**: After building, the app's output files are uploaded as artifacts, making it easy for testers and contributors to download and review each build.

### **CI/CD Pipeline Details**
The GitHub Actions workflow file (`.github/workflows/build.yml`) is configured to:
- Utilize environment secrets to securely store sensitive information like passwords and API keys, enhancing security.
- Build the app using Gradle, with environment variables to manage secure signing efficiently.
- Store build artifacts for easy access post-build, facilitating testing and distribution.

---

## **Installation Instructions**
To install Tsea Africa, follow these steps:
1. Download the latest release from the [Releases](https://github.com/your-username/your-repo/releases) section.
2. Transfer the APK file to your Android device.
3. Open the APK file to install the app, and follow any on-screen prompts to complete the installation.

---

## **Usage**
1. **Home Page (Dashboard)**: Upon logging in, users are greeted by an overview of their meal plan, allowing quick access to current meal plans, grocery lists, and recently viewed recipes.
2. **Meal Plan Creation**: Users can browse color-coded sections based on African cultures to add meals to their plans, adjusting according to dietary preferences and macro requirements.
3. **Recipe Browsing**: Users can explore a wide range of user-generated and curated recipes, filtering by culture, ingredient, or dietary preference, promoting exploration and culinary creativity.

---

## **Release Notes**

### **Version 1.0**
_Released on: November 1, 2024_

#### **New Features and Updates**
- **Color-Coded Recipe Sections**: Introduced a vibrant way to navigate through different African cuisines, enhancing user experience.
- **Grocery Shopping Integration**: Users can now automatically generate shopping lists based on selected recipes, streamlining the shopping process.
- **AI-Driven Grocery Planning**: AI suggestions help users optimize their grocery shopping experience by recommending items based on previous selections and dietary needs.
- **User-Generated Recipes**: Added functionality for users to create and share their own recipes within the app, fostering community engagement.

#### **Innovations Added**
- **Live Photo Uploads**: Users can now upload live photos of meals, enhancing the recipe-sharing experience with visual context.
- **Biometric Access**: Enhanced security feature allowing users to log in using biometric authentication for added convenience.
- **Macro Counter**: Nutritional tracking feature helps users monitor their carbohydrate, protein, and fat intake, empowering them to make healthier choices.
- **Multilingual Support**: Initial support for Afrikaans and English, making the app accessible to a broader audience.

### **Previous Releases**
- **Version 0.1**: Initial prototype focusing on basic recipe browsing and a single cultural cuisine.

---

## **Screenshots**
- **Home Screen**
- 
- ![Screenshot 2024-11-04 234755](https://github.com/user-attachments/assets/8256b659-8b5c-47ea-8e9c-b37f9d7f8d80)

  
- **Recipe Browsing**
-
-     ![Screenshot 2024-11-04 234828](https://github.com/user-attachments/assets/6e6ac19d-b972-4c64-9460-84e8b9410307)

  ![Recipe Browsing](https://github.com/user-attachments/assets/4971a493-57d7-4561-96a7-818517f66c00)
  
- **Recipe Adding**

- 
  ![Recipe Adding](https://github.com/user-attachments/assets/bc64c75a-c8c0-49fa-89a0-44d66817fead)

- **Language Change Feature**

- 
  ![Language Change](https://github.com/user-attachments/assets/368ac016-f156-402c-b084-8c276ff1c093)

- **Live Picture Upload**

- 
  ![Live Picture Upload](https://github.com/user-attachments/assets/921d3ac5-81a5-4773-addb-ae861c742786)
  ![Live Picture Preview](https://github.com/user-attachments/assets/56de9d84-dbf4-442e-9223-a8acb76d36eb)

- **Biometric Access**
- 
  ![Biometric Access](https://github.com/user-attachments/assets/1678bda7-3e35-4fe0-a6c4-613cb1933059)

---

## **Future Development Plans**
We aim to add the following features in future versions:
- **Expanded Language Support**: Adding more languages to reach a wider audience, promoting inclusivity in culinary exploration.
- **Enhanced AI Features**: Further integration of AI to improve user experience in meal planning, recipe suggestions, and dietary recommendations.
- **Social Features**: Implementing community features to enhance user interaction, such as forums or social sharing capabilities to share cooking experiences and tips.

---

## **Contributing**
We welcome contributions! Feel free to submit issues, feature requests, or pull requests. Please refer to our [Contributing Guide](./CONTRIBUTING.md) for more information on how to get involved.

---

## **License**
This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

---

### Suggested Edits/Improvements:
1. **Enhanced Descriptions**: The descriptions of features and technical aspects can be elaborated to convey their importance and functionality better.
2. **Engagement Encouragement**: Consider adding a section that encourages users to provide feedback or share their cooking experiences within the app to foster community.
3. **Links to Resources**: Adding links to relevant resources, such as a blog, documentation, or social media pages, can increase user engagement and support.
