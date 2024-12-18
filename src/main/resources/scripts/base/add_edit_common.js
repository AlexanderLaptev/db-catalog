const form = document.getElementById("form");
const heading = document.getElementById("heading");
const responseLabel = document.getElementById("response-label");
const deleteButton = document.getElementById("delete-button");
const backButton = document.getElementById("back-button");

hideResponseLabel();

function setupHeaderButtons() {
    backButton.setAttribute("href", `/${API_PATH}/view`);
    if (typeof ENTITY_ID !== "undefined") {
        deleteButton.setAttribute("href", `/${API_PATH}/delete?id=${ENTITY_ID}`);
    }
}

function hideResponseLabel() {
    responseLabel.style.display = "none";
}

function displayResponseMessage(message, color) {
    responseLabel.style.display = "inline";
    responseLabel.style.color = color;
    responseLabel.innerText = message;
}

function displaySuccessMessage() {
    displayResponseMessage("Success!", "green");
}

async function displayErrorMessage(response) {
    const json = await response.json();
    displayResponseMessage(`Error (${response.status}): ${json["error"]}`, "red");
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const formParams = new FormData(form);
    const entityJson = getEntityJsonFromForm(formParams);

    hideResponseLabel();
    const response = await fetch(`/api/${API_PATH}`, {
        method: METHOD,
        body: JSON.stringify(entityJson),
        headers: { "Content-type": "application/json; charset=UTF-8" },
    });
    handleResponse(response);
});
