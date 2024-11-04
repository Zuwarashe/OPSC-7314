const admin = require('firebase-admin');
const serviceAccount = require('./config/serviceAccountKey.json');

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

function sendNewRecipeNotification(recipeName) {
    const message = {
        notification: {
            title: 'New Recipe Available!',
            body: `Someone shared a new recipe: ${recipeName}`
        },
        topic: 'public_recipes'
    };

    admin.messaging().send(message)
        .then(response => {
            console.log('Successfully sent message:', response);
        })
        .catch(error => {
            console.log('Error sending message:', error);
        });
}

module.exports = { sendNewRecipeNotification };
