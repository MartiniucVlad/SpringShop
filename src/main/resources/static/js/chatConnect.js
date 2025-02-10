document.addEventListener("DOMContentLoaded", function () {
    let messagesTable = document.getElementById("messages-table");
    let messageContent = document.getElementById("message-input");
    let sendButton = document.getElementById("send-button");

    let socket = null;
    let stompClient = null;

    // Automatically connect to the WebSocket when the page loads
    connect();

    // Event Listener for the Send Button
    sendButton.addEventListener("click", function (event) {
        event.preventDefault();
        sendMessage();
    });

    // Establish WebSocket Connection
    function connect() {
        socket = new SockJS("/chat"); // Matches the endpoint defined in WebSocketConfig
        stompClient = Stomp.over(socket);



        stompClient.connect({}, function () {
            console.log("Connected to WebSocket");

            // Subscribe to the public topic
            stompClient.subscribe("/topic/public", function (messageOutput) {
                showMessage(JSON.parse(messageOutput.body));
            });

            // Enable send button when connected
            sendButton.disabled = false;
        }, function (error) {
            console.error("WebSocket connection error:", error);
            sendButton.disabled = true;
        });
    }

    // Disconnect WebSocket Connection when the user leaves the page
    window.addEventListener("beforeunload", function () {
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        console.log("Disconnected from WebSocket");
    });

    // Send Message via WebSocket
    function sendMessage() {
        if (stompClient && stompClient.connected) {
            let message = {
                content: messageContent.value,
                senderId: userId,
                senderName: username,
                chatRoomId: publicChatId
            };
            console.log(message);
            stompClient.send("/app/chat", {}, JSON.stringify(message));
        } else {
            console.error("Cannot send message. WebSocket is not connected.");
        }
    }

    // Display Received Message in the Table
    function showMessage(message) {
        let newRow = messagesTable.insertRow();
        let cell = newRow.insertCell(0);
        cell.innerText = `[${message.timestamp}] ${message.senderName}: ${message.content}`;
    }
});
