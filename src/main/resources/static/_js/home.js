function selecionarConta() {
	
	var credenciais = JSON.parse(localStorage.getItem("credenciais"));
	
	var url = "http://localhost:8080/usuario/altera-tipo-conta";
	var token = credenciais.token;
	var data = credenciais.contasBancarias;

	$.ajax({
		type: "PUT",
		url: url,
		data: JSON.stringify(data),
		async: true,
		contentType: "application/json; charset=utf-8",
		headers: {"Authorization": token}
	}).then(sucesso,falha);
	
	function sucesso(data) {
		setCredenciais(credenciais, data);
	}
	
	function falha() {
		alert("Ocorreu um erro ao selecionar a conta.");
	}
	
	function setCredenciais(credenciais,contasBancarias) {
		credenciais = { contasBancarias: contasBancarias, email: credenciais.email, displayName: credenciais.displayName, cpf: credenciais.cpf, token: credenciais.token };
		localStorage.setItem("credenciais", JSON.stringify(credenciais));
		setContasBancarias(credenciais);
	}
	
}
	
function setValoresHome() {

	var credenciais = JSON.parse(localStorage.getItem("credenciais"));

	if (credenciais !== null) {
		displayName.innerHTML = credenciais.displayName;
		titular.innerHTML = credenciais.displayName;
		email.innerHTML = credenciais.email;
		cpf.innerHTML = credenciais.cpf;
		setContasBancarias(credenciais);
	}
}

function setContasBancarias(credenciais) {
	
		for (var cont = 0; credenciais.contasBancarias.length; cont++) {

			let tipo = credenciais.contasBancarias[cont].tipo;

			if (tipo === "poupanca") {

				poupanca.innerHTML = credenciais.contasBancarias[cont].numero;

				if (credenciais.contasBancarias[cont].ativo === true) {
					contaPoupanca.checked = true;
					contaCorrente.checked = false; 
					saldo.innerHTML = credenciais.contasBancarias[cont].saldo.toLocaleString('pt-br', { minimumFractionDigits: 2 })

				}

			} else if (tipo === "corrente") {

				corrente.innerHTML = credenciais.contasBancarias[cont].numero;

				if (credenciais.contasBancarias[cont].ativo === true) {
					contaCorrente.checked = true;
					contaPoupanca.checked = false; 
					saldo.innerHTML = credenciais.contasBancarias[cont].saldo.toLocaleString('pt-br', { minimumFractionDigits: 2 });
				} 
			}
		}
		
}

function alterarCadastro() {
	window.location.href = "/public/nova-conta"
}


window.onload = setValoresHome