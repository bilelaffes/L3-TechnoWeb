tabId = [];
tabLogin = [];
prof = "Profile ";
function annule(annul) {
	var form = document.getElementById('connexion');
	if(annul.value = "AnnulerEnregistrement"){
		var form = document.getElementById('connexion');
		form.login.value = "";
		form.pass.value = "";
	}
	
	if(annul.value = "AnnulerSaisie"){
		var form = document.getElementById('inscription');
		form.prenom.value = "";
		form.nom.value = "";
		form.login.value = "";
		form.pass.value = "";
		form.pass2.value = "";
	}
}

function func_error(msg) {
	var msg_box = "<div id='msg_err_connection'><span id='erreur'>" + msg
			+ "</span></div>";
	var old_msg = $("#msg_err_connection");
	if (old_msg.length == 0) {
		$("form").prepend(msg_box);
	} else {
		old_msg.replaceWith(msg_box);
	}
}

function notif_ajouts() {
	id = env.actif.id;
	query = "id=" + id;
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/listRequest",
		data : query,
		dataType : "text",
		success : function(json, textStatus, jqXHR) {
			reponse = JSON.parse(json);
			for (i = 0; i < reponse.ajouts.length; i++) {
				tabId[i] = reponse.ajouts[i];
			}
			for (i = 0; i < tabId.length; i++) {
				$.ajax({
					type : "GET",
					url : " http://localhost:8080/AFFES-BENCHADI/getLogin",
					data : "id=" + tabId[i],
					dataType : "json",
					success : function(json, textStatus, jqXHR) {
						tabLogin.push(json.login);
						droite = document.getElementById("droite");
						d = document.createElement('div');
						d.setAttribute("id", "demande" + json.login);
						span = document.createElement('span');
						span.setAttribute("id", "texteAjout");
						span.innerHTML = json.login + " vous demande en ami ";
						b1 = document.createElement('button');
						b1.innerHTML = "Accepter";
						b1.setAttribute("id", "boutonaccept");
						b1.setAttribute("value", json.login);
						b1.setAttribute("font-size", "20");
						b1.setAttribute("onclick", "accepter_ami()");
						b2 = document.createElement('button');
						b2.setAttribute("id", "boutonrefuse");
						b2.setAttribute("value", json.login);
						b2.innerHTML = "Refuser";
						b2.setAttribute("font-size", "20");
						b2.setAttribute("onclick", "refuser_ami()");
						d.appendChild(span);
						d.appendChild(b1);
						d.appendChild(b2);
						droite.appendChild(d);
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log("Something really bad happened "
								+ textStatus);
					},
				});
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		},
	});
}

function verifier() {
	// get the form data and then serialize that
	dataString = $("#connexion").serialize();
	
	var form = document.getElementById('connexion');
	val = form.login;
	val2 = form.pass;
	// make the AJAX request, dataType is set to json
	// meaning we are expecting JSON data in response from the server
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/login",
		data : dataString,
		dataType : "json",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if(json.LoginOuPassWordIcompatible == 15){
				func_error("LoginOuPassWordIcompatible");
				val.value = "";
				val2.value = "";
				return false;
			}
			if (json.PasswordIncorrect == 4) {
				func_error("PasswordIncorrect");
				val2.value = "";
				return false;
			}
			if (json.LoginInexistant == 3) {
				func_error("LoginInexistant");
				val.value = "";
				return false;
			}
			if (json.PbArgument == -1) {
				func_error("PbArgument");
				val.value = "";
				val2.value = "";
				return false;
			} else {
				env.actif = new User(json.id, val.value, undefined);
				env.key = json.key;
				charger_page_principale();
				notif_ajouts();
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		},
	});
	return false;
}
function accepter_ami() {
	b = document.getElementById("boutonaccept");
	login = b.value;
	d = document.getElementById("demande" + login);
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
				id_to = json.id;
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		},
	});
	query = "id=" + env.actif.id + "&id_to=" + id_to;
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/acceptFriend",
		data : query,
		dataType : "json",
		success : function(json, textStatus, jqXHR) {
			if (json.probleme == -1) {
				return false;
			}
			if (json.amitie == 2) {
				string = '<span id="ami">' + login + ' est votre ami</span>';
				d.innerHTML = string;
				accepte = document.getElementsByName(login);
				for (var i = 0; i < accepte.length; i++) {
					accepte[i].src = "sup.png";
					accepte[i].setAttribute("onclick", "supprimer_ami(this)");
				}
				env.users[id_to].modifieStatus;
				return true;
			}
		}
	});

}

function refuser_ami() {
	b = document.getElementById("boutonrefuse");
	login = b.value;
	d = document.getElementById("demande" + login);
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
				id_to = json.id;
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
		},
	});
	query = "id=" + env.actif.id + "&id_to=" + id_to;
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/refuseFriend",
		data : query,
		dataType : "json",
		success : function(json, textStatus, jqXHR) {
			if (json.probleme == -1) {
				alert("il y a eu un probleme");
				return false;
			}
			if (json.requestremoved == 2) {
				s = '<span id="pasami">' + login + ' a ete refuse</span>';
				d.innerHTML = s;
				refuse = document.getElementsByName(login);
				for (var i = 0; i < accepte.length; i++) {
					refuse[i].src = "ajout.png";
					refuse[i].setAttribute("onclick", "ajouter_ami(this)");
				}
				return true;
			}
		}
	});
}

function deconnexion() {
	$("#deconex").click(function() {
		dataString = "key=" + env.key;
		$.ajax({
			type : "GET",
			url : " http://localhost:8080/AFFES-BENCHADI/logout",
			data : dataString,
			dataType : "json",
			success : function(json, textStatus, jqXHR) {
				if (json.KeyNull == 5) {
					alert("keyNull");
					return false;
				}
				if (json.KeyDoesntExist == 6) {
					alert("keyDoesntExist");
					return false;
				} else {
					env.key = undefined;
					env.actif = undefined;
					window.location.href = "Page-conex.html";
				}
			},
			error : function(jqXMR, textStatus, errorThrown) {
				alert(textStatus);
			}

		});

	});
	return false;
}

function charger_page_principale() {
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
			+ '<a id="accueil" onclick="main(env.id,true)" style="margin-right:20px">Accueil</a> '
			+ '<a href=" " OnClick="deconnexion()" title="Deconnexion">Deconnexion</a>'
			+ '</div>'
			+ '<div id="listAmi"><select id="selectAmi"><option>Ami</option></select></div>'
			+ '<div id="profil">'
			+ '<img id="imageProfile" src="Avatar.png"/>'
			+ '<span>'
			+ login
			+ '</span>'
			+ '<span>'
			+ "Fil d'actualite"
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
	document.body.onload = main(env.actif.id, true);
}
