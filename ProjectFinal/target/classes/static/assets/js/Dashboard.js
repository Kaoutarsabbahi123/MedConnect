$(document).ready(function () {
            $('#sidebarCollapse').on('click', function () {
                $('#sidebar').toggleClass('active');
				$('#content').toggleClass('active');
            });

			 $('.more-button,.body-overlay').on('click', function () {
                $('#sidebar,.body-overlay').toggleClass('show-nav');
            });

        });

document.addEventListener('DOMContentLoaded', function() {
    // Sélectionner le lien "Profile" et ajouter un écouteur d'événement
    var profileLink = document.getElementById('profileLink');
    var profileLink2 = document.getElementById('profileLink2');
    if (profileLink) {
        profileLink.addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = '/profile';
        });
    }
    if (profileLink2) {
        profileLink2.addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = '/profile';
        });
    }

    var logoutLink = document.getElementById('logoutLink');
    var logoutLink2 = document.getElementById('logoutLink2');
    if (logoutLink) {
        logoutLink.addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = '/';
        });
    }
    if (logoutLink2) {
        logoutLink2.addEventListener('click', function(event) {
            event.preventDefault();
            window.location.href = '/';
        });
    }
});