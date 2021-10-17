import 'dart:async';
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
