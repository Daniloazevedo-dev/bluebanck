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

		requestLogin(data, url);
	}

	function requestLogin(data, url) {
		$.ajax({
			type: "POST",
			url: url,
			data: JSON.stringify(data),
			async: true

		}).then(sucesso, falha);

		function sucesso(data, textStatus, request) {
			let token = request.getResponseHeader("Authorization");
			storeCredentials(token);
		}

		function falha() {
			alert("E-mail ou senha incorreto.");
		}

		function storeCredentials(token) {
			const tokenData = JSON.parse(atob(token.split(".")[1]));
			const credentials = { email: tokenData.sub, displayName: tokenData.displayName, token: token };
			localStorage.setItem("credentials", JSON.stringify(credentials));
		}
	}



}