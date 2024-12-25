const entityType = "manufacturer";
const entityTypeName = "Manufacturer";

const medicineId = searchParams.get("medicine")
const companyId = searchParams.get("company")
const searchString = `medicine=${medicineId}&company=${companyId}`;

const medicineIdField = document.getElementById("medicine-id");
const companyIdField = document.getElementById("company-id");

getFetchUrl = () => `/api/manufacturer?${searchString}`;
getEditUrl = () => `/edit/${entityType}/?${searchString}`;
getDeleteUrlSearchString = () => searchString;
getEditTitleSuffix = (_) => entityTypeName;
getEditHeadingSuffix = getEditTitleSuffix;

updateForm = function (json) {
    medicineIdField.value = medicineId;
    companyIdField.value = companyId;
};

formToCreateJson = () => ({
    "medicine_id": medicineIdField.value,
    "company_id": pharmacyIdField.value,
});

setIdFieldsFrozen = (frozen) => {
    medicineId.readOnly = frozen;
    companyId.readOnly = frozen;
}
