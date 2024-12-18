// TODO: handle missing/empty websites
const getEntityJsonFromForm = (formParams) => ({
    "id": ENTITY_ID,
    "name": formParams.get("name"),
    "website": formParams.get("website"),
    "latitude": formParams.get("latitude"),
    "longitude": formParams.get("longitude"),
});

function updatePage(entityJson) {
    const name = entityJson["name"];
    const website = entityJson["website"];
    const latitude = entityJson["latitude"];
    const longitude = entityJson["longitude"];

    document.getElementById("name").value = name;
    document.getElementById("latitude").value = latitude;
    document.getElementById("longitude").value = longitude;

    if (website !== undefined) {
        document.getElementById("website").value = website;
    } else {
        document.getElementById("website").value = "";
    }
}
