const getEntityJsonFromForm = (formParams) => ({
    "id": ENTITY_ID,
    "name": formParams.get("name"),
});

function updatePage(entityJson) {
    const name = entityJson["name"];

    const title = `Editing ${name}`;
    const heading = `${title} (${ENTITY_ID})`;

    document.title = title;
    document.getElementById("heading").innerText = heading;
    document.getElementById("name").value = name;
}
