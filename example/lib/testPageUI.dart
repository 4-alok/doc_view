import 'dart:io';

import 'package:doc_view/doc_view.dart';
import 'package:flutter/material.dart';

class TestPage extends StatefulWidget {
  TestPage({Key? key}) : super(key: key);

  @override
  _TestPageState createState() => _TestPageState();
}

class _TestPageState extends State<TestPage> {
  Future<void> task(FileSystemEntity e) async {
    if (await e.exists()) {
      final text = await DocView.fetchText(e.path, 2);
      // print(text);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Test Page"),
        actions: [
          TextButton(
              onPressed: () async {
                await DocView.getImage("/storage/emulated/0/test/TaGR.pdf", 1);
              },
              child: Text("Gen Image"))
        ],
      ),
      body: Container(
        child: Column(
          children: [
            Card(
              child: ListTile(
                title: Text("pdf"),
                onTap: () async {
                  final File file = File("/storage/emulated/0/test/TaGR.pdf");
                  await task(file);
                },
              ),
            ),
            Card(
              child: ListTile(
                title: Text("doc"),
                onTap: () async {
                  final File file =
                      File("/storage/emulated/0/test/YOGA_ASSIGNMENT.docx");
                  await task(file);
                },
              ),
            ),
            Card(
              child: ListTile(
                title: Text("ppt"),
                onTap: () {},
              ),
            ),
            Card(
              child: ListTile(
                title: Text("photo"),
                onTap: () async {
                  try {
                    File file = File("/storage/emulated/0/test/TaGR.pdf.png");
                    await file.delete();
                  } catch (e) {
                    print(e);
                  }
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
