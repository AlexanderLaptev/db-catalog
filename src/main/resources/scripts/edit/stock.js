const entityType = "stock";
const entityTypeName = "Stock";

const medicineId = searchParams.get("medicine")
const pharmacyId = searchParams.get("pharmacy")
const searchString = `medicine=${medicineId}&pharmacy=${pharmacyId}`;

const medicineIdField = document.getElementById("medicine-id");
const pharmacyIdField = document.getElementById("pharmacy-id");
const countField = document.getElementById("count");
const priceField = document.getElementById("price");

getFetchUrl = () => `/api/stock?${searchString}`;
getDeleteUrlSearchString = () => searchString;
getEditTitleSuffix = (_) => entityTypeName;
getEditHeadingSuffix = getEditTitleSuffix;

updateForm = function (json) {
    medicineIdField.value = medicineId;
    pharmacyIdField.value = pharmacyId;
    countField.value = json["count"];
    priceField.value = json["price"];
};

formToUpdateJson = () => ({
    "medicine_id": medicineIdField.value,
    "pharmacy_id": pharmacyIdField.value,
    "count": countField.value,
    "price": priceField.value,
});
