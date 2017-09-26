package com.elong.mt.llz.data.crawler.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 配置工具类 loadMode: true从jar包外jar所在的当前路径下导入配置，默认 false从jar包内导入配置 可通过setLoadMode设置
 * confCache:预加载/conf/custom/env/下的所有的配置
 * <p/>
 * 通过key取value，从confCache取 getEnv*: loadMode决定从何处读取配置 getNotEnv*: 从jar包内读取配置
 */
public class PropertiesUtil {
    private final static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static String rootPath = null;
    private static String jarPath = null;
    private static Map<String, String> confCache = new HashMap<>();
    private static boolean loadMode = true;

    static {
        rootPath = getCurrentPath();
        jarPath = getJarPath();
    }

    /**
     * 设置导入配置模式
     *
     * @param mode true从jar包外导入 false从jar包里导入
     */
    public static void setLoadMode(boolean mode) {
        loadMode = mode;
    }

    public static String getRootPath() {
        return rootPath;
    }

    /**
     * 指定路径导入配置文件
     */
    private static void loadPath() {
        confCache.clear();
        String envPath = rootPath + "/conf/custom/notenv/";
        InputStream inputStream = null;
        try {
            /**
             * 从参数里的本地文件系统里读取
             */
            logger.info("load config rootPath:" + envPath);
            File file = new File(envPath);
            if (!file.isDirectory()) {
                throw new Exception(envPath + " is not Directory");
            }

            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File arg0, String arg1) {
                    // TODO Auto-generated method stub
                    return arg1.endsWith(".properties");
                }

            };
            File[] fileList = file.listFiles(filter);
            if (fileList == null) {
                logger.error("env properties is empty.");
                return;
            }
            logger.info("load config begin");
            Properties properties = new Properties();
            for (File propFile : fileList) {
                if (propFile.isFile() && propFile.canRead()) {
                    logger.info("load config from " + propFile.getAbsolutePath());
                    inputStream = new FileInputStream(propFile);
                    properties.clear();
                    properties.load(inputStream);
                    Enumeration<?> pros = properties.propertyNames();
                    while (pros != null && pros.hasMoreElements()) {
                        String name = (String) pros.nextElement();
                        confCache.put(name, properties.getProperty(name));
                        logger.info("load config key:" + name + ",value:" + properties.getProperty(name) + " from " + propFile.getName());
                    }
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            logger.error("env properties load from file system fail, " + envPath + ", " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            logger.info("load config end");
        }

    }

    /**
     * 从jar包内导入
     */
    private static void loadJar() {
        confCache.clear();
        JarFile jarFile = null;
        try {
            logger.info("load config jar:" + jarPath);
            Properties properties = new Properties();
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> jarEntryList = jarFile.entries();
            logger.info("load config begin");
            while (jarEntryList.hasMoreElements()) {
                JarEntry jarEntry = jarEntryList.nextElement();
                if (jarEntry.getName().contains("conf/custom/env") && jarEntry.getName().endsWith(".properties")) {
                    logger.info("load config from " + jarEntry.getName());
                    InputStream inputStream = jarFile.getInputStream(jarEntry);
                    properties.clear();
                    properties.load(inputStream);
                    Enumeration<?> pros = properties.propertyNames();
                    while (pros != null && pros.hasMoreElements()) {
                        String name = (String) pros.nextElement();
                        confCache.put(name, properties.getProperty(name));
                        logger.info("load config key:" + name + ",value:" + properties.getProperty(name) + " from " + jarFile.getName());
                    }
                    inputStream.close();
                }

            }
        } catch (Exception e) {
            logger.error("env properties load from jar fail, " + jarPath + ", " + e.getMessage());
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException ignored) {
                }
            }
            logger.info("load config end");
        }

    }

    /**
     * 获取/conf/custom/env/下的配置项
     *
     * @param key
     * @return
     */
    public static int getIntValue(String key) {
        load();
        return Integer.parseInt(confCache.get(key));
    }

    /**
     * 获取/conf/custom/env/下的配置项
     *
     * @param key
     * @return
     */
    public static long getLongValue(String key) {
        load();
        return Long.parseLong(confCache.get(key));
    }

    /**
     * 获取/conf/custom/env/下的配置项
     *
     * @param key
     * @return
     */
    public static boolean getBooleanValue(String key) {
        load();
        return Boolean.parseBoolean(confCache.get(key));
    }

    /**
     * 获取/conf/custom/env/下的配置项
     *
     * @param key
     * @return
     */
    public static String getStringValue(String key) {
        load();
        return confCache.get(key);
    }

    /**
     * 测试/conf/custom/env/下的配置项是否包含key
     *
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        load();
        return confCache.containsKey(key);
    }

    /**
     * 获取/conf/custom/env/下的配置项
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntValue(String key, int defaultValue) {
        load();
        if (containsKey(key)) {
            return Integer.parseInt(confCache.get(key));
        } else {
            logger.warn("key:" + key + "not find, return defaultValue:" + defaultValue);
            return defaultValue;
        }

    }

    /**
     * 获取/conf/custom/env/下的配置项
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringValue(String key, String defaultValue) {
        load();
        if (containsKey(key)) {
            return confCache.get(key);
        } else {
            logger.warn("key:" + key + "not find, return defaultValue:" + defaultValue);
            return defaultValue;
        }

    }

    /**
     * 导入配置
     */
    private synchronized static void load() {
        if (confCache.isEmpty()) {
            if (loadMode) {
                loadPath();
            } else {
                loadJar();
            }
            logger.info("config all------------------------------");
            for (String key : confCache.keySet()) {
                logger.info(key + "=" + confCache.get(key));
            }
            logger.info("config all------------------------------");
        }
    }

    /**
     * 当前jar包所在的路径
     *
     * @return
     */
    private static String getCurrentPath() {
        String currentPath = PropertiesUtil.class.getResource("/conf").getFile();
        currentPath = currentPath.substring(currentPath.indexOf('/'));
        if (currentPath.contains(".jar")) {
            // 运行jar包时
            currentPath = currentPath.substring(0, currentPath.indexOf(".jar"));
            currentPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
        } else {
            // 本地调试时
            currentPath = currentPath.substring(0, currentPath.indexOf("/conf"));
        }

        if (currentPath.contains("/test-classes")) {
            currentPath = currentPath.replace("/test-classes", "/classes");
        }
        return currentPath;
    }

    /**
     * 当前jar包的全路径 /home/work/hotel/goods-ds-jar-with-dependencies.jar
     *
     * @return
     */
    private static String getJarPath() {
        String currentPath = PropertiesUtil.class.getResource("/conf").getFile();
        currentPath = currentPath.substring(currentPath.indexOf('/'));
        if (currentPath.contains(".jar")) {
            // 运行jar包时
            currentPath = currentPath.substring(0, currentPath.indexOf(".jar") + 4);
        } else {
            // 本地调试时
            currentPath = currentPath.substring(0, currentPath.indexOf("/conf"));
        }
        return currentPath;
    }

    /**
     * 根据相对路径获取配置所有配置,由loadMode决定从jar包外导入还是jar包内导入
     *
     * @param relativePath 形如/conf/conf.properties
     * @return
     */
    public static Properties getEnvProperties(String relativePath) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            if (loadMode) {
                File file = new File(rootPath + resolveName(relativePath));
                inputStream = new FileInputStream(file);
                properties.load(inputStream);
            } else {
                inputStream = PropertiesUtil.class.getResourceAsStream(resolveName(relativePath));
                properties.load(inputStream);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("properties load from " + (rootPath + relativePath) + " fail, " + e.getMessage());
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }

        return properties;
    }

    /**
     * 根据相对路径获取配置所有配置，从jar包内导入
     *
     * @param relativePath 形如/conf/conf.properties
     * @return
     */
    public static Properties getNotEnvProperties(String relativePath) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = PropertiesUtil.class.getResourceAsStream(resolveName(relativePath));
            properties.load(inputStream);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("properties load from jar fail, " + e.getMessage());
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }

        return properties;
    }

    /**
     * 指定文件相对路径获取配置,由loadMode决定从jar包外导入还是jar包内导入
     *
     * @param relativePath 形如/conf/conf.properties
     * @param key
     * @return
     */
    public static Integer getEnvIntValue(String relativePath, String key) {
        Properties properties = getEnvProperties(relativePath);
        if (properties == null) {
            return null;
        } else {
            return Integer.parseInt(properties.getProperty(key));
        }
    }

    /**
     * 指定文件相对路径获取配置,由loadMode决定从jar包外导入还是jar包内导入
     *
     * @param relativePath 形如/conf/conf.properties
     * @param key
     * @return
     */
    public static String getEnvStringValue(String relativePath, String key) {
        Properties properties = getEnvProperties(relativePath);
        if (properties == null) {
            return null;
        } else {
            return properties.getProperty(key);
        }
    }

    /**
     * 指定文件相对路径获取配置，从jar包内导入
     *
     * @param relativePath 形如/conf/conf.properties
     * @param key
     * @return
     */
    public static Integer getNotEnvIntValue(String relativePath, String key) {
        Properties properties = getNotEnvProperties(relativePath);
        if (properties == null) {
            return null;
        } else {
            return Integer.parseInt(properties.getProperty(key));
        }
    }

    /**
     * 指定文件相对路径获取配置，从jar包内导入
     *
     * @param relativePath 形如/conf/conf.properties
     * @param key
     * @return
     */
    public static String getNotEnvStringValue(String relativePath, String key) {
        Properties properties = getNotEnvProperties(relativePath);
        if (properties == null) {
            return null;
        } else {
            return properties.getProperty(key);
        }
    }

    /**
     * 将路径参数转换成jar内的绝对路径
     *
     * @param relativePath
     * @return
     */
    private static String resolveName(String relativePath) {
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }
        return relativePath;
    }

    /**
     * 获取/conf/custom/env/下的配置项
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static void setStringValue(String key, String defaultValue) {
        load();
        confCache.put(key, defaultValue);
    }

}