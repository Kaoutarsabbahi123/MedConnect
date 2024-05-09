 function resetForm() {
        document.getElementById('verificationForm').reset();
        document.querySelectorAll('.inputs input').forEach(input => {
            input.setAttribute('disabled', true);
        });
        document.querySelector('.inputs input').disabled = false;
    }
    function showModal(message) {
       const modal = document.getElementById('myModal');
       const modalMessage = modal.querySelector('.modal-message');
       modalMessage.textContent = message;
       modal.style.display = 'block';

       const retryButton = modal.querySelector('.modal-retry-button');
       retryButton.addEventListener('click', () => {
           window.location.href = '/SendEmail';
       });
       const cancelButton = modal.querySelector('.modal-cancel-button');
       cancelButton.addEventListener('click', () => {
           window.location.href = '/';
       });
   }
   function submitForm() {
       const form = document.getElementById('verificationForm');
       const formData = new FormData(form);
       let status = null

       fetch(form.action, {
           method: 'POST',
           body: formData
       })
       .then(response => {
       status = response.status;
           if (!response.ok) {
               return response.text();
           } else {
               window.location.href = '/RecupereMdp';
           }
       })
       .then(data => {
           if(status != 401) {
               document.getElementById('message').textContent = data;
               resetForm();}
           else{
           showModal(data);
           document.getElementById('message').textContent = null;
           resetForm();
           }
       })
   }
    document.querySelectorAll('.inputs input').forEach((input, index) => {
        input.addEventListener('input', () => {
            if (input.value.length === 1 && index < 3) {
                document.querySelectorAll('.inputs input')[index + 1].disabled = false; // Activer le champ suivant
                document.querySelectorAll('.inputs input')[index + 1].focus(); // Mettre le focus sur le champ suivant
            }
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && index > 0) {
                document.querySelectorAll('.inputs input')[index - 1].focus(); // Mettre le focus sur le champ précédent
            }
        });
    });