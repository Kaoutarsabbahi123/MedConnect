
jQuery(document).ready(function() {
    // Variable pour stocker le HTML du formulaire initial
    var initialFormHTML = '<form><div class="form-group"><label for="currentPassword">Mot de passe actuel</label><input type="password" class="form-control" id="currentPassword" required></div><div class="form-group"><label for="newPassword">Nouveau mot de passe</label><input type="password" class="form-control" id="newPassword" required></div><div class="form-group"><label for="confirmPassword">Confirmer le nouveau mot de passe</label><input type="password" class="form-control" id="confirmPassword" required></div><span id="messageContainer" style="display: inline-block; width:100%;"></span></form>';

    // Fonction pour réinitialiser le contenu du modal
    function resetModalContent() {
        jQuery('#currentPassword').val('');
        jQuery('#newPassword').val('');
        jQuery('#confirmPassword').val('');
        jQuery('#changePasswordModal .modal-body').html(initialFormHTML); // Réinsérer le formulaire initial
    }
    // Gérer le clic sur le bouton "Enregistrer" dans le modal
    jQuery('#saveChangesButton').click(function() {
        var currentPassword = jQuery('#currentPassword').val();
        var newPassword = jQuery('#newPassword').val();
        var confirmPassword = jQuery('#confirmPassword').val();

        if (newPassword !== confirmPassword) {
            const messageContainer = document.getElementById('messageContainer');
            messageContainer.textContent = "les mots de passe ne corresponds pas";
            messageContainer.classList.add('alert', 'alert-danger');
             return;
        }

        // Effectuer une requête AJAX pour changer le mot de passe
        jQuery.ajax({
            url: '/change-password',
            type: 'POST',
            data: {
                currentPassword: currentPassword,
                newPassword: newPassword
            },
            success: function(response) {
                // Afficher le message de succès dans le modal
                jQuery('#changePasswordModal .modal-body').html('<div class="alert alert-success" role="alert">' + response + '</div>');

            },
            error: function(xhr, status, error) {
                var errorMessage = xhr.responseJSON ? xhr.responseJSON.message : "Le mot de passe actuel est incorrect !";
                // Afficher le message d'erreur dans le modal

                const messageContainer = document.getElementById('messageContainer');
                messageContainer.textContent = errorMessage;
                messageContainer.classList.add('alert', 'alert-danger');
            }
        });
    });

    // Gérer le clic sur le bouton "Fermer" dans le modal
    jQuery('#closeModalButton').click(function() {
        // Réinitialiser le contenu du modal en réinsérant le formulaire initial
        resetModalContent();
    });

    // Gérer l'événement lorsque le modal se ferme
    jQuery('#changePasswordModal').on('hidden.bs.modal', function () {
        // Réinitialiser le contenu du modal
        resetModalContent();
    });
});
