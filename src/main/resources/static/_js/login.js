function login(form) {

	event.preventDefault();

	if (form.checkValidity() === true) {

		var url = rota + "login";
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
			contentType: "application/json; charset=utf-8",
			async: true

		}).then(sucesso, falha);

		function sucesso(data, textStatus, request) {
			let token = request.getResponseHeader("Authorization");
			storeCredentials(token);
			redirectHome();
		}

		function falha(data) {
			alert(data.responseJSON.error);
		}

		function storeCredentials(token) {
			const tokenData = JSON.parse(atob(token.split(".")[1]));
			const credenciais = { contasBancarias: tokenData.contasBancarias, email: tokenData.sub, displayName: tokenData.displayName, cpf: tokenData.cpf, token: token };
			localStorage.setItem("credenciais", JSON.stringify(credenciais));

		}

		function redirectHome() {
			window.location.href = "/home";
		}
	}

}

function logout() {
	localStorage.removeItem("credenciais");
}

window.onload = logout;
