package com.lmiky.platform.freemarker.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lmiky.platform.logger.util.LoggerUtils;
import com.lmiky.platform.util.DateUtils;
import com.lmiky.platform.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * 实体类
 * @author lmiky
 * @date 2015年5月6日 下午9:35:16
 */
public class PojoBuilder extends BaseBuilder {
	//模板文件名
	public static final String TEMPLATE_FILE_NAME = "pojo.ftl";
	private static String dbHost = "localhost";
	private static int dbPort = 3306;
	private static String dbName = "gameopedb";
	private static String dbUserName = "root";
	private static String dbPassword = "root";
	private static String tableName;
	
	/**
	 * 构造
	 * @author lmiky
	 * @date 2015年5月6日 下午9:35:28
	 * @param clazz
	 * @return
	 */
	public static boolean builder(String className, String pojoDesc, String tableName) {
		PojoBuilder.tableName = tableName;
		try {
			Configuration configuration = new Configuration();
			configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
			configuration.setObjectWrapper(new DefaultObjectWrapper());
			configuration.setDefaultEncoding("UTF-8"); // 这个一定要设置，不然在生成的页面中 会乱码
			// 获取或创建一个模版。
			Template template = configuration.getTemplate(TEMPLATE_FILE_NAME);
			String outputDirectoryPath = TEMPLATE_OUTPUT_PATH + className.substring(0, className.lastIndexOf(".")).replace(".", "\\\\");
			File outputDirectory = new File(outputDirectoryPath);
			if(!outputDirectory.exists()) {
				if(!outputDirectory.mkdirs()) {
					return false;
				}
			}
			//参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String classSimpleName = className.substring(className.lastIndexOf(".") + 1);
			String pack = className.substring(0, className.lastIndexOf("."));
			paramMap.put("className", classSimpleName);
			paramMap.put("tableName", tableName);
			paramMap.put("package", pack);
			paramMap.put("pojoDesc", pojoDesc);
			paramMap.put("createData", DateUtils.formatDateTime(new Date()));
			Writer writer  = new OutputStreamWriter(new FileOutputStream(outputDirectoryPath + "\\\\" + classSimpleName + ".java"),"UTF-8");  
			
			//类属性列表
			paramMap.put("fields", getTableInfo());
            template.process(paramMap, writer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logException(e);
			return false;
		}
	}

	/**
	 * 获取表信息
	 * @author lmiky
	 * @date 2015年5月6日 下午9:56:33
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<Map<String, String>> getTableInfo() throws ClassNotFoundException, SQLException {
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		Class.forName("com.mysql.jdbc.Driver");
		String dbUrl = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&amp;characterEncoding=utf-8", dbHost, dbPort, dbName);
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			statement = conn.createStatement();
			resultSet = statement.executeQuery("desc " + tableName);
			while(resultSet.next()) {
				String fieldName = resultSet.getString(1);
				if("id".equals(fieldName)) {
					continue;
				}
				String fieldType = resultSet.getString(2);
				if(fieldType.startsWith("bigint")) {
					fieldType = "Long";
				} else if(fieldType.startsWith("int")) {
					fieldType = "Integer";
				} else if(fieldType.startsWith("varchar")) {
					fieldType = "String";
				} else if(fieldType.startsWith("date") || fieldType.startsWith("time")) {
					fieldType = "Date";
				} else if(fieldType.startsWith("char")) {
					fieldType = "String";
				} else {
					fieldType = "String";
				}
				Map<String, String> field = new HashMap<String, String>();
				field.put("type", fieldType);
				field.put("name", fieldName);
				field.put("firstUpercaseNme", StringUtils.firstLetterUpperCase(fieldName));
				fields.add(field);
			}
			resultSet.close();
		} finally {
			if(resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
			if(statement != null && !statement.isClosed()) {
				statement.close();
			}
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		return fields;
	}
	
	public static void main(String[] args) {
		PojoBuilder.builder("com.markany.gameboss.player.pojo.PlayerOnlineList", "玩家在线清单", "t_player_online_list");
	}
}