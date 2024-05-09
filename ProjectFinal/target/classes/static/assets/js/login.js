  document.getElementById("btn-login").addEventListener("click", function() {
         login();
     });

  function login() {
      var login = document.getElementById("login").value;
      var password = document.getElementById("password").value;

      // Vérification des champs vides
      if (login.trim() === '' || password.trim() === '') {
          var errorMessageElement = document.getElementById("errorMessage");
          errorMessageElement.textContent = 'Veuillez remplir tous les champs.';
          errorMessageElement.classList.add('alert', 'alert-danger');
          return; // Arrêter l'exécution de la fonction si les champs sont vides
      }

      var xhr = new XMLHttpRequest();
      xhr.open("POST", "/login", true);
      xhr.setRequestHeader('Content-Type', 'application/json');
      xhr.onreadystatechange = function () {
          if (xhr.readyState === 4) {
              if (xhr.status === 200) {
                  // Connexion réussie
                  window.location.href = "/Dashboard"; // Redirection vers la page home.html
              } else {
                  // Connexion échouée
                  var errorMessageElement = document.getElementById("errorMessage");
                  errorMessageElement.textContent = xhr.responseText;
                  errorMessageElement.classList.add('alert', 'alert-danger');
              }
          }
      };

      var data = JSON.stringify({
          "login": login,
          "password": password
      });
      xhr.send(data);
  }
