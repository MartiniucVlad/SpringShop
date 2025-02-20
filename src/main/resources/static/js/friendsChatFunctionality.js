document.addEventListener("DOMContentLoaded", function () {
    let friendsTable = document.getElementById("friends-table");
    let messagesTable = document.getElementById("messages-table");
    let messageContent = document.getElementById("message-input");
    let sendButton = document.getElementById("send-button");



    let socket = null;
    let stompClient = null;
    let currentChatRoomId = null;

    // Track unread messages for each friend
    let unreadMessages = {};
    connect();
    loadFriends()

    sendButton.addEventListener("click", function (event) {
        event.preventDefault();
        sendMessage();
    });

    // Load friends and subscribe to their chat rooms


    function loadFriends() {
        friendList.forEach(function (friend) {
            showFriend(friend);
            subscribeToChatRoom(friend.chatRoomId); // Subscribe to each friend's chat room
        });
    }

    function showFriend(friend) {
        let newRow = friendsTable.insertRow();
        let cell = newRow.insertCell(0);
        let dotCell = newRow.insertCell(1); // Cell for the green dot

        newRow.setAttribute('data-friend-id', friend.id);
        cell.innerText = `${friend.username}`;

        // Initialize unread messages for this friend
        unreadMessages[friend.id] = friend.nrUnreadUser;

        // Add a green dot if there are unread messages
        updateUnreadDot(friend.id, dotCell);

        // Add click event to select the friend
        newRow.addEventListener("click", function () {
            if (selectedFriendRow) {
                selectedFriendRow.classList.remove("selected-friend"); // Deselect previous friend
            }
            newRow.classList.add("selected-friend"); // Highlight selected friend
            selectedFriendRow = newRow;

            // Show the chat window
            messagesTableContainer.style.display = "block";
        });
    }

    function updateUnreadDot(friendId, dotCell) {
        if (unreadMessages[friendId] > 0) {
            // Show green dot and the number of unread messages
            dotCell.innerHTML = `<span style="color: green;">● ${unreadMessages[friendId]}</span>`;
        } else {
            dotCell.innerHTML = ''; // No dot or count
        }
    }

    friendsTable.addEventListener("click", function (event) {
        let row = event.target.closest('tr');
        if (row) {
            let friendId = row.getAttribute('data-friend-id'); // Retrieve the friend's ID
            fetch(`/friends-chat/${friendId}`)
                .then(response => response.json())
                .then(data => {
                    currentChatRoomId = data.chatRoomId;
                    const messages = JSON.parse(data.privateMessages);
                    loadMessages(messages); // Load messages for the selected friend
                    unreadMessages[friendId] = 0; // Reset unread messages for this friend
                    updateUnreadDot(friendId, row.cells[1]); // Update the green dot
                });
        }
    });


    // Establish WebSocket Connection
    function connect() {
        socket = new SockJS("/chat");
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function () {
            console.log("Connected to WebSocket");
        }, function (error) {
            console.error("WebSocket connection error:", error);
        });
    }

    // Function to subscribe to a chat room
    function subscribeToChatRoom(chatRoomId) {
        waitUntilConnected()
            .then(() => {
                if (stompClient && stompClient.connected) {
                    let destination = `/topic/private/${chatRoomId}`;
                    console

                    stompClient.subscribe(destination, function (messageOutput) {
                        let message = JSON.parse(messageOutput.body);
                            if (currentChatRoomId === chatRoomId) {
                                // If this is the active chat, show the message
                                showMessage(message);
                            } else {
                                // Otherwise, increment unread messages for this friend

                                let friendRow = document.querySelector(`tr[data-friend-id="${message.senderId}"]`);
                                if (friendRow) {
                                    let friendId = friendRow.getAttribute('data-friend-id'); // Retrieve the friend's ID
                                    unreadMessages[friendId]++;
                                    updateUnreadDot(friendId, friendRow.cells[1]);
                                }
                        }
                    });
                } else {
                    console.error("WebSocket not connected. Subscription failed.");
                }
            })
            .catch((error) => {
                console.error(error.message);
            });
    }

    // Function to load messages for a specific chat room
    function loadMessages(messages) {
        messagesTable.innerHTML = ""; // Clear the current messages
        if(messages.length === 0) {
            displayNoMessages();
        }
        else messages.forEach(message => showMessage(message));
    }

    // Function to wait until WebSocket is connected
    function waitUntilConnected(maxAttempts = 10, interval = 100) {
        return new Promise((resolve, reject) => {
            let attempts = 0;

            const intervalId = setInterval(() => {
                if (stompClient && stompClient.connected) {
                    clearInterval(intervalId); // Stop the interval
                    resolve(); // Resolve the promise when connected
                } else if (attempts >= maxAttempts) {
                    clearInterval(intervalId); // Stop the interval
                    reject(new Error("Failed to connect after maximum attempts.")); // Reject the promise
                } else {
                    attempts++;
                }
            }, interval);
        });
    }

    // Send Message via WebSocket
    function sendMessage() {
        if (stompClient && stompClient.connected && currentChatRoomId) {
            let message = {
                content: messageContent.value,
                senderId: userId,
                senderName: username,
                chatRoomId: currentChatRoomId
            };
            stompClient.send(`/app/private-chat/${currentChatRoomId}`, {}, JSON.stringify(message));
            messageContent.value = ""; // Clear the input field
        } else {
            console.error("Cannot send message. WebSocket is not connected or no chat room selected.");
        }
    }

    // Display Received Message in the Table
    function showMessage(message) {
        let newRow = messagesTable.insertRow();
        let cell = newRow.insertCell(0);

        if (userId == message.senderId) {
            cell.style.backgroundColor = "lightgreen";
            cell.style.fontWeight = "bold";
        }
        cell.innerText = `[${message.timestamp}] ${message.senderName}: ${message.content}`;

    }

    function displayNoMessages() {
        let noMessagesRow = messagesTable.insertRow();
        let noMessagesCell = noMessagesRow.insertCell(0);
        noMessagesCell.colSpan = 1; // Span the cell across all columns
        noMessagesCell.innerText = "No messages between users";
        noMessagesCell.style.textAlign = "center"; // Center the text
        noMessagesCell.style.fontStyle = "italic"; // Make the text italic
    }
});