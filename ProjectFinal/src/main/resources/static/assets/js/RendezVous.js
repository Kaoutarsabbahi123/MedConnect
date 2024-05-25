   $(document).ready(function() {
     $('#specialite').change(function() {
         var selectedId = $(this).val();
         $('#specialiteId').val(selectedId);
         if (selectedId !== "") {
             $.ajax({
                 url: '/specialite/' + selectedId + '/docteurs',
                 type: 'GET',
                 success: function(data) {
                     var docteurs = data;
                     var options = '<option value="">Sélectionnez une docteur</option>';
                     docteurs.forEach(function(docteur) {
                       options += `<option value="${docteur.id_docteur}">${docteur.nom} ${docteur.prenom}</option>`;
                     });
                     $('#docteur').prop('readonly', false); // Rendre le champ modifiable
                     $('#docteur').empty().append(options); // Mettre à jour les options du champ docteur
                 },
                 error: function(xhr, status, error) {
                     console.error(xhr.responseText);
                 }
             });
         } else {
             $('#docteur').prop('readonly', true); // Rendre le champ docteur en lecture seule
             $('#docteur').val(''); // Effacer le champ docteur si aucune spécialité n'est sélectionnée
         }
     });
 });
  $(document).ready(function() {
      $('#docteur').change(function() {
          var selectedId = $(this).val();
          if (selectedId !== "") {
              $.ajax({
                  url: '/docteur/' + selectedId + '/dates-dispo',
                  type: 'GET',
                  success: function(data) {
                      var dates = data;
                      var dateOptions = dates.map(function(date) {
                          // Convertir chaque date en format lisible (e.g., DD/MM/YYYY)
                          var formattedDate = new Date(date).toLocaleDateString('fr-FR');
                          return `<option value="${formattedDate}">${formattedDate}</option>`;
                      });

                      // Mettre à jour les options du champ date-dispo
                      $('#date-dispo').prop('readonly', false); // Rendre le champ modifiable
                      $('#date-dispo').empty().append('<option value="">Sélectionnez une date</option>').append(dateOptions);
                  },
                  error: function(xhr, status, error) {
                      console.error(xhr.responseText);
                  }
              });
          } else {
              // Aucun docteur sélectionné, réinitialiser le champ date-dispo
              $('#date-dispo').prop('readonly', true); // Rendre le champ date-dispo en lecture seule
              $('#date-dispo').empty().append('<option value="">Sélectionnez une date</option>'); // Effacer et afficher le message par défaut
          }
      });
  });

 $(document).ready(function() {
    $('#date-dispo').change(function() {
        var selectedDate = $(this).val();
        var selectedDoctorId = $('#docteur').val();

        $.ajax({
            url: '/api/consultation-hours',
            type: 'GET',
            data: {
                date: selectedDate,
                doctorId: selectedDoctorId
            },
            success: function(data) {
                console.log('Response data:', data);

                // Vérifier si data est un tableau
                if (Array.isArray(data)) {
                    // Créer des options pour chaque heure disponible
                    var options = data.map(hour => `<option value="${hour}">${hour}</option>`);

                    // Vider la liste déroulante et ajouter les options
                      $('#heure_consultation').empty().append('<option value="">Sélectionnez une heure</option>').append(options);
                } else {
                    console.error('Data is not an array:', data);
                }
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText);
            }
        });
    });
});


$(document).ready(function() {
    // Lorsque le formulaire est soumis
    $('#registrationForm').submit(function(event) {
        event.preventDefault(); // Empêche le comportement par défaut du formulaire

        // Soumettre le formulaire via AJAX
        $.ajax({
            type: "POST",
            url: "/api/rendezvous/save",
            data: $(this).serialize(),
            success: function(response) {
                // Afficher le modal de succès avec le message
                $('#successMessage').text(response);
                // Utilisez vanilla JavaScript pour afficher le modal Bootstrap
                var myModal = new bootstrap.Modal(document.getElementById('successModal'));
                myModal.show();
                $('#registrationForm')[0].reset();
            },
            error: function(xhr, status, error) {
                console.log("Erreur lors de la soumission du formulaire:", error);
            }
        });
    });

    // Lorsque l'icône de fermeture du popup est cliquée
    $('.close').click(function() {
        $('.popup').hide(); // Cacher le popup
    });
});
