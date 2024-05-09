document.addEventListener("DOMContentLoaded", function() {
  var form = document.querySelector('.php-email-form');
  form.addEventListener("submit", function(e) {
    e.preventDefault();
    var formData = new FormData(form);
    var url = form.getAttribute('action');
    var xhr = new XMLHttpRequest();

    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onload = function() {
      if (xhr.status >= 200 && xhr.status < 400) {
        var response = xhr.responseText;
        document.querySelector('.sent-message').style.display = 'block';
        document.querySelector('.error-message').style.display = 'none';
        form.reset();
      } else {
        var errorMessage = xhr.responseText;
        document.querySelector('.error-message').innerHTML = errorMessage;
        document.querySelector('.error-message').style.display = 'block';
        document.querySelector('.sent-message').style.display = 'none';
      }
    };

    xhr.onerror = function() {
      console.error("Une erreur s'est produite lors de la requête.");
    };

    xhr.send(new URLSearchParams(formData).toString());
  });
});
