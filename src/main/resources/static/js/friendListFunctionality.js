
document.addEventListener("DOMContentLoaded", function () {
    // Attach event listeners to all "Unfriend" buttons
    document.querySelectorAll('.unfriend-btn').forEach(button => {
        button.addEventListener('click', function () {
            const friendId = button.getAttribute('data-friend-id');

            fetch('/remove-friend', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: friendId
            })
                .then(response => {
                    if (response.ok) {
                        // Remove the friend item from the DOM
                        button.closest('.friend-item').remove();
                        alert('Friend removed successfully!');
                    } else {
                        alert('Failed to remove friend.');
                    }
                })
                .catch(error => {
                    console.error('Error removing friend:', error);
                });
        });
    });
});