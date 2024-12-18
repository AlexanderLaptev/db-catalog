const METHOD = "PUT";
const ENTITY_ID = parseId();

function parseId() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    return parseInt(urlParams.get("id"));
}

async function onPageLoaded() {
    setupHeaderButtons();
    loadEntityJson();
}

async function loadEntityJson() {
    const entityJson = await fetch(`/api/${API_PATH}/byId/${ENTITY_ID}`, {
        method: "GET",
        headers: { "Content-type": "application/json; charset=UTF-8" },
    }).then((response) => response.json());
    updatePage(entityJson);
}

async function handleResponse(response) {
    if (response.ok) {
        displaySuccessMessage();
        loadEntityJson();
    } else {
        displayErrorMessage(response);
    }
}
