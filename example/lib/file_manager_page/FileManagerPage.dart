// import 'dart:io';
// import 'package:doc_view/doc_view.dart';
// import 'package:flutter/material.dart';
// import 'package:file_manager/file_manager.dart';

// class FileManagerPage extends StatefulWidget {
//   FileManagerPage({Key? key}) : super(key: key);

//   @override
//   _FileManagerPageState createState() => _FileManagerPageState();
// }

// class _FileManagerPageState extends State<FileManagerPage> {
//   final FileManagerController controller = FileManagerController();

//   @override
//   void initState() {
//     super.initState();
//     controller.sortedBy = SortBy.date;
//   }

//   void update() {
//     setState(() {});
//   }

//   Future<void> fun(FileSystemEntity e) async {
//     if (await e.exists()) {
//       final result = await DocView.getImage(e.path, 0);
//       print(result);
//     }
//   }

//   @override
//   Widget build(BuildContext context) {
//     return WillPopScope(
//       onWillPop: () async {
//         if (await controller.isRootDirectory()) {
//           return true;
//         } else {
//           controller.goToParentDirectory();
//           return false;
//         }
//       },
//       child: Scaffold(
//           appBar: AppBar(
//             title: const Text('Plugin example app'),
//             actions: [
//               Stack(
//                 children: [
//                   Container(
//                     padding: EdgeInsets.only(right: 20),
//                     alignment: Alignment.center,
//                     child: SizedBox(
//                       height: 25,
//                       width: 25,
//                       child: CircularProgressIndicator(),
//                     ),
//                   ),
//                 ],
//               )
//             ],
//           ),
//           body: FileManager(
//             controller: controller,
//             builder: (context, entitys) {
//               return ListView.builder(
//                 itemCount: entitys.length,
//                 itemBuilder: (context, index) {
//                   return Card(
//                     child: ListTile(
//                       title: Text(FileManager.basename(entitys[index], false)),
//                       subtitle: subtitle(entitys[index]),
//                       trailing: (FileManager.isDirectory(entitys[index]) &&
//                               FileManager.basename(entitys[index].parent) ==
//                                   "test")
//                           ? IconButton(
//                               onPressed: () async {
//                                 await Directory(entitys[index].path)
//                                     .delete(recursive: true);
//                                 update();
//                               },
//                               icon: Icon(Icons.delete),
//                             )
//                           : Text(''),
//                       onTap: () async {
//                         if (FileManager.isDirectory(entitys[index])) {
//                           controller.openDirectory(entitys[index]);
//                         } else {
//                           // await fun(entitys[index]);
//                           print(entitys[index].path);
//                           // update();
//                         }
//                       },
//                     ),
//                   );
//                 },
//               );
//             },
//           )),
//     );
//   }

//   Widget subtitle(FileSystemEntity entity) {
//     return FutureBuilder<FileStat>(
//       future: entity.stat(),
//       builder: (context, snapshot) {
//         if (snapshot.hasData) {
//           if (entity is File) {
//             int size = snapshot.data!.size;

//             return Text(
//               "${FileManager.formatBytes(size)}",
//             );
//           }
//           return Text(
//             "",
//           );
//         } else {
//           return Text("");
//         }
//       },
//     );
//   }
// }
