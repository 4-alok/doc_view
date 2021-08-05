import 'dart:async';

import 'package:flutter/services.dart';

class DocView {
  static const MethodChannel _channel = const MethodChannel('doc_view');

  static Future<int> pageCount(String path) async {
    final int _pageCount = await _channel
        .invokeMethod('pageCount', <String, dynamic>{'path': path});
    return _pageCount;
  }

  static Future<dynamic> getImage(String path, int index) async {
    print(DateTime.now());
    final _pageCount = await _channel
        .invokeMethod('getImage', <String, dynamic>{'path': path, 'index': index});
    print(DateTime.now());
    return _pageCount;
  }

  static Future<Object> genThumbnail(String data) async {
    final result = await _channel
        .invokeMethod('genThumbnail', <String, dynamic>{'path': data});
    return result;
  }
}
