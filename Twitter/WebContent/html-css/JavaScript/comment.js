/**
 * 
 */

cpt = 1;
function erase(val) {
	if (val.innerHTML != '') {
		val.innerHTML = '';
	}else{
		val.value = '';
	}
}

function date_actuel() {
	var mnt = new Date();
	var jour = mnt.getDate();
	var mois = mnt.getMonth() + 1;
	var an = mnt.getFullYear();
	var heure = mnt.getHours();
	var minute = mnt.getMinutes();
	var now = jour + "/" + mois + "/" + an + " " + heure + ":" + minute;
	return now;
}

function recupText(date, image) {
	countryCode = $("#comment").val();

	regex2 = new RegExp("^(.*) $");
	regex3 = new RegExp("([^#]+)#(.*)$");

	if (regex3.test(countryCode)) {
		string = RegExp.$1;
	}
	if (regex2.test(countryCode)) {
		countryCode = RegExp.$1;
	}
	regex = new RegExp("([^@]+)@(.*)$");
	variable = false;
	var pluginArrayArg = new Array();
	if (regex.test(countryCode)) {
		variable = true;
		param = RegExp.$2.split("@");
		countryCode = RegExp.$1;
		myJsonString = JSON.parse(JSON.stringify(param));
	}
	if (variable == false) {

		b = document.getElementById("addArea");
		d = document.createElement('div');
		a = document.createElement('a');
		
		pro = "profile : "+login;
		a.appendChild(document.createTextNode("@" + env.actif.login));
		a.setAttribute("id", env.actif.id);
		a.setAttribute("style", "color:blue;");
		a.setAttribute("onClick", "charger_page(this,false,pro)");

		d.setAttribute("id", "block");
		d.setAttribute("name", cpt);
		d.setAttribute("style", "background-color: #2AD4FF");

		cpt++;

		d.appendChild(image);
		d.appendChild(a);
		if (regex3.test($("#comment").val())) {
			d.appendChild(document.createTextNode(" :" + string));
			hach_tag = document.createElement('a');
			hach_tag.appendChild(document.createTextNode("#" + RegExp.$2));
			hach_tag.setAttribute("style", "color:pink;");
			hach_tag.setAttribute("onClick", "tag(this)");
			d.appendChild(hach_tag);
		} else {
			d.appendChild(document.createTextNode(" :" + countryCode));
		}
		d.appendChild(date);
		b.appendChild(d);
		dataString = "id=" + env.actif.id;
		dataString = dataString + "&commentaire="
				+ encodeURIComponent(countryCode);
		// make the AJAX request, dataType is set to json
		// meaning we are expecting JSON data in response from the server
		$.ajax({
			type : "GET",
			url : " http://localhost:8080/AFFES-BENCHADI/addComment",
			data : dataString,
			dataType : "json",
			// if received a response from the server
			success : function(json, textStatus, jqXHR) {
				if (json.idOuNomExistePas == 9) {
					alert("idOuNomExistePas");
					return false;
				}
				if (json.PbArgument == -1) {
					alert("PbArgument");
					return false;
				} else {
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("Something really bad happened " + textStatus);
				$("#inscription").html(jqXHR.responseText);
			},
		});
	} else {
		$.ajax({
			type : "GET",
			url : " http://localhost:8080/AFFES-BENCHADI/addCommentList",
			data : "id=" + env.actif.id + "&loginTab=" + myJsonString
					+ "&commentaire=" + countryCode,
			dataType : "text",
			// if received a response from the server
			success : function(json, textStatus, jqXHR) {
				if (json.idOuNomExistePas == 9) {
					alert("idOuNomExistePas");
					return false;
				}
				if (json.PbArgument == -1) {
					alert("PbArgument");
					return false;
				} else {
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("Something really bad happened " + textStatus);
				$("#inscription").html(jqXHR.responseText);
			},
		});
	}
	document.getElementById("comment").value = '';
}

function div_Comment() {
	d = document.createElement('div');

	image = document.createElement('img');
	image2 = document.createElement('img');

	date = document.createElement('p');
	font = document.createElement('font');

	date.setAttribute("align", "left");
	d.setAttribute("style", "float:right");

	font.setAttribute("color", "black");

	image2.setAttribute("id", ajout);
	image2.setAttribute("src", "ajout.png");
	image2.setAttribute("style", "float:right");
	image2.setAttribute("height", "20");
	image2.setAttribute("width", "20");
	image2.setAttribute("onclick", "ajoutOuSupAmis(this)");

	image.setAttribute("src", "croi.png");
	image.setAttribute("id", cpt);
	image.setAttribute("onclick", "remove_comment(this)");
	image.setAttribute("height", "20");
	image.setAttribute("width", "20");

	d.appendChild(image);

	font.appendChild(document.createTextNode(date_actuel()));
	date.appendChild(font);
	date.appendChild(image2);
	recupText(date, d);
}

function tag(commentaire) {
	tab = [];
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/search",
		data : "id=" + env.actif.id + "&amiOuTout=Tout le monde"+ "&commentaire=" + encodeURIComponent(commentaire.innerHTML),
		dataType : "text", // if received a response from the
		success : function(json, textStatus, jqXHR) {
			if (json.PbArgument == -1) {
				alert("PbArgument");
				return false;
			} else {
				actu = false;
				listFriends();
				charger_page("b", true,"ListComment avec hashtag");
				rechercheCommentaire.traiteReponseComment(json);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something reallybad happened " + textStatus);
		},
	});

}
