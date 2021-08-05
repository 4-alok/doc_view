#import "DocViewPlugin.h"
#if __has_include(<doc_view/doc_view-Swift.h>)
#import <doc_view/doc_view-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "doc_view-Swift.h"
#endif

@implementation DocViewPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftDocViewPlugin registerWithRegistrar:registrar];
}
@end
