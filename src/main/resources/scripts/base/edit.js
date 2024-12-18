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
        headers: JSON_HEADERS,
    }).then((response) => response.json());
    updateTitleAndHeading(entityJson);
    updatePage(entityJson);
}

function updateTitleAndHeading(entityJson) {
    const name = entityJson["name"];
    const title = `Editing ${name}`;
    const heading = `${title} [${ENTITY_ID}]`;
    document.title = title;
    document.getElementById("heading").innerText = heading;
}

async function handleResponse(response) {
    if (response.ok) {
        displaySuccessMessage();
        loadEntityJson();
    } else {
        displayErrorMessage(response);
    }
}
