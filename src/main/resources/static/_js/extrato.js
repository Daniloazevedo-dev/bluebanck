$(document).ready(function() {
	$('#table').DataTable()
});


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

function redirectHome() {
	window.location.href = "/home"
}

function buscaExtratoPorData() {
	
	var url = "http://localhost:8080/usuario/extrato/search";
	let inicio = dataInicial.value;
	let fim = dataFinal.value;
	
	if(inicio === "") {
		alert("Informe a data inicial e Data para buscar o extrato.");
	} else {
		
		if(fim === ""){
			fim = dataAtualFormatada();
		}
		
		buscaExtrato(inicio, fim, url); 
	}
	
	
}

function buscaExtrato(dataInicio, dataFim,url) {
	
	var credenciais = JSON.parse(localStorage.getItem("credenciais"));
	var token = credenciais.token;
	
	var data = {
		"dataInicial": dataInicio,
		"dataFinal": dataFim
	};
	
	console.log(data,url);
	
	$.ajax({
		type: "GET",
		url: url + "/" + dataInicio + "/" + dataFim,
		async: true,
		contentType: "application/json; charset=utf-8",
		headers: { "Authorization": token}
	}).then(sucesso, falha);

	function sucesso(data) {
		preencheTable(data);
	}

	function falha(data) {
		alert(data.responseJSON.error);
	}
}

function dataAtualFormatada(){
    var data = new Date(),
        dia  = data.getDate().toString().padStart(2, '0'),
        mes  = (data.getMonth()+1).toString().padStart(2, '0'), //+1 pois no getMonth Janeiro começa com zero.
        ano  = data.getFullYear();
    return ano+"-"+mes+"-"+dia;
}




function preencheTable(data) {	
	
	var linhas = "";	
		for(var cont = 0; cont <= data.length; cont++) {
		console.log(data[cont].data);
		linhas = linhas + "<tr>" + "<td>"+ data[cont].data + "</td>"+ "<td>"+ data[cont].descricao + "</td>"+ "<td>"+ data[cont].valor + "</td>" + "</tr>";
		tableBody.innerHTML = linhas;
	}

}

window.onload = setValoresExtrato