import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:io';
import 'package:google_mlkit_text_recognition/google_mlkit_text_recognition.dart';
import 'package:image_cropper/image_cropper.dart';

class CameraApp extends StatelessWidget {
  const CameraApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Camera App',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: const CameraScreen(),
    );
  }
}

class CameraScreen extends StatefulWidget {
  const CameraScreen({super.key});

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
          toolbarTitle: 'Crop Image',
          toolbarColor: Colors.blue,
          toolbarWidgetColor: Colors.white,
          initAspectRatio: CropAspectRatioPreset.original,
          lockAspectRatio: false,
        ),
        IOSUiSettings(
          title: 'Crop Image',
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

  // Hàm scan chữ từ ảnh
  Future<void> _scanTextFromImage() async {
    if (_image == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('No image available for scanning')),
      );
      return;
    }

    final InputImage inputImage = InputImage.fromFile(_image!);
    final TextRecognizer textRecognizer = TextRecognizer();

    try {
      final RecognizedText recognizedText =
          await textRecognizer.processImage(inputImage);

      // Hiển thị kết quả
      if (recognizedText.text.isNotEmpty) {
        showDialog(
          // ignore: use_build_context_synchronously
          context: context,
          builder: (_) => AlertDialog(
            title: const Text('Scanned Text'),
            content: Text(recognizedText.text),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(),
                child: const Text('OK'),
              ),
            ],
          ),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('No text found in the image')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    } finally {
      textRecognizer.close();
    }
  }

  // Hàm scan table
  Future<void> _scanTextForScoreTable() async {
    if (_image == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('No image available for scanning')),
      );
      return;
    }

    final InputImage inputImage = InputImage.fromFile(_image!);
    final TextRecognizer textRecognizer = TextRecognizer();

    try {
      final RecognizedText recognizedText =
          await textRecognizer.processImage(inputImage);

      // Xử lý văn bản OCR
      String scannedText = recognizedText.text;

      // Sau khi nhận diện văn bản, in ra kết quả
      print('OCR Result:');
      print(scannedText);

      // Tách dòng
      List<String> lines = scannedText.split('\n');

      // Biến lưu dữ liệu bảng điểm
      List<Map<String, dynamic>> scoreTable = [];

      for (String line in lines) {
        // Giả sử mỗi dòng chứa dữ liệu: "Họ tên - Mã số - Điểm 1 - Điểm 2"
        List<String> parts =
            line.split(RegExp(r'\s{2,}')); // Tách bằng khoảng trắng hoặc tab

        if (parts.length >= 4) {
          scoreTable.add({
            'name': parts[0], // Họ tên
            'studentId': parts[1], // Mã số sinh viên
            'score1': double.tryParse(parts[2]) ?? 0.0, // Điểm môn 1
            'score2': double.tryParse(parts[3]) ?? 0.0, // Điểm môn 2
          });
        }
      }

      // Hiển thị kết quả
      showDialog(
        context: context,
        builder: (_) => AlertDialog(
          title: const Text('Scanned Score Table'),
          content: Text(scoreTable.map((e) => e.toString()).join('\n')),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text('OK'),
            ),
          ],
        ),
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    } finally {
      textRecognizer.close();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Camera App'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            _image == null
                ? const Text('No image selected.')
                : GestureDetector(
                    onTap: _editImage, // Chỉnh sửa ảnh khi nhấn vào ảnh
                    child: Image.file(
                      _image!,
                      width: 300,
                      height: 300,
                      fit: BoxFit.cover,
                    ),
                  ),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: _openCamera,
              child: const Text('Mở Camera'),
            ),
            ElevatedButton(
              onPressed: _openlib,
              child: const Text('Mở thư viện'),
            ),
            ElevatedButton(
              onPressed: _scanTextFromImage,
              child: const Text('Scan Text'),
            ),
            ElevatedButton(
              onPressed: _scanTextForScoreTable,
              child: const Text('Scan Score Table'),
            ),
          ],
        ),
      ),
    );
  }
}