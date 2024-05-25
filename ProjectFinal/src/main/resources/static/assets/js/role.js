   $(document).ready(function() {
    $.ajax({
        url: '/userRole',
        type: 'GET',
        success: function(userRole) {
            console.log('User Role:', userRole);
        if (userRole === "admin") {
            // Display all elements for admin
            $('.manage-secretaries, .manage-doctors, .manage-patients, .manage-specialities, .manage-schedules,.manage-historique,.manage-appointement').show();
        } else if (userRole === "Secrétaire") {
            // Display limited elements for secretary
            $('.manage-secretaries, .manage-doctors').hide();
            $('.manage-patients, .manage-schedules,.manage-historique,.manage-appointement').show();
        }
        },
        error: function(xhr, status, error) {
            console.error('Erreur lors de la récupération du rôle utilisateur:', error);
            // Gérer les erreurs en conséquence
        }
    });
});