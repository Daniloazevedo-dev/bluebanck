var contaVerificada = false;
var tipoContaRemetente;
var numeroContaRemetente;

function setValoresTransferencia() {

	var credenciais = JSON.parse(localStorage.getItem("credenciais"));

	if (credenciais !== null) {
		displayName.innerHTML = credenciais.displayName;
		setSaudacao();
		setContasBancarias(credenciais);
	}
}

function setSaudacao() {
	let dataAtual = new Date();

	console.log(dataAtual.getHours())

	if (dataAtual.getHours() >= 1 && dataAtual.getHours() <= 11) {
		saudacao.innerHTML = "Bom dia!";
	} else if (dataAtual.getHours() >= 12 && dataAtual.getHours() <= 18) {
		saudacao.innerHTML = "Boa tarde!";
	} else if (dataAtual.getHours() >= 19 && dataAtual.getHours() <= 23) {
		saudacao.innerHTML = "Boa noite!";
	} else if (dataAtual.getHours() === 0) {
		saudacao.innerHTML = "Boa noite!";
	}


}

function setContasBancarias(credenciais) {

	for (var cont = 0; credenciais.contasBancarias.length; cont++) {

		let tipo = credenciais.contasBancarias[cont].tipo;

		if (tipo === "poupanca") {

			if (credenciais.contasBancarias[cont].ativo === true) {
				contaSelecionada.innerHTML = "Poupança";
				saldo.innerHTML = credenciais.contasBancarias[cont].saldo.toLocaleString('pt-br', { minimumFractionDigits: 2 })
				numero.innerHTML = credenciais.contasBancarias[cont].numero;

				tipoContaRemetente = "poupanca";
				numeroContaRemetente = credenciais.contasBancarias[cont].numero;
			}

		} else if (tipo === "corrente") {

			if (credenciais.contasBancarias[cont].ativo === true) {
				contaSelecionada.innerHTML = "Corrente";
				saldo.innerHTML = credenciais.contasBancarias[cont].saldo.toLocaleString('pt-br', { minimumFractionDigits: 2 })
				numero.innerHTML = credenciais.contasBancarias[cont].numero;
				
				tipoContaRemetente = "corrente";
				numeroContaRemetente = credenciais.contasBancarias[cont].numero;
			}
		}
	}

}

function verificarConta() {


	if (formVerificaContaDestino.checkValidity() === true) {

		var url = "http://localhost:8080/usuario/";
		let numeroContaDestino = numeroDaContaDestino.value;
		let tipoContaDestino;

		if (contaCorrenteDestino.checked === true) {
			tipoContaDestino = contaCorrenteDestino.value;
		} else if (contaPoupancaDestino.checked === true) {
			tipoContaDestino = contaPoupancaDestino.value;
		}

		url = url + numeroContaDestino + "/" + tipoContaDestino;

		buscaContaPorNumeroETipo(numeroContaDestino, tipoContaDestino, url);

	}

}

function buscaContaPorNumeroETipo(numeroContaDestino, tipoContaDestino, url) {

	var credenciais = JSON.parse(localStorage.getItem("credenciais"));
	var token = credenciais.token;

	$.ajax({
		type: "GET",
		url: url,
		async: true,
		contentType: "application/json; charset=utf-8",
		headers: { "Authorization": token}
	}).then(sucesso, falha);

	function sucesso(data) {

		numeroDestinatario.innerHTML = data.numero;

		if (data.tipo === "poupanca") {
			tipoContaDestinatario.innerHTML = "Poupança";
		} else if (data.tipo === "corrente") {
			tipoContaDestinatario.innerHTML = "Corrente";
		}

		titularDestinatario.innerHTML = data.titular;

		$('#ModalDadosDestinatario').modal('show');
	}

	function falha(data) {
		alert(data.responseJSON.error);
	}
}

function transferir() {

	if (contaVerificada === false) {
		alert("Favor, verificar a conta e confirmar antes de transferir.");
	} else {
		if (formTransferencia.checkValidity() === true && formVerificaContaDestino.checkValidity() === true) {

			var url = "http://localhost:8080/usuario/transferencia/";
			let numeroContaDestino = numeroDaContaDestino.value;
			let tipoContaDestino;

			if (contaCorrenteDestino.checked === true) {
				tipoContaDestino = contaCorrenteDestino.value;
			} else if (contaPoupancaDestino.checked === true) {
				tipoContaDestino = contaPoupancaDestino.value;
			} 

			var valorTransferenciaFormatado = valorTransferencia.value.replace(".", "").replace(",", ".");

			url = url + tipoContaRemetente + "/" + numeroContaRemetente + "/" + tipoContaDestino + "/" + numeroContaDestino + "/" + valorTransferenciaFormatado;
			tranferencia(url)
		}

	}
}

function tranferencia(url) {


	var credenciais = JSON.parse(localStorage.getItem("credenciais"));

	$.ajax({
		type: "PUT",
		url: url,
		contentType: "application/json; charset=utf-8",
		async: true,
		headers: { "Authorization": credenciais.token }
	}).then(sucesso, falha);

	function sucesso(data) {
		saldo.innerHTML = data.saldo.toLocaleString('pt-br', { minimumFractionDigits: 2 });

		setTimeout(function() {
			alert("Transferência realizada com sucesso.");
		}, 200);

		
		atualizaCredenciais(credenciais, data);
	}

	function falha(data) {
		alert(data.responseJSON.error);
	}

	function atualizaCredenciais(credenciais, contaBancaria) {

		for (var cont = 0; credenciais.contasBancarias.length; cont++) {

			let tipo = credenciais.contasBancarias[cont].tipo;

			if (tipo === "poupanca" || tipo === "corrente") {

				if (credenciais.contasBancarias[cont].ativo === true) {
					credenciais.contasBancarias[cont].saldo = contaBancaria.saldo
				}
			}
			localStorage.removeItem("credenciais");
			credenciais = { contasBancarias: credenciais.contasBancarias, email: credenciais.email, displayName: credenciais.displayName, cpf: credenciais.cpf, token: credenciais.token };
			localStorage.setItem("credenciais", JSON.stringify(credenciais));
		}


	}

}

function confirmaConta() {
	contaVerificada = true;
}

function redirectHome() {
	window.location.href = "/home"
}

window.onload = setValoresTransferencia