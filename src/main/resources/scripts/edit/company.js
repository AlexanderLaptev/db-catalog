const entityType = "company";
const entityTypeName = "Company";

const countryField = document.getElementById("country");

const oldUpdateForm = updateForm;
updateForm = function (json) {
    oldUpdateForm(json);
    countryField.value = json["country_code"].toUpperCase();
};

formToUpdateJson = () => ({
    "id": idField.value,
    "name": nameField.value,
    "country_code": countryField.value,
});
