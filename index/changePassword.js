function validateForm(form) {
	if (!validateMatch(form.password1, form.password2)) {
		document.getElementById("msg").innerHTML = "Passwords do not match.";
		return false;
	}

	if (!validateLength(form.password1)) {
		document.getElementById("msg").innerHTML = "Passwords must be at least 6 characters long.";
		return false;
	}

	return true;
}

function validateMatch(field1, field2) {
	return field1.value != null && field1.value == field2.value;
}

function validateLength(field) {
	return field.value != null && field.value.length >= 6;
}
