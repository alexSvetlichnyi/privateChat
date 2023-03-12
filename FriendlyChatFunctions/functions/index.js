const functions = require("firebase-functions");

/*exports.emojify =
    functions.database.ref('/messages/{pushId}/text')
    .onWrite(( change,context) => {
        // Database write events include new, modified, or deleted
        // database nodes. All three types of events at the specific
        // database path trigger this cloud function.
        // For this function we only want to emojify new database nodes,
        // so we'll first check to exit out of the function early if
        // this isn't a new message.

        // !event.data.val() is a deleted event
        // event.data.previous.val() is a modified event
        const message = change.after.val();

        // Now we begin the emoji transformation
            console.log("emojifying!");
            console.log('Retrieved message content: ', message);
            console.log('Retrieved message text: ', message.text);
            // Get the value from the 'text' key of the message
            const emojifiedText = emojifyText(message);

            console.log(emojifiedText);
            // Return a JavaScript Promise to update the database node
            return message.set(emojifiedText);
    });

// Returns text with keywords replaced by emoji
// Replacing with the regular expression /.../ig does a case-insensitive
// search (i flag) for all occurrences (g flag) in the string
function emojifyText(text) {
    var emojifiedText = text;
    emojifiedText = emojifiedText.replace(/\blol\b/ig, "😂");
    emojifiedText = emojifiedText.replace(/\bcat\b/ig, "😸");
    return emojifiedText;
}*/
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('/messages/{pushId}').onWrite((change, context) => {
    console.log('Push notification event triggered');

    //  Get the current value of what was written to the Realtime Database.
    const valueObject = change.after.val();

    // Create a notification
    const payload = {
        notification: {
            title: valueObject.name,
            body: valueObject.text,
            tag: valueObject.text,
            sound: "default"
        }
    };

    //Create an options object that contains the time to live for the notification and the priority
//    const options = {
//        priority: "high",
//        timeToLive: 60 * 60 * 24
//    };

    return admin.messaging().sendToTopic("messages", payload);
});
