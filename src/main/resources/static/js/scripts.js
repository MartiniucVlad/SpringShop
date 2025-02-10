
function sendPost(url, statusType) {


    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',

        }
    })
        .then(response => {
            if (response.ok) {
                document.getElementById('user-status').textContent = `Status: ${statusType}`;
                console.log('Status updated successfully!');
            } else {
                console.error('Error updating status.');
            }
        })
        .catch(error => console.error('Error:', error));
}

