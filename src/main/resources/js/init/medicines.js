var table = new Tabulator("#main-table", {
    height:'85vh',
    ajaxURL:'http://localhost:8080/api/medicine/all',
    layout:'fitDataStretch',
    columns: [
        {title:'ID', field:'id'},
        {title:'Name', field:'name'},
    ],
});

table.on('rowClick', function (e, row) {
    var id = row.getData().id;
    location.href = 'http://localhost:8080/edit/medicine/' + id;
});
