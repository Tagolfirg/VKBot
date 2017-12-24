package ru.brainrtp.vk.bot.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class ConfigurationProvider {

   private static final Map providers = new HashMap();


   public static ConfigurationProvider getProvider(Class provider) {
      return (ConfigurationProvider)providers.get(provider);
   }

   public abstract void save(Configuration var1, File var2) throws IOException;

   public abstract void save(Configuration var1, Writer var2);

   public abstract Configuration load(File var1) throws IOException;

   public abstract Configuration load(File var1, Configuration var2) throws IOException;

   public abstract Configuration load(Reader var1);

   public abstract Configuration load(Reader var1, Configuration var2);

   public abstract Configuration load(InputStream var1);

   public abstract Configuration load(InputStream var1, Configuration var2);

   public abstract Configuration load(String var1);

   public abstract Configuration load(String var1, Configuration var2);

   static {
      providers.put(YamlConfiguration.class, new YamlConfiguration());
   }
}
