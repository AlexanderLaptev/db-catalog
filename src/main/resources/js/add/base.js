const responseLabel = document.getElementById("response");
responseLabel.style.display = "none";

const form = document.getElementById("form");
const deleteButton = document.getElementById("delete-button");
const backButton = document.getElementById("back-button");

async function onPageLoaded() {
    deleteButton.style.display = "none";
    backButton.setAttribute("href", `/${apiPath}/view`);
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const formParams = new FormData(form);
    const createEntity = getCreateEntity(formParams);

    const response = await fetch(`/api/${apiPath}`, {
        method: "POST",
        body: JSON.stringify(createEntity),
        headers: { "Content-type": "application/json; charset=UTF-8" },
    });

    responseLabel.style.display = "inline";
    if (response.ok) {
        const entityId = (await response.json())["id"];
        window.location.href = `/${apiPath}/edit?id=${entityId}`;
    } else {
        const errorMessage = (await response.json())["error"];
        responseLabel.style.color = "red";
        responseLabel.innerText = `Error: ${errorMessage}`;
    }
})
