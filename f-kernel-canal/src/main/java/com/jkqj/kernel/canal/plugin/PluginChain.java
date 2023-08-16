package com.jkqj.kernel.canal.plugin;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

public class PluginChain {

   private Plugin sentinel;

   public PluginChain(List<Plugin> plugins) {
      buildChain(plugins);
   }

   private void buildChain(List<Plugin> plugins) {
      checkState(CollectionUtils.isNotEmpty(plugins));

      Collections.sort(plugins);

      Plugin current = newSentinel();
      sentinel = current;

      for (Plugin plugin : plugins) {
         current.setNext(plugin);

         current = current.getNext();
      }
   }

   public void handle(Context context) {
      sentinel.getNext().process(context);
   }

   private Plugin newSentinel() {
      return new AbstractPlugin() {
         @Override
         public PluginType getType() {
            return null;
         }

         @Override
         public void doProcess(Context context) {

         }
      };
   }

}
