const columns = [
    {
        title: "Medicine ID",
        field: "medicine_id",
        formatter: "link",
        formatterParams: { url: (cell) => `/medicine/edit?id=${cell.getValue()}` },
    },
    {
        title: "Pharmacy ID",
        field: "pharmacy_id",
        formatter: "link",
        formatterParams: { url: (cell) => `/pharmacy/edit?id=${cell.getValue()}` },
    },
    { title: "Count", field: "count" },
    { title: "Price", field: "price", formatter: "money" },
];

function rowClickHandler(event, row) {
    const data = row.getData();
    location.href = `/${API_PATH}/edit?medicine=${data.medicine_id}&pharmacy=${data.pharmacy_id}`;
}
