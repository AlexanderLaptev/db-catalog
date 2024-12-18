const METHOD = "POST";

deleteButton.style.display = "none";
document.title = `Adding ${NAME}`;
heading.innerText = document.title;

async function onPageLoaded() {
    setupHeaderButtons();
}

async function handleResponse(response) {
    if (response.ok) {
        const entityId = (await response.json())["id"];
        window.location.href = `/${API_PATH}/edit?id=${entityId}`;
    } else {
        displayErrorMessage(response);
    }
}
