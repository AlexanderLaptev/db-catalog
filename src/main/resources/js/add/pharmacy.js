const apiPath = "pharmacy";

document.title = "Adding Pharmacy";
document.getElementById("heading").innerText = document.title;

const getCreateEntity = (formParams) => ({
    "name": formParams.get("name"),
    "website": formParams.get("website"),
    "latitude": formParams.get("latitude"),
    "longitude": formParams.get("longitude"),
});

function updatePage(entityJson) {
    document.getElementById("delete-button").display = "block";

    const entityId = entityJson["id"];
    const name = entityJson["name"];
    const website = entityJson["website"];
    const latitude = entityJson["latitude"];
    const longitude = entityJson["longitude"];

    const title = `Editing ${name}`;
    const heading = `${title} (${entityId})`;

    document.title = title;
    document.getElementById("heading").innerText = heading;
    document.getElementById("name").value = name;
    document.getElementById("latitude").value = latitude;
    document.getElementById("longitude").value = longitude;

    if (website !== undefined) {
        document.getElementById("website").value = website;
    } else {
        document.getElementById("website").value = "";
    }
}
