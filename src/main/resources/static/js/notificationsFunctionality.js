document.addEventListener("DOMContentLoaded", function () {

    let notifIcon = document.getElementById("notif-icon");
    let notifDropdown = document.getElementById("notif-dropdown");

    notifIcon.addEventListener("click", function (event) {
        event.preventDefault();
        loadNotifications();
    });

    function loadNotifications() {
        notifDropdown.innerHTML = ""; // Clear previous notifications

        if (notifications.length === 0) {
            notifDropdown.innerHTML = `<li><span class="dropdown-item text-muted">No new notifications</span></li>`;
            return;
        }

        notifications.forEach((notif) => {
            let notifItem = document.createElement("li");

            // Check the notification type and call corresponding function
            if (notif.type === "BASIC_NOTIFICATION") {
                notifItem.innerHTML = createBasicNotificationHTML(notif);
            } else if (notif.type === "FRIEND_REQUEST") {
                notifItem.innerHTML = createFriendRequestNotificationHTML(notif);
            }

            notifDropdown.appendChild(notifItem);
        });
    }

// Function to create basic notification HTML
    function createBasicNotificationHTML(notif) {
        return `
        <a class="dropdown-item d-flex align-items-start">
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

// Function to create friend request notification HTML
    function createFriendRequestNotificationHTML(notif) {
        return `
        <a class="dropdown-item d-flex align-items-start">
            <div class="me-2">
                <i class="bi ${getNotifIcon(notif.type)} text-primary"></i>
            </div>
            <div>
                <strong>${notif.sender_name}</strong><br>
                <span class="text-muted small">${notif.message}</span>
                <div class="mt-2">
                    <button class="btn btn-sm btn-success" onclick="handleFriendRequest('accept', ${notif.sender_id})">Accept</button>
                    <button class="btn btn-sm btn-danger ms-2" onclick="handleFriendRequest('reject', ${notif.sender_id})">Reject</button>
                </div>
            </div>
        </a>
    `;
    }

// Function to handle friend request accept/reject



// Returns an icon based on notification type
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


});

function handleFriendRequest(action, senderId) {
    if (action === 'accept') {
        console.log(`Accepted friend request from ${senderId}`);
        // Add your friend request accept logic here
    } else if (action === 'reject') {
        console.log(`Rejected friend request from ${senderId}`);
        // Add your friend request reject logic here
    }
}
