const getEntityJsonFromForm = (formParams) => ({
    "id": ENTITY_ID,
    "name": formParams.get("name"),
    "country_code": formParams.get("country").toUpperCase(),
});

function updatePage(entityJson) {
    const name = entityJson["name"];
    const country = entityJson["country_code"].toUpperCase();

    document.getElementById("name").value = name;
    document.getElementById("country").value = country;
}
