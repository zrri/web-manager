package both.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import both.common.util.LoggerUtil;
import both.common.util.TypeConverter;

/**
 * 配置文件preference
 *
 * @author 江成
 */
public class ConfigPreference {

    /**
     * 互斥锁
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 编码格式
     */
    private String encoding = "UTF-8";
    
    /**
     * 最后访问时间戳
     */
    private long lastModified;

    /**
     * 配置属性集合
     */
    private Map<String, Map<String, Object>> configProperties = new LinkedHashMap<String, Map<String, Object>>();

    /**
     * 配置文件
     */
    private String configFile;

    /**
     * 构造函数
     */
    public ConfigPreference(String path) {
        // 获取配置文件列表
        this.configFile = path;
        // 加载配置
        this.load();
    }

    /**
     * 加载配置
     */
    private void load() {
    	File file = new File(this.configFile);
    	long time=file.lastModified();
        if(lastModified==time){
        	return;
        }
        // 加载配置文件
        this.loadConfigFile(file, this.configProperties);
        //更新last modified time
        this.lastModified=time;
    }

    /**
     * 加载配置文件
     *
     * @param file
     * @param configMap
     */
    private void loadConfigFile(File file,
                                Map<String, Map<String, Object>> configMap) {
        // 记录开始时间
        long s = System.currentTimeMillis();
        StringBuilder msgSb = new StringBuilder();
        msgSb.append("开始加载配置文件[");
        msgSb.append(file.getName());
        msgSb.append("]");
        LoggerUtil.debug(msgSb.toString());


        // 定义输入流
        InputStream in = null;
        try {
            if (!file.exists()) {
                StringBuilder sb = new StringBuilder();
                sb.append("配置文件[");
                sb.append(file.getName());
                sb.append("]不存在");
                LoggerUtil.warn(sb.toString());
                return;
            }
            in=new FileInputStream(file);
            // 获取reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, this.encoding));

            String line = null;
            while ((line = reader.readLine()) != null) {
                // 去掉空格
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                char c = line.charAt(0);
                // 已经注释，不作处理
                if (c == '#') {
                    continue;
                }

                int index = line.lastIndexOf("=");
                if (index != -1) {
                    // 获取前部分
                    String prePart = line.substring(0, index).trim();
                    // 获取后部分
                    String postPart = line.substring(index + 1).trim();

                    index = prePart.indexOf("/");
                    if (index != -1) {
                        String mark = prePart.substring(0, index).trim();
                        String key = prePart.substring(index + 1).trim();
                        String value = postPart.trim();

                        // 获取子配置MAP
                        Map<String, Object> subConfigMap = configMap.get(mark);
                        if (subConfigMap == null) {
                            subConfigMap = new LinkedHashMap<String, Object>();
                            // 加入配置MAP
                            configMap.put(mark, subConfigMap);
                        }
                        // 加入配置
                        subConfigMap.put(key, value);

                    }
                }
            }

        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("解析配置文件[");
            sb.append(file.getName());
            sb.append("]错误");
            LoggerUtil.error(sb.toString(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LoggerUtil.error(e.getMessage(), e);
                }
            }
        }

        // 记录结束时间
        long t = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("完成加载配置文件[");
        sb.append(file.getName());
        sb.append("],耗时");
        sb.append(t - s);
        sb.append("毫秒");
        LoggerUtil.debug(sb.toString());  
    }

    /**
     * Returns the value associated with the specified mark and key in this
     * node.
     *
     * @param mark
     * @param key
     * @param defaultValue
     * @return
     */
    protected Object get(String mark, String key, Object defaultValue) {
        Object value = null;
        lock.readLock().lock();
        try {
        	//加载
        	this.load();
            // 获取mark对应的properties
            Map<String, Object> subConfigPropertis = this.configProperties
                    .get(mark);
            if (subConfigPropertis != null) {
                // 获取对应的value
                value = subConfigPropertis.get(key);
            }
        } finally {
            lock.readLock().unlock();
        }

        if (value == null) {
            value = defaultValue;
        }
        return value;

    }

    /**
     * Returns the Boolean object associated with the specified mark and key in
     * this node.
     *
     * @param mark
     * @param key
     * @param defaultValue
     * @return
     */
    public Boolean getBoolean(String mark, String key, Boolean defaultValue) {
        // 获取value
        Object value = this.get(mark, key, defaultValue);
        Boolean res = null;
        try {
            // 转换类型
            res = (Boolean) TypeConverter.convert(value, Boolean.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    /**
     * Returns the double object associated with the specified mark and key in
     * this node.
     *
     * @param mark
     * @param key
     * @return
     */
    public Double getDouble(String mark, String key, Double defaultValue) {
        // 获取value
        Object value = this.get(mark, key, defaultValue);
        Double res = null;
        try {
            // 转换类型
            res = (Double) TypeConverter.convert(value, Double.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    /**
     * Returns the float object associated with the specified mark and key in
     * this node.
     *
     * @param mark
     * @param key
     * @param defaultValue
     * @return
     */
    public Float getFloat(String mark, String key, Float defaultValue) {
        // 获取value
        Object value = this.get(mark, key, defaultValue);
        Float res = null;
        try {
            // 转换类型
            res = (Float) TypeConverter.convert(value, Float.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    /**
     * Returns the Integer object associated with the specified mark and key in
     * this node.
     *
     * @param mark
     * @param key
     * @param defaultValue
     * @return
     */
    public Integer getInt(String mark, String key, Integer defaultValue) {
        // 获取value
        Object value = this.get(mark, key, defaultValue);
        Integer res = null;
        try {
            // 转换类型
            res = (Integer) TypeConverter.convert(value, Integer.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    /**
     * Returns the long object associated with the specified mark and key in
     * this node.
     *
     * @param mark
     * @param key
     * @return
     */
    public Long getLong(String mark, String key, Long defaultValue) {
        // 获取value
        Object value = this.get(mark, key, defaultValue);
        Long res = null;
        try {
            // 转换类型
            res = (Long) TypeConverter.convert(value, Long.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    /**
     * Returns the string object associated with the specified mark and key in
     * this node.
     *
     * @param mark
     * @param key
     * @return
     */
    public String getString(String mark, String key, String defaultValue) {
        // 获取value
        Object value = this.get(mark, key, defaultValue);
        String res = null;
        try {
            // 转换类型
            res = (String) TypeConverter.convert(value, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        // 重新加载数据
        this.load();
    }

}
