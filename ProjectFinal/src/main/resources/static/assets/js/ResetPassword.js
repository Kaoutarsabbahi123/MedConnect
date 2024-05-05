document.getElementById('confirmButton').addEventListener('click', function(event) {
    event.preventDefault();

    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    if (password !== confirmPassword) {
        document.getElementById('confirmPasswordError').textContent = "Les mots de passe ne correspondent pas";
        document.getElementById('passwordForm').reset();
        return;
    }
    document.getElementById('confirmPasswordError').textContent = "";
    const formData = new FormData(document.getElementById('passwordForm'));
    fetch('/reinitialiser_mdp', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            const messageContainer = document.getElementById('messageContainer');
            messageContainer.innerHTML = "";
            const successMessage = document.createElement('p');
            successMessage.textContent = "Mot de passe réinitialisé avec succès";
            const loginLink = document.createElement('a');
            loginLink.textContent = "Cliquez ici pour vous connecter";
            loginLink.href = "/login";
            successMessage.appendChild(document.createElement('br'));
            successMessage.appendChild(loginLink);
            messageContainer.appendChild(successMessage);
            messageContainer.classList.add('alert', 'alert-success');
            document.getElementById('passwordForm').reset();
        } else {
            response.text().then(errorMessage => {
                document.getElementById('messageContainer').textContent = errorMessage;
            });
        }
    })
    .catch(error => {
        console.error("Error:", error);
        document.getElementById('messageContainer').textContent = "Une erreur s'est produite. Veuillez réessayer.";
    });
});
