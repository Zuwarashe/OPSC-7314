const admin = require("firebase-admin");
const serviceAccount = require("./config/serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://tseaafricadb-532c4-default-rtdb.firebaseio.com"
});

// Sample code to send a message
const message = {
    notification: {
        title: "New Recipe Available!",
        body: "Check out the latest public recipe."
    },
    topic: "public_recipes"
};

admin.messaging().send(message)
    .then((response) => {
        console.log("Successfully sent message:", response);
    })
    .catch((error) => {
        console.log("Error sending message:", error);
    });
