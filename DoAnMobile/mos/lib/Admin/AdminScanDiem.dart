import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:io';
import 'package:google_mlkit_text_recognition/google_mlkit_text_recognition.dart';
import 'package:image_cropper/image_cropper.dart';
import 'package:mos/Admin/ScorePage.dart';
import 'package:mos/Class/PhieuKetQua.dart';

class CameraScreen extends StatefulWidget {
  final int contestId;
  const CameraScreen({required this.contestId});

  @override
  _CameraScreenState createState() => _CameraScreenState();
}

class _CameraScreenState extends State<CameraScreen> {
  File? _image; // Lưu trữ ảnh chụp
  File? _imageSrc; // Lưu trữ ảnh chụp

  final ImagePicker _picker = ImagePicker();

// Hàm chỉnh sửa ảnh đã chụp
  Future<void> _editImage() async {
    if (_imageSrc != null) {
      File? editedImage = await _cropImage(_imageSrc!);
      if (editedImage != null) {
        setState(() {
          _image = editedImage; // Cập nhật ảnh đã chỉnh sửa
        });
      }
    }
  }

  // Hàm cắt ảnh sau khi chụp
  Future<File?> _cropImage(File imageFile) async {
    final croppedFile = await ImageCropper().cropImage(
      sourcePath: imageFile.path,
      aspectRatioPresets: [
        CropAspectRatioPreset.square,
        CropAspectRatioPreset.original,
        CropAspectRatioPreset.ratio16x9,
      ],
      uiSettings: [
        AndroidUiSettings(
          toolbarTitle: 'Cắt ảnh',
          toolbarColor: Colors.blue,
          toolbarWidgetColor: Colors.white,
          initAspectRatio: CropAspectRatioPreset.original,
          lockAspectRatio: false,
        ),
        IOSUiSettings(
          title: 'Cắt ảnh',
        ),
      ],
    );

    if (croppedFile != null) {
      return File(croppedFile.path);
    }
    return null;
  }

  Future<void> _openlib() async {
    final XFile? photo = await _picker.pickImage(source: ImageSource.gallery);

    if (photo != null) {
      File? croppedImage = await _cropImage(File(photo.path));
      _imageSrc = File(photo.path); // Lưu đường dẫn ảnh nguồn để sửa

      if (croppedImage != null) {
        setState(() {
          _image = croppedImage; // Sử dụng ảnh đã cắt
        });
      }
    }
  }

  Future<void> _openCamera() async {
    final XFile? photo = await _picker.pickImage(source: ImageSource.camera);

    if (photo != null) {
      File? croppedImage = await _cropImage(File(photo.path));
      _imageSrc = File(photo.path); // Lưu đường dẫn ảnh nguồn để sửa

      if (croppedImage != null) {
        setState(() {
          _image = croppedImage; // Sử dụng ảnh đã cắt
        });
      }
    }
  }

  // Hàm scan bảng điểm
  Future<void> _scanTextForScoreTable() async {
    if (_image == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Không có hình ảnh để quét.')),
      );
      return;
    }

    final InputImage inputImage = InputImage.fromFile(_image!);
    final TextRecognizer textRecognizer = TextRecognizer();

    try {
      // OCR quét văn bản
      final RecognizedText recognizedText =
          await textRecognizer.processImage(inputImage);
      final String scannedText = recognizedText.text;
      print(scannedText); // Kiểm tra nội dung văn bản quét

      // Tách và xử lý văn bản
      final List<Map<String, dynamic>> scoreTable =
          _processScannedText(scannedText);

      if (scoreTable.isNotEmpty) {
        // Chuyển sang trang hiển thị bảng điểm
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) =>
                ScorePage(scoreTable: scoreTable, contestId: widget.contestId),
          ),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text(
                  'Không tìm thấy dữ liệu hợp lệ trong văn bản được quét.')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Lỗi trong quá trình quét: $e')),
      );
    } finally {
      textRecognizer.close();
    }
  }
  
  List<Map<String, dynamic>> _processScannedText(String scannedText) {
    List<Map<String, dynamic>> scoreTable = [];
    final List<String> lines = scannedText.split('\n');

    int soCot = 6;
    int n = (lines.length / soCot).toInt(); // số dòng (số thí sinh)
    if (n<1)
      return scoreTable;
    List<PhieuKetQua> list = List.generate(n, (index) => PhieuKetQua());

    for (int i = 0; i < lines.length; i = i + n) {
      // Mỗi lần sẽ nhảy n dòng
      for (int j = 0; j < n; j++) {
        // Lưu từng dòng
        if (i < n) {
          list[j].maPhieu = lines[i + j].toString();
        } else if (i < n * 2) {
          list[j].cccd = lines[i + j].toString();
        } else if (i < n * 3) {
          list[j].hoTen = lines[i + j].toString();
        } else if (i < n * 4) {
          list[j].phut =
              int.tryParse(lines[i + j].toString()) ?? -1; // -1 sẽ là int
          // Nếu không thể chuyển đổi, gán giá trị mặc định là 0.0
        } else if (i < n * 5) {
          list[j].giay = int.tryParse(lines[i + j].toString()) ??
              -2; // Nếu không thể chuyển đổi, gán giá trị mặc định là 0.0
        } else if (i < n * 6) {
          list[j].diem = int.tryParse(lines[i + j].toString()) ??
              -3; // Nếu không thể chuyển đổi, gán giá trị mặc định là 0.0
        }
      }
    }

    // Tạo bảng điểm với giá trị mặc định cho các trường hợp thiếu dữ liệu
    for (int j = 0; j < n; j++) {
      scoreTable.add({
        'maPhieu':
            list[j].maPhieu ?? 'Chưa có mã phiếu', // Gán giá trị mặc định
        'cccd': list[j].cccd ?? 'Chưa có CCCD', // Gán giá trị mặc định
        'hoTen': list[j].hoTen ?? 'Chưa có họ tên', // Gán giá trị mặc định
        'phut': list[j].phut != -1
            ? list[j].phut
            : 0, // Gán giá trị mặc định nếu không hợp lệ
        'giay': list[j].giay != -2
            ? list[j].giay
            : 0, // Gán giá trị mặc định nếu không hợp lệ
        'diem': list[j].diem != -3
            ? list[j].diem
            : 0, // Gán giá trị mặc định nếu không hợp lệ
        'trangThai': 0, // Trạng thái mặc định
      });
    }
    return scoreTable;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Nhập điểm'),
        centerTitle: true,
        backgroundColor: Colors.blueAccent, // Thêm màu nền cho app bar
      ),
      body: SingleChildScrollView(
        // Đảm bảo có thể cuộn khi màn hình nhỏ
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Hiển thị ảnh hoặc thông báo
            _image == null
                ? Container(
                    height: 250,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(12),
                      color: Colors.grey[200],
                    ),
                    child: const Center(
                      child: Text('No image selected.',
                          style: TextStyle(fontSize: 18, color: Colors.grey)),
                    ),
                  )
                : Card(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    elevation: 4,
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(12),
                      child: GestureDetector(
                        onTap: () {
                          // Đặt hàm để chỉnh sửa ảnh
                          _editImage();
                        },
                        child: Image.file(
                          _image!,
                          width: double.infinity,
                          height: 250,
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                  ),
            const SizedBox(height: 16),

            // Các nút chức năng (Mở Camera, Thư viện)
            Divider(color: Colors.grey[400], thickness: 1.0),
            const SizedBox(height: 16),
            Wrap(
              // Sử dụng Wrap để các nút không bị tràn khi nhỏ màn hình
              spacing: 16,
              alignment: WrapAlignment.center,
              children: [
                ElevatedButton.icon(
                  onPressed: _openCamera,
                  icon: const Icon(Icons.camera_alt),
                  label: const Text('Mở Camera'),
                  style: ElevatedButton.styleFrom(
                    minimumSize: Size(150, 50),
                    backgroundColor: Colors.blueAccent,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12)),
                  ),
                ),
                ElevatedButton.icon(
                  onPressed: _openlib,
                  icon: const Icon(Icons.photo_library),
                  label: const Text('Mở Thư viện'),
                  style: ElevatedButton.styleFrom(
                    minimumSize: Size(150, 50),
                    backgroundColor: Colors.blueAccent,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12)),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 16),
            Divider(color: Colors.grey[400], thickness: 1.0),
            const SizedBox(height: 16),

            // Các nút chức năng (Scan Text, Scan Score Table)
            Wrap(
              spacing: 16,
              alignment: WrapAlignment.center,
              children: [
                ElevatedButton.icon(
                  onPressed: _scanTextForScoreTable,
                  icon: const Icon(Icons.table_chart),
                  label: const Text('Scan Score Table'),
                  style: ElevatedButton.styleFrom(
                    minimumSize: Size(150, 50),
                    backgroundColor: Colors.blueAccent,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12)),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
