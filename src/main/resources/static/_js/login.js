function login(form) {

	event.preventDefault();

	if (form.checkValidity() === true) {

		var url = "http://localhost:8080/login";
		var email = form.email.value;
		var senha = form.senha.value;

		var data = {
			"email": email,
			"senha": senha
		};

		requestAjax(data, url);
	}

	function requestAjax(data, url) {
		$.ajax({
			type: "POST",
			url: url,
			data: JSON.stringify(data),
			async: false

		}).then(sucesso, falha);

		function sucesso(data, textStatus, request) {

			var token = request.getResponseHeader('Authorization');

			if (token) {
				localStorage.setItem('token', token);
			}


		}

		function falha() {
			alert("E-mail ou senha incorreto.");
		}
	}



}