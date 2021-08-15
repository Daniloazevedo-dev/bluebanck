(function() {

	window.addEventListener('load', function() {

		var forms = document.getElementsByClassName('validator');

		var validation = Array.prototype.filter.call(forms, function(form) {

			form.addEventListener('submit', function(event) {

				if (form.checkValidity() === false) {
					event.preventDefault();
					event.stopPropagation();

				} else if (form.checkValidity() === true && form.getAttribute("id") === "formVerificaContaDestino") {

					event.preventDefault();
					event.stopPropagation();

					$('#ModalDadosDestinatario').modal('show');
				}

				form.classList.add('was-validated');

			}, false);
		});
	}, false);
})();

