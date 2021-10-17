function recuperaSenha() {
	if (form.checkValidity() === true) {
		var emailCadastrado = email.value;
		var url = "http://localhost:8080/public/recuperar/" + emailCadastrado;
		
		$.ajax({
			type: "POST",
			url: url,
			contentType: "application/json; charset=utf-8",
			async: true

		}).then(sucesso, falha);

		function sucesso(data) {
			alert(data.msgRecuperaSenha);
		}

		function falha(data) {
			alert(data.responseJSON.error);
		}

		
	}
}

function redirectLogin() {
	window.location.href = "/public/login";
}