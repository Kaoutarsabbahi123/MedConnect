  var sendButton = document.getElementById('sendButton');
  var loadingIcon = sendButton.querySelector('.loading-icon');

  function afficherIconeChargement() {
    loadingIcon.style.display = 'inline-block';
  }
  function Iconinvisible(){
     loadingIcon.style.display = 'none';
  }

  sendButton.addEventListener('click', function(event) {
      event.preventDefault();
      afficherIconeChargement();
      setTimeout(submitForm, 1000);
  });


  function submitForm() {
      const form = document.getElementById('EmailForm');
      const formData = new FormData(form);

      fetch(form.action, {
          method: 'POST',
          body: formData
      })
      .then(response => {
          if (response.ok) {
             window.location.href = "/verification";
          } else if (response.status === 404) {
              response.text().then(errorMessage => {
                  const messageContainer = document.getElementById('messageContainer');
                  messageContainer.textContent = errorMessage;
                  messageContainer.classList.add('alert', 'alert-danger');
                  Iconinvisible();
              });
          } else {
              const messageContainer = document.getElementById('messageContainer');
              messageContainer.textContent = "Une erreur s'est produite4. Veuillez réessayer plus tard.";
              messageContainer.classList.add('alert', 'alert-danger');
              Iconinvisible(); //
          }
      })
      .catch(error => {
          console.error("Erreur lors de l'envoi du code de vérification:", error);
      });

  }

