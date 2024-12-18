const getEntityJsonFromForm = (formParams) => ({
    "name": formParams.get("name"),
    "country_code": formParams.get("country").toUpperCase(),
});
