document.getElementById("medicine-id").readOnly = true;
document.getElementById("pharmacy-id").readOnly = true;

const getEntityJsonFromForm = (formParams) => ({
    "medicine_id": formParams.get("medicine-id"),
    "pharmacy_id": formParams.get("pharmacy-id"),
    "count": formParams.get("count"),
    "price": formParams.get("price"),
});

loadEntityJson = async function () {
    const queryString = window.location.search;
    const entityJson = await fetch(`/api/stock${queryString}`, {
        method: "GET",
        headers: JSON_HEADERS,
    }).then((response) => response.json());
    updateTitleAndHeading(entityJson);
    updatePage(entityJson);
}

setupHeaderButtons = function () {
    backButton.setAttribute("href", `/${API_PATH}/view`);
    deleteButton.setAttribute("href", `/${API_PATH}/delete${window.location.search}`);
}

updateTitleAndHeading = async function () {
    document.title = `Editing ${NAME}`;
    document.getElementById("heading").innerText = document.title;
}

function updatePage(entityJson) {
    const medicineId = entityJson["medicine_id"];
    const pharmacyId = entityJson["pharmacy_id"];
    const count = entityJson["count"];
    const price = entityJson["price"];

    document.getElementById("medicine-id").value = medicineId;
    document.getElementById("pharmacy-id").value = pharmacyId;
    document.getElementById("count").value = count;
    document.getElementById("price").value = price;
}
