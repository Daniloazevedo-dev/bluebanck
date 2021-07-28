function setValoresHome() {
	
	var credenciais = JSON.parse(localStorage.getItem("credenciais"));
	
	if(credenciais !== null ) {
		displayName.innerHTML = credenciais.displayName;
		titular.innerHTML = credenciais.displayName;
		email.innerHTML = credenciais.email;
		cpf.innerHTML = credenciais.cpf;
		
		
		for(var cont = 0;credenciais.contasBancarias.length; cont++) {
			
			let tipo = credenciais.contasBancarias[cont].tipo;
			
			if( tipo === "poupanca") {
				
				poupanca.innerHTML = credenciais.contasBancarias[cont].numero;
				
				if(credenciais.contasBancarias[cont].ativo === true) {
					contaPoupanca.setAttribute("checked", "true");
					saldo.innerHTML = credenciais.contasBancarias[cont].saldo.toLocaleString('pt-br', {minimumFractionDigits: 2})
					
				}
				
			} else if(tipo === "corrente") {
								
				corrente.innerHTML = credenciais.contasBancarias[cont].numero;
				
				if(credenciais.contasBancarias[cont].ativo === true) {
					contaCorrente.setAttribute("checked", "true");
					saldo.innerHTML = credenciais.contasBancarias[cont].saldo.toLocaleString('pt-br', {minimumFractionDigits: 2});
				}
				
			}
		}
		
	}
}

window.onload = setValoresHome