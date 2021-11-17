import 'dart:io';
import 'package:file_manager/file_manager.dart';
import 'package:flutter/material.dart';

import 'doc.dart';

class HomePage extends StatefulWidget {
  HomePage({Key? key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  late final FileManagerController controller;

  @override
  void initState() {
    controller = FileManagerController();
    controller.sortedBy = SortBy.date;
    super.initState();
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        if (await controller.isRootDirectory()) {
          return true;
        } else {
          controller.goToParentDirectory();
          return false;
        }
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text("Select Doc"),
        ),
        body: FileManager(
          controller: controller,
          builder: (context, entities) {
            return ListView.builder(
              physics: BouncingScrollPhysics(),
              itemCount: entities.length,
              itemBuilder: (context, index) {
                final FileSystemEntity e = entities[index];
                return Card(
                    child: ListTile(
                  leading: FileManager.isFile(e)
                      ? Icon(Icons.feed_outlined)
                      : Icon(Icons.folder),
                  title: Text(FileManager.basename(e, false)),
                  subtitle: subtitle(e),
                  onTap: () {
                    if (FileManager.isDirectory(e)) {
                      controller.openDirectory(e);
                    } else {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => Doc(path: e.path)));
                    }
                  },
                ));
              },
            );
          },
        ),
      ),
    );
  }

  Widget subtitle(FileSystemEntity entity) {
    return FutureBuilder<FileStat>(
      future: entity.stat(),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          if (entity is File) {
            int size = snapshot.data!.size;

            return Text(
              "${FileManager.formatBytes(size)}",
            );
          }
          return Text(
            "${snapshot.data!.modified}".substring(0, 10),
          );
        } else {
          return Text("");
        }
      },
    );
  }
}
