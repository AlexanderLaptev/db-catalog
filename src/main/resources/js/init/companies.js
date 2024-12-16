var table = new Tabulator('#main-table', {
    height:'86vh',
    pagination:true,
    paginationMode:'remote',
    ajaxURL:'http://localhost:8080/api/company/all',
    layout:'fitDataStretch',
    columns: [
        {title:'ID', field:'id'},
        {title:'Country', field:'countryCode'},
        {title:'Name', field:'name'},
    ],
});

table.on('rowClick', function (e, row) {
    var id = row.getData().id;
    location.href = 'http://localhost:8080/edit/company/' + id;
});
