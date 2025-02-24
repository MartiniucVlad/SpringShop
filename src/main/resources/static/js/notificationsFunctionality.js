let socketNotif = null;
let stompClientNotif = null;

document.addEventListener("DOMContentLoaded", function () {

    let notifIcon = document.getElementById("notif-icon");
    let notifDropdown = document.getElementById("notif-dropdown");


    connect();

    notifIcon.addEventListener("click", function (event) {
        event.preventDefault();
        loadNotifications();
    });

    function connect() {
        socketNotif = new SockJS("/notifications");
        stompClientNotif = Stomp.over(socketNotif);

        stompClientNotif.connect({}, function () {
            console.log("Connected to WebSocket for notifications");
            subscribeToNotifications();
        }, function (error) {
            console.error("WebSocket connection error:", error);
        });
    }

    function subscribeToNotifications() {
        waitUntilConnected().then(() => {
            let destination = `/topic2/${user.id}`;
            stompClientNotif.subscribe(destination, function (messageOutput) {
                let notif = JSON.parse(messageOutput.body);
                showNotification(notif);
            });
        }).catch((error) => {
            console.error(error.message);
        });
    }

    function loadNotifications() {
        notifDropdown.innerHTML = ""; // Clear previous notifications

        if (notifications.length === 0) {
            notifDropdown.innerHTML = `<li><span class="dropdown-item text-muted">No new notifications</span></li>`;
            return;
        }

        notifications.forEach((notif) => {
            let notifItem = document.createElement("li");

            if (notif.type === "BASIC_NOTIFICATION") {
                notifItem.innerHTML = createBasicNotificationHTML(notif);
            } else if (notif.type === "FRIEND_REQUEST") {
                notifItem.innerHTML = createFriendRequestNotificationHTML(notif);
            }

            notifDropdown.appendChild(notifItem);
        });
    }

    function showNotification(notif) {
        let notifItem = document.createElement("li");

        if (notif.type === "BASIC_NOTIFICATION") {
            notifItem.innerHTML = createBasicNotificationHTML(notif);
        } else if (notif.type === "FRIEND_REQUEST") {
            notifItem.innerHTML = createFriendRequestNotificationHTML(notif);
        }

        notifDropdown.prepend(notifItem);
    }

    function createBasicNotificationHTML(notif) {
        return `
            <a id="notif-${notif.id}" class="dropdown-item d-flex align-items-start">
                <div class="me-2">
                    <i class="bi ${getNotifIcon(notif.type)} text-primary"></i>
                </div>
                <div>
                    <strong>${notif.sender_name}</strong><br>
                    <span class="text-muted small">${notif.message}</span>
                </div>
            </a>
        `;
    }

    function createFriendRequestNotificationHTML(notif) {
        return `
            <a id="notif-${notif.id}" class="dropdown-item d-flex align-items-start">
                <div class="me-2">
                    <i class="bi ${getNotifIcon(notif.type)} text-primary"></i>
                </div>
                <div>
                    <strong>${notif.sender_name}</strong><br>
                    <span class="text-muted small">${notif.message}</span>
                    <div class="mt-2">
                        <button class="btn btn-sm btn-success" onclick="handleFriendRequest('accept', ${notif.sender_id}, ${notif.id})">Accept</button>
                        <button class="btn btn-sm btn-danger ms-2" onclick="handleFriendRequest('reject', ${notif.sender_id}, ${notif.id})">Reject</button>
                    </div>
                </div>
            </a>
        `;
    }

    function getNotifIcon(type) {
        switch (type) {
            case "MESSAGE":
                return "bi-chat-dots";
            case "FRIEND_REQUEST":
                return "bi-person-plus";
            case "ALERT":
                return "bi-exclamation-circle";
            default:
                return "bi-bell";
        }
    }

    function waitUntilConnected(maxAttempts = 10, interval = 100) {
        return new Promise((resolve, reject) => {
            let attempts = 0;

            const intervalId = setInterval(() => {
                if (stompClientNotif && stompClientNotif.connected) {
                    clearInterval(intervalId);
                    resolve();
                } else if (attempts >= maxAttempts) {
                    clearInterval(intervalId);
                    reject(new Error("Failed to connect after maximum attempts."));
                } else {
                    attempts++;
                }
            }, interval);
        });
    }
});

function handleFriendRequest(action, senderId, notifId) {
    let notifElement = document.getElementById(`notif-${notifId}`);

    if (notifElement) {
        // Remove the parent <li> element of the <a> element
        let listItem = notifElement.parentElement;
        if (listItem) {
            listItem.remove();
        }
    }

    fetch('/resolve-friend-notification', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action, senderId, notifId })
    })

}
