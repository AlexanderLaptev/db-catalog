const apiPath = "company";

document.title = "Adding Company";
document.getElementById("heading").innerText = document.title;

const getCreateEntity = (formParams) => ({
    "name": formParams.get("name"),
    "country_code": formParams.get("country").toUpperCase(),
});

function updatePage(entityJson) {
    document.getElementById("delete-button").display = "block";

    const entityId = entityJson["id"];
    const name = entityJson["name"];
    const country = entityJson["country_code"].toUpperCase();

    const title = `Editing ${name}`;
    const heading = `${title} (${entityId})`;

    document.title = title;
    document.getElementById("heading").innerText = heading;
    document.getElementById("name").value = name;
    document.getElementById("country").value = country;
}
