cpt = 1;
tab = [];
tabFriends = [];
tabComment = [];
tabId = [];
auteur = "";
date = "";
prof = "Profile ";
function main2() {
	$.ajaxSetup({
		async : false
	});
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/verifSession",
		dataType : "json",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if (json.CookieVide == 1) {
				env = {};
				env.users = []; // tableau qui stocke les informations sur les
				return false;
			} else {
				env = {};
				env.users = [];
				env.actif = new User(json.id, json.login, undefined);
				charger_page(json, false);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
			$("#connexion").html(jqXHR.responseText);
		},
	});
}

function User(id, login, contact) {
	this.id = id;
	this.login = login;
	if (contact != undefined) {
		this.contact = contact;
	}
	env.users[this.id] = this;
}

function Commentaire(id, auteur, texte, date, score) {
	this.id = id;
	this.auteur = auteur;
	this.texte = texte;
	this.date = date;
	this.score = score;
}

Commentaire.prototype.getHtml = function() {
	if ((env.actif != undefined)) {
		getComment(this.id, this.texte, this.auteur.contact, this.date,
				this.auteur.login);
	}
}

User.prototype.modifieStatus = function() {
	this.contact = !this.contact;
}

function creeText(date, image, commentaire, login) {
	b = document.getElementById("addArea");
	d = document.createElement('div');
	a = document.createElement('a');
	regex = new RegExp("([^#]+)#(.*)$");
	if (regex.test(commentaire)) {
		string = RegExp.$1;
	}

	a.appendChild(document.createTextNode("@" + login));
	profile(login);
	a.setAttribute("id", id_from);
	a.setAttribute("style", "color:blue;");
	pro = "Profile : " + login;
	a.setAttribute("onClick", "charger_page(this,false,pro)");

	d.setAttribute("id", "block");
	d.setAttribute("name", cpt);
	d.setAttribute("style", "background-color: #2AD4FF");

	cpt++;

	d.appendChild(image);
	d.appendChild(a);
	if (regex.test(commentaire)) {
		d.appendChild(document.createTextNode(" :" + string));
		hach_tag = document.createElement('a');
		hach_tag.appendChild(document.createTextNode("#" + RegExp.$2));
		hach_tag.setAttribute("style", "color:pink;");
		hach_tag.setAttribute("onClick", "tag(this)");
		d.appendChild(hach_tag);
	} else {
		d.appendChild(document.createTextNode(" :" + commentaire));
	}
	d.appendChild(date);
	b.appendChild(d);
}

function getComment(id, commentaire, contacte, dateOfComment, login) {
	d = document.createElement('div');

	image = document.createElement('img');
	image2 = document.createElement('img');
	image3 = document.createElement('img');

	date = document.createElement('p');
	font = document.createElement('font');

	date.setAttribute("align", "left");
	d.setAttribute("style", "float:right");

	font.setAttribute("color", "black");

	image2.setAttribute("name", login);
	image2.setAttribute("src", "ajout.png");
	image2.setAttribute("style", "float:right");
	image2.setAttribute("height", "20");
	image2.setAttribute("width", "20");
	image2.setAttribute("onclick", "ajouter_ami(this)");

	image3.setAttribute("name", login);
	image3.setAttribute("src", "sup.png");
	image3.setAttribute("height", "20");
	image3.setAttribute("width", "20");
	image3.setAttribute("style", "float:right");
	image3.setAttribute("onclick", "supprimer_ami(this)");

	if (!actu) {
		image.setAttribute("src", "croi.png");
		image.setAttribute("id", cpt);
		image.setAttribute("onclick", "remove_comment(this)");
		image.setAttribute("height", "20");
		image.setAttribute("width", "20");

		d.appendChild(image);
	}

	font.appendChild(document.createTextNode(dateOfComment));
	date.appendChild(font);
	if (contacte == true) {
		date.appendChild(image3);
	} else {
		date.appendChild(image2);
	}
	creeText(date, d, commentaire, login);
}

function rechercheCommentaire(resultats, recherche, contact_only, auteur, date) {
	this.resultats = resultats;
	this.recherche = recherche;
	this.contact_only = contact_only;
	this.auteur = auteur;
	this.date = date;
	env.recherche = this;
}

rechercheCommentaire.prototype.getHtml = function() {
	for (var i = 0; i < this.resultats.length; i++) {
		this.resultats[i].getHtml();
	}
}

rechercheCommentaire.traiteReponseComment = function(json) {
	var obj = JSON.parse(json, rechercheCommentaire.revival);
	obj.getHtml();
}

rechercheCommentaire.traiteReponseSearchFriends = function(json) {
	select =  document.getElementById("selectAmi");
	select.setAttribute("style", "display:initial");
	select.setAttribute("onchange", "rechercheContacte()");
	select.length = 1;
	JSON.parse(json,rechercheCommentaire.searchFriends);
	div = document.getElementById("listAmi");
	div.appendChild(select);
	
}

rechercheCommentaire.traiteReponseFriends = function(json) {
	JSON.parse(json, rechercheCommentaire.friends);
}

rechercheCommentaire.traiteReponseId = function(json) {
	JSON.parse(json, rechercheCommentaire.id);
}

rechercheCommentaire.traiteReponseComment2 = function(json) {
	JSON.parse(json, rechercheCommentaire.comment);
}

rechercheCommentaire.revival = function revival(key, value) {
	if (key.length == 0) {
		if (value.erreur == undefined) {
			r = new rechercheCommentaire(tab, tab, false, auteur, date);
			return r;
		} else {
			return (value);
		}
	} else if (key == '_comment') {
		text = value;
	} else if (key == '_auteur') {
		auteur = value;
	} else if (key == 'id') {
		id = value;
	} else if (key == "_login") {
		login = value;
		profile(login);
		if (env.actif.id == id_from) {
			u = env.actif;
		} else {
			ami = false;
			for (var i = 0; i < tabFriends.length; i++) {
				if (tabFriends[i] == auteur) {
					ami = true;
					break;
				} else {
					ami = false;
				}
			}
			u = new User(id_from, login, ami);
		}
		c = new Commentaire(id, u, text, date, 03);
		tab.push(c);
	} else if (key == '_date') {
		mois = {
			"Jan" : "01",
			"Feb" : "02",
			"Mar" : "03",
			"Apr" : "04",
			"May" : "05",
			"Jun" : "06",
			"Jul" : "07",
			"Aug" : "08",
			"Sep" : "09",
			"Oct" : "10",
			"Nov" : "11",
			"Dec" : "12"
		};
		regex = new RegExp("^.*\\s(.*)\\s(.*)\\s(.*:.*):.*\\s.*\\s(.*)$");
		if (regex.test(value)) {
			date = RegExp.$2 + "/" + mois[RegExp.$1] + "/" + RegExp.$4 + "  "
					+ RegExp.$3;
		}
	} else {
		return (value);
	}
}

rechercheCommentaire.friends = function friends(key, value) {
	if (key != 'Friends') {
		tabFriends.push(value);
	}
}
rechercheCommentaire.id = function id(key, value) {
	if (key == 'ajouts') {
		tabId.push(value);
	}
}

rechercheCommentaire.comment = function comment(key, value) {
	if (key == '_comment') {
		tabComment.push(value);
	}
}

rechercheCommentaire.searchFriends = function searchFriends(key, value){
	if(key != 'OK'){
		if (value instanceof Array) {
			if(key != 'Users'){
				idTab = value.split(" ");
				var length = select.length;
				option = document.createElement('option');
				option.setAttribute("value", idTab[0]);
				option.appendChild(document.createTextNode(idTab[1]+" "+idTab[2]));
				select[length] = option;
			}
		}else{
			idTab = value.split(" ");
			var length = select.length;
			option = document.createElement('option');
			option.setAttribute("value", idTab[0]);
			option.appendChild(document.createTextNode(idTab[1]+" "+idTab[2]));
			select[length] = option;
		}
	}
}

function rechercheContacte(){
	var testSelect =  document.getElementById("selectAmi");
	var id = testSelect[testSelect.selectedIndex].value;
	charger_page(id,false,pro);
}

function envoieMotDePasse(){
	var conex =  document.getElementById('connexion');
	var passeOublie = conex.login.value;
	if(passeOublie.length == 0){
		func_error("Ecrivez votre login");	
	}
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/motDePasseOblie",
		data : "login="+passeOublie,
		dataType : "json",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if (json.PbArgument == -1) {
				alert(json);
				alert("PbArgument");
				return false;
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
			$("#connexion").html(jqXHR.responseText);
		},
	});
}

function envoiCommentaire() {
	tab = [];
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/listComment",
		data : id_from,
		dataType : "text",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if (json.ErreurID == -1) {
				alert("ErreurID");
				return false;
			} else {
				rechercheCommentaire.traiteReponseComment(json);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
			$("#inscription").html(jqXHR.responseText);
		},
	});
}

function searchFriends(input) {
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/getUsers",
		data: "nomPrenom="+input.value,
		dataType : "text",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			j = JSON.parse(json);
			if (j.PbArgument == -1) {
				alert("PbArgument");
				return false;
			} else if (j.nomPrenomInexistant == 15) {
				alert("nomPrenomInexistant");
				return false;
			} else {
				rechercheCommentaire.traiteReponseSearchFriends(json);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		},
	});
}


function listFriends() {
	$.ajaxSetup({
		async : false
	});
	$.ajax({
		type : "GET",
		url : "http://localhost:8080/AFFES-BENCHADI/listFriends",
		data : "id_from=" + env.actif.id,
		dataType : "text",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if (json.cetIdNexistePas == 8) {
				alert("cetIdNexistePas");
				return false;
			} else {
				rechercheCommentaire.traiteReponseFriends(json);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		}
	});
}

function remove_comment(img) {
	id = img.id;
	body = document.getElementsByName(id).item(0).parentNode;
	div = document.getElementsByName(id).item(0);
	fils = div.childNodes;
	fils2 = fils[1].childNodes;
	fils3 = fils[3].childNodes;

	tags = false;
	regex = new RegExp("(#.*)$");
	if (regex.test(fils3[0].nodeValue)) {
		tags = true;
		hachtag = RegExp.$1
	}

	regex = new RegExp("^.*:(.*)$");
	regex2 = new RegExp("^@(.*)$");
	if (regex.test(fils[2].nodeValue)) {
		commentaire = RegExp.$1;
		if (regex2.test(fils2[0].nodeValue)) {
			login = RegExp.$1;
			if (tags) {
				comment_req = "id=" + env.actif.id + "&login=" + login
						+ "&commentaire="
						+ encodeURIComponent(commentaire + hachtag);
			} else {
				comment_req = "id=" + env.actif.id + "&login=" + login
						+ "&commentaire=" + encodeURIComponent(commentaire);
			}
			$.ajax({
				type : "GET",
				url : " http://localhost:8080/AFFES-BENCHADI/removeComment",
				data : comment_req,
				dataType : "json",
				// if received a response from the server
				success : function(json, textStatus, jqXHR) {
					if (json.KeyNexistePas == 10) {
						alert("KeyNexistePas");
						return false;
					} else if (json.PbArgument == -1) {
						alert("PbArgument");
						return false;
					} else {
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					console.log("Something really bad happened " + textStatus);
				}
			});
			body.removeChild(div);
		}
	}
}

function profile(login) {
	$.ajaxSetup({
		async : false
	});
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/getID",
		data : "login=" + login,
		dataType : "json",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if (json.PbArgument == -1) {
				alert("PbArgument");
				return false;
			} else if (json.idOuNomExistePas == 9) {
				alert("idOuNomExistePas");
				return false;
			} else {
				id = json.id;
				id_from = id;
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
			$("#inscription").html(jqXHR.responseText);
		},
	});
}

function getCommentAll() {
	tab = [];
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/getCommentAll",
		dataType : "text",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if (json.PbArgument == -1) {
				alert("PbArgument");
				return false;
			} else if (json.idOuNomExistePas == 9) {
				alert("idOuNomExistePas");
				return false;
			} else {
				listFriends();
				rechercheCommentaire.traiteReponseComment(json);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);

		},
	});
}

function func_error_recherche(msg) {
	var msg_box = "<div id='msg_err'>" + msg + "</div>";
	var old_msg = $("#msg_err");
	if (old_msg.length == 0) {
		$("gauche").prepend(msg_box);
	} else {
		old_msg.replaceWith(msg_box);
	}
	$('#msg_err').css({
		"color" : "red",
		"font-family" : "maPolice, cursive, sans-serif"
	});
}


function changePicture() {
	var imgProfile = document.getElementById("imageProfile");
	var input = document.getElementById("fileProfile");
	if(input.value != 0){
		imgProfile.src = input.value;
	}
}


function hachTag() {
	tab = [];
	hachtag = document.getElementById("hachtag");
	choix = "";
	if ($('#choix1').is(':checked') && $('#choix2').is(':checked')) {
		func_error_recherche("Vous devez cocher qu'une seul case");
	} else {
		if ($('#choix1').is(':checked')) {
			choix = "Amis";
		}
		if ($('#choix2').is(':checked')) {
			choix = "Tout le monde";
		}
		$.ajax({
			type : "GET",
			url : " http://localhost:8080/AFFES-BENCHADI/search",
			data : "id=" + env.actif.id + "&amiOuTout=" + choix
					+ "&commentaire=" + encodeURIComponent(hachtag.value),
			dataType : "text",
			// if received a response from the server
			success : function(json, textStatus, jqXHR) {
				if (json.PbArgument == -1) {
					func_error_recherche("PbArgument");
					return false;
				} else if (choix == "") {
					func_error_recherche("VousDevezCocherLuneDesCheckBox");
					return false;
				} else {
					actu = false;
					listFriends();
					charger_page("b", true,"Resultat de la recherche");
					rechercheCommentaire.traiteReponseComment(json);
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("Something really bad happened " + textStatus);
				$("#inscription").html(jqXHR.responseText);
			},
		});
	}
}
function actualite() {
	charger_page("b", true, "Fil d'actualite");
	main(env.id, true);
}
function charger_page(a, pasMain, type) {
	login = env.actif.login;
	query = '<div class="header"><div id="logo2"><img src="logotweeto.png" />'
			+ '</div>' + '<div id="deconex">'
			+'<input id="fileProfile" type="file"/>'
			+ '<input type="submit" onClick="changePicture()"/>'
			+ '<input id="friends" type="text" placeholder="Recherche" style="margin-right:30px" onkeyup="searchFriends(this)"/>'
			+ '<img src="Avatar.png" style="width: 25px;height: 25px"/>'
			+ '<a id="'
			+ env.actif.id
			+ '" OnClick="charger_page(this,false,prof)" style="margin-right:20px">'
			+ login
			+ '</a>'
			+ '<a id="accueil" onclick="actualite()" style="margin-right:20px">Accueil</a> '
			+ '<a href="#" OnClick="deconnexion()" title="Deconnexion">Deconnexion</a>'
			+ '</div>'
			+ '<div id="listAmi"><select id="selectAmi"><option>Ami</option></select></div>'
			+ '<div id="profil">'
			+ '<img id="imageProfile" src="Avatar.png"/>'
			+ '<span>'
			+ login
			+ '</span>'
			+ '<span>'
			+ type
			+ '</span>'
			+ '</div>	'
			+ '</div>'
			+ '<div id="milieu">'
			+ '<div id="gauche">'
			+ '<div id="search">'
			+ '<span>Recherche par hashtag : </span>'
			+ '<input id="hachtag" type="text" name="motCle"/>'
			+ '</div>'
			+ '<div id="bouton">'
			+ '<button type="button" value="rechercher" onClick="hachTag()">Rechercher</button>'
			+ '</div>'
			+ '<div id="msg_err"></div>'
			+ '<div id="filtrer">'
			+ '<span>Filtrer les Tweets : </span>'
			+ '<input type="checkbox" id="choix1" value="1"> Amis'
			+ '<input type="checkbox" id="choix2" value="2"> Tout le monde'
			+ '</div>'
			+ '</div>'
			+

			'<div id="addArea">'
			+ '<textarea id="comment" maxlength="200" style="overflow: hidden"'
			+ 'onClick="javascript:erase(this)">Ecrivez votre tweet</textarea>'
			+ '<div id="commentArea">'
			+ '<button id="ajout" type="button" onclick="div_Comment()">Tweeter</button>'
			+ '</div>'
			+ '</div>'
			+

			'<div id="droite">'
			+ '<span id="centrenotif">Centre de notifications : </span>'
			+ '</div>' + '</div>';
	document.body.innerHTML = query;
	if (!pasMain) {
		if(a.id == undefined){
			document.body.onload = main(a, false);
		}else{
			document.body.onload = main(a.id, false);	
		}
	}
}


function ajouter_ami(image) {
	profile(image.name);
	dataString = "id=" + env.actif.id + "&id_to=" + id_from;
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/friendRequest",
		data : dataString,
		dataType : "json",
		success : function(json, textStatus, jqXHR) {
			if (json.IdDoesntExist == -1) {
				return false;
			}
			if (json.Ajout == 1) {
				image.src = "envoye.png";
				image.setAttribute("height", "35");
				image.setAttribute("width", "35");
				image.setAttribute("margin-top","3cm");
				return true;
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		},
	});
}

function supprimer_ami(image) {
	profile(image.name);
	dataString = "key=" + env.key + "&id_friend=" + id_from;
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/removeFriends",
		data : dataString,
		dataType : "json",
		success : function(json, textStatus, jqXHR) {
			if (json.KeyDoesntExist == 7) {
				return false;
			}
			if (json.PbArgument == -1) {
				return false;
			}
			if (json.amiSup == 1) {
				alert("ami supprimé");
				accepte = document.getElementsByName(image.name);
				for (var i = 0; i < accepte.length; i++) {
					accepte[i].src = "ajout.png";
					accepte[i].setAttribute("onclick", "ajouter_ami(this)");
				}
				env.users[id_to].modifieStatus;
				return true;
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		},
	});
}


function main(id_uti, actualite) {
	actu = actualite;
	if (actualite) {
		getCommentAll();
		id_from = id_uti;
	} else {
		id_from = "id=" + id_uti;
		listFriends();
		envoiCommentaire();
	}
}

