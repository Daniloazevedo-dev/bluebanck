//$(document).ready( function () {
//  $('#myTable').DataTable();
//} );

$(document).ready(function() {
	$('#tableBody').pageMe({
		pagerSelector: '#developer_page',
		showPrevNext: true,
		hidePageNumbers: false,
		perPage: 3
	});
});

function setValoresExtrato() {
	tableBody.innerHTML = "<td colspan='3' >Não há dados para serem mostrados.</td>";
	var credenciais = JSON.parse(localStorage.getItem("credenciais"));

	if (credenciais !== null) {
		displayName.innerHTML = credenciais.displayName;
		setSaudacao();
	}
}

function setSaudacao() {
	let dataAtual = new Date();

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

function redirectHome() {
	window.location.href = "/home"
}

function buscaExtratoPorData() {

	var url =  rota + "usuario/extrato/search";
	let inicio = dataInicial.value;
	let fim = dataFinal.value;

	if (inicio === "") {
		alert("Informe a data inicial e Data para buscar o extrato.");
	} else {

		if (fim === "") {
			fim = dataAtualFormatada();
		}

		buscaExtrato(inicio, fim, url);
	}


}

function buscaExtrato(dataInicio, dataFim, url) {

	var credenciais = JSON.parse(localStorage.getItem("credenciais"));
	var token = credenciais.token;

	var data = {
		"dataInicial": dataInicio,
		"dataFinal": dataFim
	};

	$.ajax({
		type: "GET",
		url: url + "/" + dataInicio + "/" + dataFim,
		async: true,
		contentType: "application/json; charset=utf-8",
		headers: { "Authorization": token }
	}).then(sucesso, falha);

	function sucesso(data) {
		preencheTable(data);
	}

	function falha(data) {
		alert(data.responseJSON.error);
	}
}

function dataAtualFormatada() {
	var data = new Date(),
		dia = data.getDate().toString().padStart(2, '0'),
		mes = (data.getMonth() + 1).toString().padStart(2, '0'), //+1 pois no getMonth Janeiro começa com zero.
		ano = data.getFullYear();
	return ano + "-" + mes + "-" + dia;
}




function preencheTable(data) {
	var linhas = "";
	if (data.length === 0) {
		tableBody.innerHTML = "<td colspan='4' >Não há dados para serem mostrados.</td>";
	} else {
		for (var cont = 0; cont <= data.length; cont++) {
			var conta = data[cont].tipoDaConta;
			
			if(conta === "poupanca") {
				conta = "Poupança";
			} if(conta === "corrente") {
				conta = "Corrente";
			}
			
			linhas = linhas + "<tr>" + "<td>" + formataData(data[cont].data) + "</td>" + "<td>" + data[cont].descricao + "</td>" + "<td>" + data[cont].valor.toLocaleString('pt-br', { minimumFractionDigits: 2 }) + "</td>" + "<td>" + conta + "</td>" +"</tr>";
			tableBody.innerHTML = linhas;
		}
	}

}

function redirectHome() {
	window.location.href = "/home"
}

function formataData(data) {
	data = new Date(data);
	var mes = data.getMonth() + 1;
	var dia = data.getDate() + 1
	var mesFormatado = corrigeMesEDia(mes);
	var diaFormatado = corrigeMesEDia(dia);
	data = (( diaFormatado)) + "/" + ((mesFormatado)) + "/" + data.getFullYear();
	return data;
}

function corrigeMesEDia(diaOuMes) {
	
	var numeros = [1,2,3,4,5,6,7,8,9];
	var cont = 0;
	
	while(cont <= numeros.length) {
		if(diaOuMes === cont) {
			diaOuMes = '0' + diaOuMes ;
		} 
		cont++;
	}
	return diaOuMes;
}

window.onload = setValoresExtrato