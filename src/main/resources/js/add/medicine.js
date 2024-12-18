const apiPath = "medicine";

document.title = "Adding Medicine";
document.getElementById("heading").innerText = document.title;

const getCreateEntity = (formParams) => ({
    "name": formParams.get("name"),
});

function updatePage(entityJson) {
    document.getElementById("delete-button").display = "block";

    const entityId = entityJson["id"];
    const name = entityJson["name"];

    const title = `Editing ${name}`;
    const heading = `${title} (${entityId})`;

    document.title = title;
    document.getElementById("heading").innerText = heading;
    document.getElementById("name").value = name;
}
