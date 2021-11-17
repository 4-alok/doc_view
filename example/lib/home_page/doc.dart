import 'package:doc_view/doc_view.dart';
import 'package:flutter/material.dart';

class Doc extends StatefulWidget {
  final String path;
  Doc({
    required this.path,
    Key? key,
  }) : super(key: key);

  @override
  _DocState createState() => _DocState();
}

class _DocState extends State<Doc> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("")),
      body: DocViewer(path: widget.path,),
    );
  }
}
