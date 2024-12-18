const entityId = parseId();

const responseLabel = document.getElementById("response");
responseLabel.style.display = "none";

const form = document.getElementById("form");

function parseId() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    return parseInt(urlParams.get("id"));
}

async function onPageLoaded() {
    document.getElementById("delete-button").setAttribute("href", `/${apiPath}/delete?id=${entityId}`);
    document.getElementById("back-button").setAttribute("href", `/${apiPath}/view`);

    const entityJson = await fetch(`/api/${apiPath}/byId/${entityId}`, {
        method: "GET",
        headers: { "Content-type": "application/json; charset=UTF-8" },
    }).then((response) => response.json());
    updatePage(entityJson);
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const formParams = new FormData(form);
    const updateEntity = getUpdateEntity(formParams);

    const response = await fetch(`/api/${apiPath}`, {
        method: "PUT",
        body: JSON.stringify(updateEntity),
        headers: { "Content-type": "application/json; charset=UTF-8" },
    });
    responseLabel.style.display = "block";
    if (response.ok) {
        responseLabel.style.color = "green";
        responseLabel.innerText = "Success!";
        updatePage(updateEntity);
    } else {
        const errorMessage = (await response.json())["error"];
        responseLabel.style.color = "red";
        responseLabel.innerText = `Error: ${errorMessage}`;
    }
})
