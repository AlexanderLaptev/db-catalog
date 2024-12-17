var table = new Tabulator('#main-table', {
    height:'86vh',
    pagination:true,
    paginationMode:'remote',
    ajaxURL:'http://localhost:8080/api/pharmacy/all',
    layout:'fitDataStretch',
    columns: [
        {title:'ID', field:'id'},
        {title:'Name', field:'name'},
        {title:'Website', field:'website'},
        {title:'Latitude', field:'latitude'},
        {title:'Longitude', field:'longitude'},
    ],
});

table.on('rowClick', function (e, row) {
    var id = row.getData().id;
    location.href = 'http://localhost:8080/edit/pharmacy/' + id;
});
