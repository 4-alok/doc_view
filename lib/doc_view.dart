import 'dart:async';
import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class DocView {
  static const MethodChannel _channel = const MethodChannel('doc_view');
  static const String _pageCount = "pageCount";
  static const String _getImage = "getImage";
  static const String _genThumbnail = "genThumbs";
  static const String _fetchText = "fetchText";
  
  // count pages from the pdf 
 static Future<int> pageCount(String path) async {
    final int _pageCount = await _channel
        .invokeMethod(DocView._pageCount, <String, dynamic>{'path': path});
    return _pageCount;
  }

  // generate image of a selleced page from the PDF
  static Future<dynamic> getImage(String path, int index) async {
    final _pageCount = await _channel.invokeMethod(
        DocView._getImage, <String, dynamic>{'path': path, 'index': index});
    return _pageCount;
  }
  
  // generate thumbnail of the PDF 
  static Future<Object> genThumbnail(String path) async {
    final result = await _channel
        .invokeMethod(DocView._genThumbnail, <String, dynamic>{'path': path});
    return result;
  }

  // fetch text from the selected page(INDEX) of the PDF 
  static Future<String> fetchText(String path, int index) async {
    final String result = await _channel.invokeMethod(
        DocView._fetchText, <String, dynamic>{'path': path, 'index': index});
    return result;
  }
}

class DocViewer extends StatefulWidget {
  final String path;
  DocViewer({
    required this.path,
    Key? key,
  }) : super(key: key);

  @override
  _DocViewerState createState() => _DocViewerState();
}

class _DocViewerState extends State<DocViewer> {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<int>(
        future: DocView.pageCount(widget.path),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return PageView.builder(
              itemCount: snapshot.data,
              itemBuilder: (context, index) {
                return FutureBuilder<dynamic>(
                  future: DocView.getImage(widget.path, index),
                  builder: (context, imgSnapshot) {
                    if (imgSnapshot.hasData) {
                      final Uint8List img = imgSnapshot.data;
                      return Image.memory(img);
                    } else if (snapshot.hasError) {
                      return Container(
                        child: Center(child: Text(snapshot.error.toString())),
                      );
                    } else {
                      return Center(child: CircularProgressIndicator());
                    }
                  },
                );
              },
            );
          } else {
            return Container(
              child: Center(child: CircularProgressIndicator()),
            );
          }
        });
  }
}
