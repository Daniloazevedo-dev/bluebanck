function novaConta(form) {

	event.preventDefault();

	var credenciais = JSON.parse(localStorage.getItem("credenciais"));

	if (form.checkValidity() === true) {

		var url = "http://localhost:8080/public/nova-conta";

		var titular = form.titular.value;
		var email = form.email.value;
		var cpf = form.cpf.value;
		var senha = form.senha.value;

		var usuario = {
			titular: titular,
			email: email,
			cpf: cpf,
			senha: senha
		}

		if (credenciais === null) {
			cadastrarUsuario(usuario, url);
		} else {
			alterarUsuario(usuario, credenciais);
		}


	}
}

function cadastrarUsuario(usuario, url) {
	$.ajax({
		type: "POST",
		url: url,
		data: JSON.stringify(usuario),
		contentType: "application/json; charset=utf-8",
		async: true
	}).then(sucesso, falha);

	function sucesso() {
		alert("Usuário cadastrado com sucesso. Faça o login.");
		redirectLogin();
	}

	function falha(data) {
		alert(data.responseJSON.error);
	}

}

function redirectLogin() {
	window.location.href = "/public/login";
}

function setDadosParaAlterarCadastro() {

	var credenciais = JSON.parse(localStorage.getItem("credenciais"));

	if (credenciais !== null) {

		labelSenha.style.display = "none";
		senha.style.display = "none";

		titular.value = credenciais.displayName;
		email.value = credenciais.email;
		cpf.value = credenciais.cpf;
		abrirConta.value = "Alterar dados"
		menuNovaConta.style.display = "none";
		displayName.innerHTML = credenciais.displayName;
	} else {
		menuLogado.style.display = "none";
	}
}

function alterarUsuario(usuario, credenciais) {

	let url = "http://localhost:8080/usuario/altetar-dados";
	let data = {
		titular: usuario.titular,
		email: usuario.email,
		cpf: usuario.cpf
	}


	$.ajax({
		type: "PUT",
		url: url,
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		headers: {"Authorization": credenciais.token},
		async: true
	}).then(sucesso, falha);

	function sucesso(data) {
		alert("Usuário Alterado com sucesso.");
		setCredenciais(credenciais,data);
	}

	function falha(data) {
		alert(data.responseJSON.error);
	}
}

function setCredenciais(credenciais,data) {
	
		if(credenciais.email !== data.email) {
			alert("Houve alteração no E-mail. Logue novamente com o novo E-mail.");
			window.location.href = "/public/login";
		}
	
		credenciais = { contasBancarias: credenciais.contasBancarias, email: data.email, displayName: data.titular, cpf: data.cpf, token: credenciais.token };
		localStorage.setItem("credenciais", JSON.stringify(credenciais));
	}
	
function voltar() {
	var credenciais = JSON.parse(localStorage.getItem("credenciais"));
	if(credenciais !== null) {
		window.location.href = "/index";
	} else {
		window.location.href = "/public/login";
	}
}

window.onload = setDadosParaAlterarCadastro;