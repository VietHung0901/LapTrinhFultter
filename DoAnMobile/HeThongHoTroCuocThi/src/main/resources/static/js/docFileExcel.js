// document.addEventListener('DOMContentLoaded', function () {
//     const fileInput = document.getElementById('excelFileInput');
//     const cancelButton = document.getElementById('cancelButton');
//     const tableBody = document.querySelector("#previewTable tbody");
//
//     fileInput.addEventListener('change', function (event) {
//         const file = event.target.files[0];
//
//         if (file && file.type === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
//             const reader = new FileReader();
//             reader.onload = function (e) {
//                 const data = new Uint8Array(e.target.result);
//                 const workbook = XLSX.read(data, { type: 'array' });
//                 const sheet = workbook.Sheets[workbook.SheetNames[0]];
//
//                 const rows = XLSX.utils.sheet_to_json(sheet, { header: 1, defval: "", raw: true });
//                 tableBody.innerHTML = "";
//
//                 rows.slice(1).forEach(row => {
//                     if (row.every(cell => cell === "")) {
//                         return;
//                     }
//                     const newRow = document.createElement("tr");
//
//                     row.forEach((cellData, index) => {
//                         const cellElement = document.createElement("td");
//
//                         if (index === 5 && cellData) {
//                             let date;
//
//                             if (typeof cellData === 'object' && cellData instanceof Date) {
//                                 date = cellData;
//                             } else if (typeof cellData === 'number') {
//
//                                 const excelEpoch = new Date(1900, 0, 1);
//                                 date = new Date(excelEpoch.getTime() + (cellData - 1) * 24 * 60 * 60 * 1000);
//                             } else {
//                                 date = new Date(cellData);
//                             }
//
//                             if (date instanceof Date && !isNaN(date)) {
//                                 cellElement.textContent = formatDate(date);
//                             } else {
//                                 cellElement.textContent = "Invalid Date";
//                             }
//
//                         } else {
//                             cellElement.textContent = cellData;
//                         }
//                         newRow.appendChild(cellElement);
//                     });
//                     const statusCell = document.createElement("td");
//                     statusCell.textContent = "Đang chờ duyệt";
//                     newRow.appendChild(statusCell);
//
//                     tableBody.appendChild(newRow);
//                 });
//             };
//             reader.readAsArrayBuffer(file);
//         } else {
//             alert("Vui lòng chọn tệp Excel (.xlsx)");
//         }
//     });
//     cancelButton.addEventListener('click', function () {
//         fileInput.value = "";
//         tableBody.innerHTML = "";
//     });
//
//     function formatDate(date) {
//         const day = String(date.getDate()).padStart(2, '0');
//         const month = String(date.getMonth() + 1).padStart(2, '0');
//         const year = date.getFullYear();
//         return `${year}-${month}-${day}`;
//     }
// });


document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('excelFileInput');
    const cancelButton = document.getElementById('cancelButton');
    const tableBody = document.querySelector("#previewTable tbody");

    fileInput.addEventListener('change', function (event) {
        const file = event.target.files[0];

        if (file && file.type === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
            const reader = new FileReader();
            reader.onload = function (e) {
                const data = new Uint8Array(e.target.result);
                const workbook = XLSX.read(data, { type: 'array' });
                const sheet = workbook.Sheets[workbook.SheetNames[0]];

                // Đọc dữ liệu từ sheet
                const rows = XLSX.utils.sheet_to_json(sheet, { header: 1, defval: "", raw: true });
                tableBody.innerHTML = "";  // Clear any previous content

                rows.slice(1).forEach(row => { // Skip the first row if it's the header
                    if (row.every(cell => cell === "")) {
                        return; // Skip empty rows
                    }

                    const newRow = document.createElement("tr");

                    row.forEach((cellData, index) => {
                        const cellElement = document.createElement("td");

                        // Xử lý cột Ngày Sinh
                        if (index === 5 && cellData) {
                            let date;
                            if (typeof cellData === 'object' && cellData instanceof Date) {
                                date = cellData;
                            } else if (typeof cellData === 'number') {
                                const excelEpoch = new Date(1900, 0, 1);
                                date = new Date(excelEpoch.getTime() + (cellData - 1) * 24 * 60 * 60 * 1000);
                            } else {
                                date = new Date(cellData);
                            }

                            if (date instanceof Date && !isNaN(date)) {
                                cellElement.textContent = formatDate(date);
                            } else {
                                cellElement.textContent = "Invalid Date";
                            }
                        } else {
                            cellElement.textContent = cellData;
                        }

                        newRow.appendChild(cellElement);
                    });

                    const statusCell = document.createElement("td");
                    statusCell.textContent = "Đang chờ duyệt";
                    newRow.appendChild(statusCell);

                    tableBody.appendChild(newRow);
                });
            };
            reader.readAsArrayBuffer(file);
        } else {
            alert("Vui lòng chọn tệp Excel (.xlsx)");
        }
    });

    cancelButton.addEventListener('click', function () {
        fileInput.value = ""; // Reset file input
        tableBody.innerHTML = ""; // Clear the table body
    });

    function formatDate(date) {
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${year}-${month}-${day}`;
    }
});

