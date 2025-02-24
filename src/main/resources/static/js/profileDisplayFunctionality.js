document.addEventListener("DOMContentLoaded", function () {
    const addFriendBtn = document.getElementById('addFriendBtn');

    if (addFriendBtn) {
        addFriendBtn.addEventListener('click', () => {
            console.log('Add Friend button clicked');

            // Create the notification payload
            const notification = {
                sender_id: userId, // Current user's ID
                sender_name: username, // Current user's name
                recipient_id: displayUserId, // Profile user's ID
                type: 'FRIEND_REQUEST',
                message: `Friend request from ${username}`
            };

            // Send the notification via WebSocket
            if (stompClientNotif && stompClientNotif.connected) {
                stompClientNotif.send("/app2/add-notification", {}, JSON.stringify(notification));
                alert('Friend request sent!');
            } else {
                console.error('WebSocket connection is not established.');
                alert('Failed to send friend request. Please try again.');
            }
        });
    }
});