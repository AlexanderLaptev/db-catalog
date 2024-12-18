const getEntityJsonFromForm = (formParams) => ({
    "name": formParams.get("name"),
    "website": formParams.get("website"),
    "latitude": formParams.get("latitude"),
    "longitude": formParams.get("longitude"),
});
