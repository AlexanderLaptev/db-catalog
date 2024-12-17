var ajaxURL = 'http://localhost:8080/api/' + apiRootName + '/all'

var table = new Tabulator('#main-table', {
    height:'86vh',
    pagination:true,
    paginationMode:'remote',
    ajaxURL:ajaxURL,
    layout:'fitDataStretch',
    columns:columns,
});

table.on('rowClick', function (e, row) {
    var id = row.getData().id;
    location.href = 'http://localhost:8080/edit/' + apiRootName + '/' + id;
});
