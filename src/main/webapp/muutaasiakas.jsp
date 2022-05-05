<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1"><script src="scripts/main.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Muuta asiakas</title>
</head>
<body>
<form id="tiedot">
	<table>
		<thead>

			<tr>
				<th colspan="5" class="oikealle"><span id="takaisin">Takaisin listaukseen</span></th>
			</tr>
			<tr>
				<th>Etunimi</th>
				<th>Sukunimi</th>
				<th>Puhelin</th>
				<th>Sposti</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="text" name="etunimi" id="etunimi"></td>
				<td><input type="text" name="sukunimi" id="sukunimi"></td>
				<td><input type="text" name="puhelin" id="puhelin"></td>
				<td><input type="text" name="sposti" id="sposti"></td>
				<td><input type="submit" id="tallenna" value="Hyv�ksy"></td>
		</tbody>
	</table>
	<input type="hidden" name="asiakas_id" id="asiakas_id">
</form>
<br>
<span id="ilmo"></span>
<script>
$(document).ready(function(){
	$("#etunimi").focus();  // vied��n kursori etunimi-kentt��n
	
	$("#takaisin").click(function() {
		document.location="listaaasiakkaat.jsp";
	});
	
	// Haetaan muutettavan asiakkaan tiedot. Kutsutaan backin GET-metodia
	// GET /autot/haeyksi/asiakas_id
	var asiakas_id = requestURLParam("asiakas_id"); // Funktio l�ytyy script-hakemistosta
	$.ajax({
		url:"asiakkaat/haeyksi/" + asiakas_id,
		type:"GET",
		dataType:"json",
		success:function(result) {
			$("#asiakas_id").val(result.asiakas_id);  // asiakas_id ei ole muokattavissa, koska se on autonumber-tyyppinen, joten sit� ei voi muuttaa manuaalisesti
			$("#etunimi").val(result.etunimi);
			$("#sukunimi").val(result.sukunimi);
			$("#puhelin").val(result.puhelin);
			$("#sposti").val(result.sposti);
		}
	});
	
	$("#tiedot").validate({
		rules:	{
			etunimi:	{
				required: true,
				minlength: 1,
				maxlength: 50 // varchar(50)
			},
			sukunimi:	{
				required: true,
				minlength: 1,
				maxlength: 50 // varchar(50)
			},
			puhelin:	{
				required: true,
				minlength: 8,
				maxlength: 20 // varchar(20)
			},
			sposti:	{
				required: true,
				minlength: 6,
				maxlength: 100 // varchar(100)
			}
		},
		messages:	{
			etunimi:	{
				required:	"Puuttuu",
				minlength:	"Liian lyhyt",
				maxlength:	"Liian pitk�"
			},
			sukunimi:	{
				required:	"Puuttuu",
				minlength:	"Liian lyhyt",
				maxlength:	"Liian pitk�"
			},
			puhelin:	{
				required:	"Puuttuu",
				minlength:	"Liian lyhyt",
				maxlength:	"Liian pitk�"
			},
			sposti:	{
				required:	"Puuttuu",
				minlength:	"Liian lyhyt",
				maxlength:	"Liian pitk�"
			}
		},
		submitHandler: function(form) {
			paivitaAsiakas();
		}
	});
});

// funktio tietojen lis��mist� varten. 
// Kutsutaan backin PUT-metodia ja v�litet��n kutsun mukana uudet tiedot json-stringin�.
//PUT /asiakkaat/
function paivitaAsiakas(){
	// muutetaan lomakkeen tiedot json-stringiksi:
	var formJsonStr = formDataJsonStr($("#tiedot").serializeArray());
	console.log(formJsonStr);
	$.ajax({
		url:"asiakkaat",
		data:formJsonStr, 
		type:"PUT", 
		dataType:"json",
		success:function(result) {	//result on joko {"response:1"} tai {"response:0"}
			if (result.response == 0) {
				$("#ilmo").html("Asiakkaan p�ivitt�minen ep�onnistui.");
			} else if (result.response == 1) {
				$("#ilmo").html("Asiakkaan p�ivitt�minen onnistui.");
				$("#etunimi, #sukunimi, #puhelin, #sposti").val("");

			}
		}
	});
}
</script>
</body>
</html>