fetchData()

async function fetchData() {
    const apiUrl = 'http://localhost:8080/api/medicine/all';
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error('Network response was not ok');
        const data = await response.json();
        displayDataInTable(data);
    } catch (error) {
        console.error('Error fetching data:', error);
        document.getElementById('tableContainer').innerHTML = `<p>Error fetching data: ${error.message}</p>`;
    }
}

function displayDataInTable(data) {
    if (!Array.isArray(data) || data.length === 0) {
        document.getElementById('tableContainer').innerHTML = `<p>No data available</p>`;
        return;
    }

    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    const headerRow = document.createElement('tr');
    Object.keys(data[0]).forEach(key => {
        const th = document.createElement('th');
        th.textContent = key;
        headerRow.appendChild(th);
    });
    thead.appendChild(headerRow);

    data.forEach(item => {
        const row = document.createElement('tr');
        Object.values(item).forEach(value => {
            const td = document.createElement('td');
            td.textContent = value;
            row.appendChild(td);
        });
        tbody.appendChild(row);
    });

    table.appendChild(thead);
    table.appendChild(tbody);
    document.getElementById('tableContainer').innerHTML = '';
    document.getElementById('tableContainer').appendChild(table);
}
