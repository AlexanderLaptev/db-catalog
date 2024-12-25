const RESPONSE_LABEL_DISPLAY = "inline";
const RESPONSE_LABEL_OK_COLOR = "green";
const RESPONSE_LABEL_ERROR_COLOR = "red";

const EDIT_VERB = "Editing";
const ADD_VERB = "Add";

const splitPath = location.pathname.split("/");
const searchParams = new URL(location.href).searchParams;
const action = splitPath[splitPath.length - 1];

const heading = document.getElementById("heading");
const backButton = document.getElementById("back-button");
const deleteButton = document.getElementById("delete-button");
const responseLabel = document.getElementById("response-label");
const form = document.getElementById("form");

const idField = document.getElementById("id");
const nameField = document.getElementById("name");

let getFetchUrl = () => `/api/${entityType}/byId/${searchParams.get("id")}`;
let getUpdateUrl = () => `/api/${entityType}`;
let getCreateUrl = getUpdateUrl;

let getEditUrl = () => `/edit/${entityType}/id=${searchParams.get("id")}`;
let getBackButtonUrl = () => `/${entityType}/view`;
let getDeleteUrlSearchString = () => `id=${searchParams.get("id")}`;

let getEditTitleSuffix = (json) => json["name"];
let getEditHeadingSuffix = (json) => `${entityTypeName}: ${json["name"]}`;

// TODO: handle missing entity
function onPageLoaded() {
    backButton.href = getBackButtonUrl();
    switch (action) {
        case "edit":
            handleEdit();
            break;
        case "add":
            handleAdd();
            break;
    }
}

document.getElementById("form").addEventListener("submit", (event) => {
    event.preventDefault();
    switch (action) {
        case "edit":
            submitEdit();
            break;
        case "add":
            submitAdd();
            break;
    }
});

async function handleEdit() {
    deleteButton.style.display = "inline";
    deleteButton.href = `/${entityType}/delete?${getDeleteUrlSearchString()}`;

    const json = await fetchEntityJson();

    updateTitles(json);
    updateForm(json);
    setIdFieldsFrozen(true);
}

async function fetchEntityJson() {
    const response = await fetch(getFetchUrl(), { method: "GET" });
    return response.json();
}

function updateForm(json) {
    idField.value = searchParams.get("id");
    nameField.value = json["name"];
}

function updateTitles(json) {
    if (json) {
        document.title = `${EDIT_VERB} ${getEditTitleSuffix(json)}`;
        heading.innerText = `${EDIT_VERB} ${getEditHeadingSuffix(json)}`;
    } else {
        document.title = `${ADD_VERB} ${entityTypeName}`;
        heading.innerText = `${ADD_VERB} ${entityTypeName}`;
    }
}

function setIdFieldsFrozen(frozen) {
    // if (idField) idField.readOnly = frozen;
}

async function submitEdit() {
    responseLabel.style.display = "none";

    if (!validateForm()) return;

    const updateJson = formToUpdateJson();
    const response = await sendJsonToServer(getUpdateUrl(), updateJson, "PUT");

    responseLabel.style.display = RESPONSE_LABEL_DISPLAY;
    if (response.ok) {
        responseLabel.style.color = RESPONSE_LABEL_OK_COLOR;
        responseLabel.innerText = `Success!`;

        const json = await fetchEntityJson();
        updateTitles(json);
        updateForm(json);
    } else {
        const error = (await response.json())["error"];
        responseLabel.style.color = RESPONSE_LABEL_ERROR_COLOR;
        responseLabel.innerText = `Error (${response.status}): ${error}`;
    }
}

function formToUpdateJson() { }

// TODO: proper client-side validation
function validateForm() { return true; }

async function sendJsonToServer(url, json, method) {
    return fetch(url, {
        method: method,
        body: JSON.stringify(json),
        headers: { "Content-type": "application/json; charset=UTF-8" },
    });
}

function handleAdd() {
    deleteButton.style.display = "none";

    updateTitles(null);
    setIdFieldsFrozen(false);
}

async function submitAdd() {
    responseLabel.style.display = "none";

    if (!validateForm()) return;

    const createJson = formToCreateJson();
    const response = await sendJsonToServer(getCreateUrl(), createJson, "POST");

    if (response.ok) {
        const id = (await response.json())["id"];
        document.location.replace(`/edit/${entityType}?id=${id}`);
    } else {
        const error = (await response.json())["error"];
        responseLabel.style.display = RESPONSE_LABEL_DISPLAY;
        responseLabel.style.color = RESPONSE_LABEL_ERROR_COLOR;
        responseLabel.innerText = `Error (${response.status}): ${error}`;
    }
}
