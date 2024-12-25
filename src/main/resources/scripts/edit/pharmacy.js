const entityType = "pharmacy";
const entityTypeName = "Pharmacy";

const latitudeField = document.getElementById("latitude");
const longitudeField = document.getElementById("longitude");
const websiteField = document.getElementById("website");

const oldUpdateForm = updateForm;
updateForm = function (json) {
    oldUpdateForm(json);

    latitudeField.value = json["latitude"];
    longitudeField.value = json["longitude"];

    const website = json["website"];
    if (website) websiteField.value = website;
    else websiteField.value = "";
};

formToUpdateJson = () => {
    const json = formToCreateJson();
    json["id"] = idField.value;    
};

formToCreateJson = () => ({
    name: nameField.value,
    website: websiteField.value,
    latitude: latitudeField.value,
    longitude: longitudeField.value
});
