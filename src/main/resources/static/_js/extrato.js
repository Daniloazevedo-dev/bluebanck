function setValoresExtrato() {

	var credenciais = JSON.parse(localStorage.getItem("credenciais"));

	if (credenciais !== null) {
		displayName.innerHTML = credenciais.displayName;
		setSaudacao();
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
window.onload = setValoresExtrato