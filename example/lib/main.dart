import 'dart:io';
import 'dart:ui';
import 'package:doc_view_example/file_manager_page/FileManagerPage.dart';
import 'package:file_manager/file_manager.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  final FileManagerController controller = FileManagerController();

  Future<void> genThumbnail(FileSystemEntity file) async {
    if (await file.exists()) {
      final Directory dir = Directory(file.path.split(".").first);
      print(dir.path);
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          brightness: Brightness.dark,
        ),
        home: FileManagerPage());
  }
}
