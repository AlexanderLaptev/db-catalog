const apiPath = "medicine";

const getUpdateEntity = (formParams) => ({
    "id": entityId,
    "name": formParams.get("name"),
});

function updatePage(entityJson) {
    const medicineName = entityJson["name"];

    const title = `Editing ${medicineName}`;
    const heading = `${title} (${entityId})`;

    document.title = title;
    document.getElementById("heading").innerText = heading;
    document.getElementById("name").value = medicineName;
}
