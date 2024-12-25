const entityType = "medicine";
const entityTypeName = "Medicine";

formToUpdateJson = () => {
    const json = formToCreateJson();
    json["id"] = idField.value;    
};

formToCreateJson = () => ({
    name: nameField.value,
});
