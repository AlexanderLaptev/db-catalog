const AJAX_URL = `/api/${API_PATH}/all`;

const table = new Tabulator('#main-table', {
    pagination:true,
    paginationSize:30,
    paginationSizeSelector:[15, 30, 60, 90, 120],
    paginationCounter:'rows',
    paginationMode:'remote',

    ajaxURL:AJAX_URL,
    layout:'fitDataStretch',
    columns:columns,
});

table.on('rowClick', (_, row) => {
    const id = row.getData().id;
    location.href = `/${API_PATH}/edit?id=${id}`;
});
