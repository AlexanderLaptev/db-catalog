const getEntityJsonFromForm = (formParams) => ({
    "medicine_id": formParams.get("medicine-id"),
    "company_id": formParams.get("company-id"),
});

hideResponseLabel();
form.removeEventListener("submit", handleSubmit);
handleSubmit = async function (event) {
    event.preventDefault();

    const medicineId = document.getElementById("medicine-id").value;
    const companyId = document.getElementById("company-id").value;
    const queryString = `?medicine=${medicineId}&company=${companyId}`;

    const response = await fetch(`/api/manufacturer${queryString}`, { method: "POST" });
    if (response.ok) {
        window.location.href = `/manufacturer/edit${queryString}`;
    } else {
        displayErrorMessage(response);
    }
}
form.addEventListener("submit", handleSubmit);
