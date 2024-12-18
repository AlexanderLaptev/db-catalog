const apiPath = "company";

const getUpdateEntity = (formParams) => ({
    "id": entityId,
    "name": formParams.get("name"),
    "country_code": formParams.get("country").toUpperCase(),
});

function updatePage(entityJson) {
    const companyName = entityJson["name"];
    const companyCountry = entityJson["country_code"].toUpperCase();

    const title = `Editing ${companyName}`;
    const heading = `${title} (${entityId})`;

    document.title = title;
    document.getElementById("heading").innerText = heading;
    document.getElementById("name").value = companyName;
    document.getElementById("country").value = companyCountry;
}
