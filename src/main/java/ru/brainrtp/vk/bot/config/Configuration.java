package ru.brainrtp.vk.bot.config;

import java.util.*;

public final class Configuration {

   private static final char SEPARATOR = '.';
   final Map self;
   private final Configuration defaults;


   public Configuration() {
      this((Configuration)null);
   }

   Configuration(Map self, Configuration config) {
      this.self = self;
      this.defaults = config;
   }

   public Configuration(Configuration defaults) {
      this(new LinkedHashMap(), defaults);
   }

   private Configuration getSectionFor(String path) {
      int index = path.indexOf(46);
      if(index == -1) {
         return this;
      } else {
         String root = path.substring(0, index);
         Object section = this.self.get(root);
         if(section == null) {
            section = new LinkedHashMap();
            this.self.put(root, section);
         }

         return section instanceof Configuration?(Configuration)section:new Configuration((Map)section, this.defaults == null?null:this.defaults.getSectionFor(path));
      }
   }

   private String getChild(String path) {
      int index = path.indexOf(46);
      return index == -1?path:path.substring(index + 1);
   }

   public Object get(String path, Object def) {
      Configuration section = this.getSectionFor(path);
      Object val;
      if(section == this) {
         val = this.self.get(path);
      } else {
         val = section.get(this.getChild(path), def);
      }

      return val != null?val:def;
   }

   public Object get(String path) {
      return this.get(path, this.getDefault(path));
   }

   public Object getDefault(String path) {
      return this.defaults == null?null:this.defaults.get(path);
   }

   public void set(String path, Object value) {
      Configuration section = this.getSectionFor(path);
      if(section == this) {
         if(value == null) {
            this.self.remove(path);
         } else {
            this.self.put(path, value);
         }
      } else {
         section.set(this.getChild(path), value);
      }

   }

   public Configuration getSection(String path) {
      Object def = this.getDefault(path);
      return new Configuration((Map)((Map)this.get(path, def instanceof Map?def:Collections.EMPTY_MAP)), this.defaults == null?null:this.defaults.getSection(path));
   }

   public byte getByte(String path) {
      Object def = this.getDefault(path);
      return this.getByte(path, def instanceof Number?((Number)def).byteValue():0);
   }

   public byte getByte(String path, byte def) {
      Object val = this.get(path, Byte.valueOf(def));
      return val instanceof Number?((Number)val).byteValue():def;
   }

   public List getByteList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Number) {
            result.add(((Number) object).byteValue());
         }
      }

      return result;
   }

   public short getShort(String path) {
      Object def = this.getDefault(path);
      return this.getShort(path, def instanceof Number?((Number)def).shortValue():0);
   }

   public short getShort(String path, short def) {
      Object val = this.get(path, Short.valueOf(def));
      return val instanceof Number?((Number)val).shortValue():def;
   }

   public List getShortList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Number) {
            result.add(Short.valueOf(((Number)object).shortValue()));
         }
      }

      return result;
   }

   public int getInt(String path) {
      Object def = this.getDefault(path);
      return this.getInt(path, def instanceof Number?((Number)def).intValue():0);
   }

   public int getInt(String path, int def) {
      Object val = this.get(path, Integer.valueOf(def));
      return val instanceof Number?((Number)val).intValue():def;
   }

   public List getIntList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Number) {
            result.add(Integer.valueOf(((Number)object).intValue()));
         }
      }

      return result;
   }

   public long getLong(String path) {
      Object def = this.getDefault(path);
      return this.getLong(path, def instanceof Number?((Number)def).longValue():0L);
   }

   public long getLong(String path, long def) {
      Object val = this.get(path, Long.valueOf(def));
      return val instanceof Number?((Number)val).longValue():def;
   }

   public List getLongList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Number) {
            result.add(Long.valueOf(((Number)object).longValue()));
         }
      }

      return result;
   }

   public float getFloat(String path) {
      Object def = this.getDefault(path);
      return this.getFloat(path, def instanceof Number?((Number)def).floatValue():0.0F);
   }

   public float getFloat(String path, float def) {
      Object val = this.get(path, Float.valueOf(def));
      return val instanceof Number?((Number)val).floatValue():def;
   }

   public List getFloatList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Number) {
            result.add(Float.valueOf(((Number)object).floatValue()));
         }
      }

      return result;
   }

   public double getDouble(String path) {
      Object def = this.getDefault(path);
      return this.getDouble(path, def instanceof Number?((Number)def).doubleValue():0.0D);
   }

   public double getDouble(String path, double def) {
      Object val = this.get(path, Double.valueOf(def));
      return val instanceof Number?((Number)val).doubleValue():def;
   }

   public List getDoubleList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Number) {
            result.add(Double.valueOf(((Number)object).doubleValue()));
         }
      }

      return result;
   }

   public boolean getBoolean(String path) {
      Object def = this.getDefault(path);
      return this.getBoolean(path, def instanceof Boolean?((Boolean)def).booleanValue():false);
   }

   public boolean getBoolean(String path, boolean def) {
      Object val = this.get(path, Boolean.valueOf(def));
      return val instanceof Boolean?((Boolean)val).booleanValue():def;
   }

   public List getBooleanList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Boolean) {
            result.add((Boolean)object);
         }
      }

      return result;
   }

   public char getChar(String path) {
      Object def = this.getDefault(path);
      return this.getChar(path, def instanceof Character?((Character)def).charValue():'\u0000');
   }

   public char getChar(String path, char def) {
      Object val = this.get(path, Character.valueOf(def));
      return val instanceof Character?((Character)val).charValue():def;
   }

   public List getCharList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof Character) {
            result.add((Character)object);
         }
      }

      return result;
   }

   public String getString(String path) {
      Object def = this.getDefault(path);
      return this.getString(path, def instanceof String?(String)def:"");
   }

   public String getString(String path, String def) {
      Object val = this.get(path, def);
      return val instanceof String?(String)val:def;
   }

   public List getStringList(String path) {
      List list = this.getList(path);
      ArrayList result = new ArrayList();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object object = i$.next();
         if(object instanceof String) {
            result.add((String)object);
         }
      }

      return result;
   }

   public List getList(String path) {
      Object def = this.getDefault(path);
      return this.getList(path, def instanceof List?(List)def:Collections.EMPTY_LIST);
   }

   public List getList(String path, List def) {
      Object val = this.get(path, def);
      return val instanceof List?(List)val:def;
   }
}
