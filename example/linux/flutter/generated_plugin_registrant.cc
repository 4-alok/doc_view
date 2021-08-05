//
//  Generated file. Do not edit.
//

#include "generated_plugin_registrant.h"

#include <doc_view/doc_view_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) doc_view_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "DocViewPlugin");
  doc_view_plugin_register_with_registrar(doc_view_registrar);
}
