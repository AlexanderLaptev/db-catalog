const table = new Tabulator("#results", {
    pagination: true,
    paginationSize: 30,
    paginationSizeSelector: [15, 30, 60, 90, 120],
    paginationCounter: "rows",
    paginationMode: "remote",

    ajaxURL: `/api/search${location.search}`,

    layout: "fitDataStretch",
    columns: [
        { title: "Medicine name", field: "medicine_name" },
        { title: "Company name", field: "company_name" },
        { title: "Country", field: "country" },
        { title: "Pharmacy name", field: "pharmacy_name" },
        { title: "Coordinates", field: "pharmacy_geo" },
        { title: "Price", field: "price" },
        { title: "Count", field: "count" },
    ],
});
