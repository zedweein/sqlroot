package com.awn.sqlroot;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.awn.sqlroot.exception.ConfigNotFoundException;
import com.awn.sqlroot.exception.MultiDefinedException;
import com.awn.sqlroot.exception.SqlNotFoundException;
import com.awn.sqlroot.exception.XmlNotFoundException;
import com.awn.sqlroot.monitor.MapperAdaptor;
import com.awn.sqlroot.utils.PropertyUtil;

public class SQLRoot {

    private static Logger log = Logger.getLogger(SQLRoot.class);
	
	
	private static PropertyUtil _util = new PropertyUtil("sqlroot.properties");
	private static SQLRoot _instance = null;
	private static boolean monitored = false;
	private static Map<String, String> _mapperSqls = _mappers();
	

	private SQLRoot() {

	}

	private static Map<String, String> _mappers(){
		Map<String, String> sqlMap = new HashMap<String, String>();
		String mapperFilesPath = _util.getProperty("mapper.location");
		if (mapperFilesPath == null || mapperFilesPath.replaceAll(" ", "").length() == 0) {
			throw new ConfigNotFoundException();
		}
		URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
		if (mapperFilesPath.startsWith("/")) {
			mapperFilesPath = mapperFilesPath.substring(1);
		}
		File file = new File(classPath.getFile() + mapperFilesPath);
		if (!file.exists()) {
			throw new ConfigNotFoundException();
		}
		
		if(!monitored) {
			try {
				int scanUnit = 1000;
				String scanInterval = _util.getProperty("mapper.scan.interval");
				if(scanInterval==null||scanInterval.length()==0) {
					log.warn("property [mapper.scan.interval] not configed, use default 1000(ms)");
					scanUnit = 1000;
				}else {
					try {
						scanUnit = Integer.parseInt(scanInterval);
					} catch (Exception e) {
						log.warn("property [mapper.scan.interval] not configed, use default 1000(ms)");
						scanUnit = 1000;
					}
				}
				FileAlterationObserver observer = new FileAlterationObserver(file);
				observer.addListener(new MapperAdaptor());
				FileAlterationMonitor monitor = new FileAlterationMonitor(scanUnit, observer);
				monitor.start();
				log.warn("[Mappers directory monitored] : " + file.getAbsolutePath());
				monitored = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		File[] fileMappers = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(".xml");
			}
		});

		if (fileMappers == null || fileMappers.length == 0) {
			throw new XmlNotFoundException();
		}
		for(File mapper : fileMappers) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(mapper);
				NodeList sqlList = document.getElementsByTagName("sql");
				if(sqlList.getLength()>0) {
					for(int i=0;i<sqlList.getLength();i++) {
						Element sqlElement = (Element) sqlList.item(i);
						String idValue = sqlElement.getAttribute("id").trim();
						String sqlText =  sqlElement.getTextContent().trim();
						if(sqlMap.get(idValue)!=null) {
							throw new MultiDefinedException("sql ' " +idValue + " ' in [ "+mapper.getName()+" ] has already defined");
						}
						sqlMap.put(idValue, sqlText);
					}
				}
			} catch (Exception e) {
				throw new MultiDefinedException(e.getMessage());
			}

		}
		return sqlMap;
	}

	public static SQLRoot instance() {
		if (_instance == null) {
			_instance = new SQLRoot();
		}
		return _instance;
	}

	/**
	 * 获取SQL
	 * @param sqlKey sqlId
	 * @param param 参数
	 * @return sql语句
	 */
	public String sql(Map<String, String> param) {
		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
		StackTraceElement a=(StackTraceElement)temp[2];
		String sqlKey = a.getMethodName();
		String paramInfo = "";
		String sql = _mapperSqls.get(sqlKey);
		if (sql == null) {
			throw new SqlNotFoundException();
		}
		if(!param.isEmpty()) {
			for(String key : param.keySet()) {
				paramInfo += key + " = " +param.get(key) + " , ";
				sql = sql.replace("#{"+key+"}", "'"+param.get(key)+"'");
				sql = sql.replace("${"+key+"}", param.get(key));
			}
		}
		String mapperLog = _util.getProperty("mapper.logger");
		if(!"false".equals(mapperLog)) {
			paramInfo = paramInfo.length()>1?paramInfo.substring(0,paramInfo.length()-2):"";
			log.debug("[Prepared param info] : "+paramInfo);
			log.debug("[Prepared SQL query] : "+sql);
		}
		return sql;
	}

	/**
	 * 获取SQL
	 * @param sqlKey sqlId
	 * @return sql语句
	 */
	public String sql() {
		StackTraceElement[] temp=Thread.currentThread().getStackTrace();
		StackTraceElement a=(StackTraceElement)temp[2];
		String sqlKey = a.getMethodName();
		String sql = _mapperSqls.get(sqlKey);
		if (sql == null) {
			throw new SqlNotFoundException();
		}
		String mapperLog = _util.getProperty("mapper.logger");
		if(!"false".equals(mapperLog)) {
			log.debug("[Prepared SQL query]: "+sql);
		}
		return sql;
	}
	
	/**
	 * 获取SQL  指定SQL-KEY
	 * @param sqlKey sqlId
	 * @param param 参数
	 * @return sql语句
	 */
	public String sql(String sqlKey,Map<String, String> param) {
		String paramInfo = "";
		String sql = _mapperSqls.get(sqlKey);
		if (sql == null) {
			throw new SqlNotFoundException();
		}
		if(!param.isEmpty()) {
			for(String key : param.keySet()) {
				Object value = param.get(key);
				String _vstr = "";
				if(value!=null) {
					_vstr = String.valueOf(value);
				}
				paramInfo += key + " = " +_vstr + " , ";
				sql = sql.replace("#{"+key+"}", "'"+_vstr+"'");
				sql = sql.replace("${"+key+"}", _vstr);
			}
		}
		String mapperLog = _util.getProperty("mapper.logger");
		if(!"false".equals(mapperLog)) {
			paramInfo = paramInfo.length()>1?paramInfo.substring(0,paramInfo.length()-2):"";
			log.debug("[Prepared param info] : "+paramInfo);
			log.debug("[Prepared SQL query] : "+sql);
		}
		return sql;
	}

	/**
	 * 获取SQL 指定SQL-KEY
	 * @param sqlKey sqlId
	 * @return sql语句
	 */
	public String sql(String sqlKey) {
		String sql = _mapperSqls.get(sqlKey);
		if (sql == null) {
			throw new SqlNotFoundException();
		}
		String mapperLog = _util.getProperty("mapper.logger");
		if(!"false".equals(mapperLog)) {
			log.debug("[Prepared SQL query]: "+sql);
		}
		return sql;
	}
	
	/**
	 * 获取当前SQL数
	 * @return
	 */
	public Integer size() {
		return _mapperSqls.size();
	}


	public static void mapperCache() {
		_mapperSqls = _mappers();
	}
	
}
