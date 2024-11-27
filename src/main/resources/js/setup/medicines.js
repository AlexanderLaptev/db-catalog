var data = fetchData('http://localhost:8080/api/medicine/all').then(function (data) {
    var table = new Tabulator("#table-container", {
        height:'100vh',
        data:data,
        layout:'fitColumns',
//        columns: [
//            {title:'ID', field:'id'},
//            {title:'Name', field:'name'},
//        ],
        autoColumns:'full',
    });
});

