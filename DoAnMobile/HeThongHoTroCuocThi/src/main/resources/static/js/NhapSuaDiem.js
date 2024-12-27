// Lắng nghe sự kiện click trên nút "Xác nhận"
document.getElementById('confirmInfoButton').addEventListener('click', function() {
    // Lấy giá trị CCCD từ trường input
    var cccd = document.getElementById('cccd').value;

    // Kiểm tra xem trường CCCD có được điền hay không
    if (cccd.trim() === '') {
        // Nếu trường CCCD trống, hiển thị thông báo
        alert("Vui lòng nhập CCCD.");
        return;
    }
    var formNhapDiem = document.getElementsByClassName("form-NhapDiem")[0];
    var imageElement = document.getElementById('imageUrl');
    // Gọi API để lấy thông tin người dùng
    fetch('/User/id/' + cccd)
        .then(response => response.json())
        .then(data => {
            // Xử lý dữ liệu trả về
            console.log(data);
            // Ví dụ: hiển thị thông tin người dùng
            document.getElementById('userId').value = data.id;
            document.getElementById('hoten').value = data.hoten;
            document.getElementById('sdt').value = data.phone;
            document.getElementById('email').value = data.email;
            document.getElementById('truongId').value = data.truongId;
            document.getElementById('truongName').value = data.truongName;

            var genderElement = document.getElementById('gender');
            genderElement.textContent = data.gender === '0' ? 'Nam' : 'Nữ';

            // Cập nhật ảnh
            imageElement.src = data.imageUrl;
            imageElement.style.display = 'block'; // Hiển thị ảnh

            var inputUser = document.getElementById("userId");
            var UserId = inputUser.value;

            var inputPDK = document.getElementById("phieuDangKyUserId");
            var pdkUserId = inputPDK.value;

            if(UserId === pdkUserId)
            {
                // alert('Thông tin thí sinh trùng khớp với phiếu đăng ký, Mời nhập/sửa điểm.');
                formNhapDiem.style.display = "block";
            }
            else
            {
                alert('Thông tin thí sinh không trùng khớp với phiếu đăng ký, Vui lòng xác nhận lại!');
                formNhapDiem.style.display = "none";
            }
        })
        .catch(error => {
            // Xử lý lỗi
            alert('Không tìm thấy thông tin cần xác nhận');
            document.getElementById('userId').value = '';
            document.getElementById('hoten').value = '';
            document.getElementById('sdt').value = '';
            document.getElementById('email').value = '';
            document.getElementById('truongId').value = '';
            document.getElementById('truongName').value = '';
            imageElement.style.display = 'none';
            formNhapDiem.style.display = "none";
        });
});