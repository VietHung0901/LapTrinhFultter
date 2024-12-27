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

                        if (index === 3 && cellData) {
                            let date;

                            if (typeof cellData === 'object' && cellData instanceof Date) {
                                date = cellData;
                            } else if (typeof cellData === 'number') {
                                // Nếu dữ liệu là số, Excel có thể đã lưu nó dưới dạng số ngày (Excel date number)
                                const excelEpoch = new Date(1900, 0, 1); // Bắt đầu từ 01-01-1900
                                date = new Date(excelEpoch.getTime() + (cellData - 1) * 24 * 60 * 60 * 1000);
                            } else {
                                // Nếu là chuỗi, thử chuyển đổi thành Date
                                date = new Date(cellData);
                            }

                            if (date instanceof Date && !isNaN(date)) {
                                cellElement.textContent = formatDate(date);  // Định dạng ngày theo dd/MM/yyyy
                            } else {
                                cellElement.textContent = "Invalid Date";  // Nếu ngày không hợp lệ, hiển thị lỗi
                            }

                        } else {
                            cellElement.textContent = cellData;  // Thêm dữ liệu vào ô
                        }
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

    function formatDate(date) {
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
        const year = date.getFullYear();
        return `${year}-${month}-${day}`;
    }
});
