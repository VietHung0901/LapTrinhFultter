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

                const rows = XLSX.utils.sheet_to_json(sheet, { header: 1, defval: "", raw: true });
                tableBody.innerHTML = "";  // Xóa nội dung bảng cũ

                // Bỏ qua dòng tiêu đề và duyệt qua các dòng dữ liệu
                rows.slice(1).forEach(row => {
                    const newRow = document.createElement("tr");

                    row.forEach((cellData, index) => {
                        const cellElement = document.createElement("td");
                        cellElement.textContent = cellData;
                        newRow.appendChild(cellElement);  // Thêm ô vào dòng
                    });

                    tableBody.appendChild(newRow);  // Thêm dòng mới vào bảng
                });
            };
            reader.readAsArrayBuffer(file);
        } else {
            alert("Vui lòng chọn tệp Excel (.xlsx)");
        }
    });

    // Xử lý khi nhấn nút "Hủy Tệp"
    cancelButton.addEventListener('click', function () {
        // Xóa tệp đã chọn
        fileInput.value = "";
        // Xóa bảng xem trước
        tableBody.innerHTML = "";
    });
});
