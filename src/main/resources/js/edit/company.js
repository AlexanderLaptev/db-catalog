const getEntityJsonFromForm = (formParams) => ({
    "id": ENTITY_ID,
    "name": formParams.get("name"),
    "country_code": formParams.get("country").toUpperCase(),
});

function updatePage(entityJson) {
    const name = entityJson["name"];
    const country = entityJson["country_code"].toUpperCase();

    const title = `Editing ${name}`;
    const heading = `${title} (${ENTITY_ID})`;

    document.title = title;
    document.getElementById("heading").innerText = heading;
    document.getElementById("name").value = name;
    document.getElementById("country").value = country;
}
