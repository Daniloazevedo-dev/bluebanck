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
	
	if(dataAtual.getHours() >= 1 && dataAtual.getHours() <= 11 ) {
		saudacao.innerHTML = "Bom dia!";
	} else if(dataAtual.getHours() >= 12 && dataAtual.getHours() <= 18 ) {
		saudacao.innerHTML = "Boa tarde!";
	} else if(dataAtual.getHours() >= 19 && dataAtual.getHours() <= 23 ) {
		saudacao.innerHTML = "Boa noite!";
	} else if(dataAtual.getHours() === 0) {
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
				}

			} else if (tipo === "corrente") {

			if (credenciais.contasBancarias[cont].ativo === true) {
					contaSelecionada.innerHTML = "Corrente";
					saldo.innerHTML = credenciais.contasBancarias[cont].saldo.toLocaleString('pt-br', { minimumFractionDigits: 2 })
					numero.innerHTML = credenciais.contasBancarias[cont].numero;
				}
			}
		}
		
}

window.onload = setValoresTransferencia