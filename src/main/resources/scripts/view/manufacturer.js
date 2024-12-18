const columns = [
    {
        title: "Medicine ID",
        field: "medicine_id",
        formatter: "link",
        formatterParams: { url: (cell) => `/medicine/edit?id=${cell.getValue()}` },
    },
    {
        title: "Company ID",
        field: "company_id",
        formatter: "link",
        formatterParams: { url: (cell) => `/company/edit?id=${cell.getValue()}` },
    },
    // { title: "Medicine Name", field: "medicine_name" },
    // { title: "Company Name", field: "company_name" },
    // { title: "Company Country", field: "company_country" },
];

// async function ajaxResponse(url, params, response) {
//     for (const entity of response["data"]) {
//         const medicineId = entity["medicine_id"];
//         const companyId = entity["company_id"];

//         const medicineJson = await fetch(`/api/medicine/byId/${medicineId}`, {
//             method: "GET",
//             "headers": { "Content-type": "application/json; charset=UTF-8" }
//         }).then((response) => response.json())
//         const companyJson = await fetch(`/api/company/byId/${companyId}`, {
//             method: "GET",
//             "headers": { "Content-type": "application/json; charset=UTF-8" }
//         }).then((response) => response.json())

//         entity["medicine_name"] = medicineJson["name"];
//         entity["company_name"] = companyJson["name"];
//         entity["company_country"] = companyJson["country_code"].toUpperCase();
//     }
//     return response;
// }
