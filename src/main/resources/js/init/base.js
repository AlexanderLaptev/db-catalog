var ajaxURL = 'http://localhost:8080/api/' + apiRootName + '/all'

var table = new Tabulator('#main-table', {
    pagination:true,
    paginationSize:30,
    paginationSizeSelector:[15, 30, 60, 90, 120],
    paginationCounter:'rows',
    paginationMode:'remote',

    ajaxURL:ajaxURL,
    layout:'fitDataStretch',
    columns:columns,
});

table.on('rowClick', function (e, row) {
    var id = row.getData().id;
    location.href = 'http://localhost:8080/' + apiRootName + '/edit?id=' + id;
});
