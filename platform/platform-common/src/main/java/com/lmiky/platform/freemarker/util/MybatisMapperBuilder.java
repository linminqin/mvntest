package com.lmiky.platform.freemarker.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.lmiky.platform.logger.util.LoggerUtils;
import com.lmiky.platform.util.PropertyUtils;
import com.lmiky.platform.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * mybatis 映射文件
 * @author lmiky
 * @date 2014年9月14日 下午4:28:35
 */
public class MybatisMapperBuilder extends BaseBuilder {
	//模板文件名
	public static final String TEMPLATE_FILE_NAME = "mapper.ftl";

	/**
	 * 构造
	 * @author lmiky
	 * @date 2014年9月14日 下午6:54:37
	 * @param clazz
	 * @return
	 */
	public static boolean builder(Class<?> clazz) {
		try {
			Configuration configuration = new Configuration();
			configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
			configuration.setObjectWrapper(new DefaultObjectWrapper());
			configuration.setDefaultEncoding("UTF-8"); // 这个一定要设置，不然在生成的页面中 会乱码
			// 获取或创建一个模版。
			Template template = configuration.getTemplate(TEMPLATE_FILE_NAME);
			String className = clazz.getName();
			String outputDirectoryPath = TEMPLATE_OUTPUT_PATH + className.substring(0, className.lastIndexOf(".")).replace(".", "\\\\");
			File outputDirectory = new File(outputDirectoryPath);
			if(!outputDirectory.exists()) {
				if(!outputDirectory.mkdirs()) {
					return false;
				}
			}
			
			//参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			//继承
			Class<?> superClass = clazz.getSuperclass();
			String extendsName = superClass.getName() + "." + StringUtils.firstLetterLowerCase(superClass.getSimpleName());
			paramMap.put("extendsName", extendsName);	
			paramMap.put("namespace", className);
			String classSimpleName = clazz.getSimpleName();
			paramMap.put("className", classSimpleName);
			//String firstLetter = classSimpleName.charAt(0) + "";
			//首字母改为小写
			String classAlias = StringUtils.firstLetterLowerCase(classSimpleName);
			paramMap.put("resultMapId", classAlias);
			String tableName = "t_";
			for(char c : classAlias.toCharArray()) {
				if(c>='A'  &&  c<='Z') {  
					tableName = tableName + "_" + (c + "").toLowerCase();
		        } else {
		        	tableName += c;
		        }
			}
			paramMap.put("tableName", tableName);
			Writer writer  = new OutputStreamWriter(new FileOutputStream(outputDirectoryPath + "\\\\" + classSimpleName + ".xml"),"UTF-8");  
			
			//类属性列表
			paramMap.put("localFields", PropertyUtils.getDeclaredProperties(clazz));
			paramMap.put("fields", PropertyUtils.getProperties(clazz));
            template.process(paramMap, writer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logException(e);
			return false;
		}
	}

	public static void main(String[] args) {
		//MybatisMapperBuilder.builder(Logger.class);
	}
}