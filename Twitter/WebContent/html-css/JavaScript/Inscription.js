function annuler() {
	var form = document.getElementById('inscription');
	form.prenom.value = "";
	form.nom.value = "";
	form.login.value = "";
	form.pass.value = "";
	form.pass2.value = "";
	form.mail.value = "";
	
}

function inscriptions(form) {
	var login = form.login.value;
	var password = form.pass.value;
	var verif = form.pass2.value;
	var mail = form.mail.value;
	var ok = verif_form_connexion(login, password, verif, mail);
	return ok;
}

function func_error_inscription(msg) {
	var msg_box = "<div id='msg_err'><span id='erreur'>" + msg
			+ "</span></div>";
	var old_msg = $("#msg_err");
	if (old_msg.length == 0) {
		$("form").prepend(msg_box);
	} else {
		old_msg.replaceWith(msg_box);
	}
}

function verif_form_connexion(login, password, verif, mail) {
	if (login.length == 0 && password.length == 0 && mail.length == 0) {
		func_error_inscription("login et password vide");
		return false;
	}

	if (login.length == 0) {
		func_error_inscription("login vide");
		return false;
	}

	if (password.length == 0) {
		func_error_inscription("password vide");
		return false;
	}
	
	if (mail.length == 0) {
		func_error_inscription("mail vide");
		return false;
	}

	if (login.length > 20 && password.length < 8) {
		func_error_inscription("login trop long\n et  password trop petit ");
		return false;
	}

	if (login.length > 20) {
		func_error_inscription("login 20 max ");
		return false;
	}

	if (password.length < 8) {
		func_error_inscription("password 8 min ");
		return false;
	}

	if (verif != password) {
		func_error_inscription("retapez votre password");
		return false;
	}
	
	if (mail.length != 0) {
		regex = new RegExp("^.*@.*$");
		if (!regex.test(mail)) {
			func_error_inscription("Mail Incorrect");
			return false;
		}
	}
	// get the form data and then serialize that
	// dataString = $("#inscription").serialize();
	var countryCode = $("input#lo").val();
	dataString = "login=" + countryCode;
	countryCode = $("input#motdePass").val();
	dataString = dataString + "&" + "password=" + countryCode;
	countryCode = $("input#no").val();
	dataString = dataString + "&" + "nom=" + countryCode;
	countryCode = $("input#pre").val();
	dataString = dataString + "&" + "prenom=" + countryCode;
	countryCode = $("input#email").val();
	dataString = dataString + "&" + "mail=" + countryCode;
	// make the AJAX request, dataType is set to json
	// meaning we are expecting JSON data in response from the server
	$.ajax({
		type : "GET",
		url : " http://localhost:8080/AFFES-BENCHADI/createUser",
		data : dataString,
		dataType : "json",
		// if received a response from the server
		success : function(json, textStatus, jqXHR) {
			if (json.LoginExistant == 2) {
				func_error_inscription("LoginExistant");
				return false;
			}
			if (json.PbArgument == -1) {
				func_error_inscription("PbArgument");
				return false;
			} else {
				annuler();
				func_error_inscription("Vous pouvez vous connecter :)");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("Something really bad happened " + textStatus);
			$("#inscription").html(jqXHR.responseText);
		},
	});

	return true;
}
