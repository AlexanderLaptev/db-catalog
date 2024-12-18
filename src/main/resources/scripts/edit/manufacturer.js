onPageLoaded = function() {
    const params = new URLSearchParams(window.location.search);
    const medicineId = params.get("medicine");
    const companyId = params.get("company");
    
    document.title = `Editing ${NAME}`;
    heading.innerText = document.title;
    
    backButton.setAttribute("href", `/${API_PATH}/view`);
    deleteButton.setAttribute("href", `/${API_PATH}/delete?medicine=${medicineId}&company=${companyId}`);
    
    document.getElementById("submit").disabled = true;
    document.getElementById("medicine-id").disabled = true;
    document.getElementById("medicine-id").value = medicineId;
    document.getElementById("company-id").disabled = true;
    document.getElementById("company-id").value = companyId;
}

form.removeEventListener("submit", handleSubmit);
