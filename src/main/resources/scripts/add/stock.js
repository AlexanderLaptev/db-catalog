const getEntityJsonFromForm = (formParams) => ({
    "medicine_id": formParams.get("medicine-id"),
    "pharmacy_id": formParams.get("pharmacy-id"),
    "count": formParams.get("count"),
    "price": formParams.get("price"),
});

handleResponse = async function (response) {
    if (response.ok) {
        const medicineId = document.getElementById("medicine-id").value;
        const pharmacyId = document.getElementById("pharmacy-id").value;
        window.location.href = `/stock/edit?medicine=${medicineId}&pharmacy=${pharmacyId}`;
    } else {
        displayErrorMessage(response);
    }
}
