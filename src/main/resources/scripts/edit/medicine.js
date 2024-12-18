const getEntityJsonFromForm = (formParams) => ({
    "id": ENTITY_ID,
    "name": formParams.get("name"),
});

function updatePage(entityJson) {
    const name = entityJson["name"];
    document.getElementById("name").value = name;
}
