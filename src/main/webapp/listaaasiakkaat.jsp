<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Listaa asiakkaat</title>
</head>
<body>
	<table id="listaus">
		<thead>
		<tr>
			<th colspan="5" class="oikealle"><span id="uusiAsiakas">Lis�� uusi asiakas</span></th>
		</tr>
		<tr>
			<tr>
				<th colspan="3" class="oikealle">Hakusana:</th>
				<th><input type="text" id="hakusana"></th>
				<th><input type="button" id="hakunappi" value="Hae"></th>
			</tr>		
			<tr>
				<th>Etunimi</th>
				<th>Sukunimi</th>
				<th>Puhelin</th>
				<th>Sposti</th>
				<th>&nbsp;</th>			
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
<script>
$(document).ready(function() {
	
	$("#uusiAsiakas").click(function() {
		document.location="lisaaasiakas.jsp";
	});
	
	haeAsiakkaat();
	$("#hakunappi").click(function(){	
		haeAsiakkaat();
	});
	
	$(document.body).on("keydown", function(event){
		  if(event.which==13){ //Enteri� painettu, ajetaan haku
			  haeAsiakkaat();
		  }
	});
	
	$("#hakusana").focus();//vied��n kursori hakusana-kentt��n sivun latauksen yhteydess�
	
});

function haeAsiakkaat(){	
	$("#listaus tbody").empty();
	//$.getJSON on $.ajax:n alifunktio, joka on erikoistunut jsonin hakemiseen. Kumpaakin voi t�ss� k�ytt��.
	//$.getJSON({url:"asiakkaat/"+$("#hakusana").val(), type:"GET", success:function(result){
	$.ajax({
		url:"asiakkaat/"+$("#hakusana").val(), 
		type:"GET", dataType:"json", 
		success:function(result){
			$.each(result.asiakkaat, function(i, field){  
        		var htmlStr;
        		htmlStr += "<tr id='rivi_" + field.asiakas_id + "'>";
        		htmlStr += "<td>" + field.etunimi + "</td>";
        		htmlStr += "<td>" + field.sukunimi + "</td>";
        		htmlStr += "<td>" + field.puhelin + "</td>";
        		htmlStr += "<td>" + field.sposti + "</td>";
        		htmlStr += "<td><span class='poista' onclick=poista("+ field.asiakas_id + ",'" + field.etunimi + "','" + field.sukunimi + "')>Poista</span></td>";
        		htmlStr += "</tr>";
        		$("#listaus tbody").append(htmlStr);
        });
    }});	
}

function poista(asiakas_id, etunimi, sukunimi){
	if (confirm("Poista asiakas " + etunimi + " " + sukunimi + "?")) {
		$.ajax({
			url:"asiakkaat/" + asiakas_id, 
			type:"DELETE", 
			dataType:"json", 
			success:function(result) {
				//result on joko {"response:1"} tai {"response:0"}
				if (result.response == 0) {
	        		$("#ilmo").html("Asiakkaan poisto ep�onnistui.");
	        	} else if (result.response == 1) {
	        		//V�rj�t��n poistetun asiakkaan rivi
	        		$("#rivi_" + asiakas_id).css("background-color", "red"); 
	        		alert("Asiakkaan " + etunimi + " " + sukunimi +" poisto onnistui.");
					haeAsiakkaat();        	
			}
	    }});
	}
	console.log(asiakas_id);
}
</script>
</body>
</html>