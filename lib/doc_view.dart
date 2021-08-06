import 'dart:async';
import 'package:flutter/services.dart';

class DocView {
  static const MethodChannel _channel = const MethodChannel('doc_view');
  static const String _pageCount = "pageCount";
  static const String _getImage = "getImage";
  static const String _genThumbnail = "genThumbs";
  static const String _fetchText = "fetchText";

  static Future<int> pageCount(String path) async {
    final int _pageCount = await _channel
        .invokeMethod(DocView._pageCount, <String, dynamic>{'path': path});
    return _pageCount;
  }

  static Future<dynamic> getImage(String path, int index) async {
    final _pageCount = await _channel.invokeMethod(
        DocView._getImage, <String, dynamic>{'path': path, 'index': index});
    return _pageCount;
  }

  static Future<Object> genThumbnail(String path) async {
    final result = await _channel
        .invokeMethod(DocView._genThumbnail, <String, dynamic>{'path': path});
    return result;
  }

  static Future<String> fetchText(String path, int index) async {
    final String result = await _channel.invokeMethod(
        DocView._fetchText, <String, dynamic>{'path': path, 'index': index});
    return result;
  }
}
