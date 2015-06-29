package com.lmiky.platform.freemarker.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lmiky.platform.logger.util.LoggerUtils;
import com.lmiky.platform.util.DateUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * mybatis 映射文件
 * @author lmiky
 * @date 2014年9月14日 下午4:28:35
 */
public class ControllerBuilder extends BaseBuilder {
	public static final String TEMPLATE_FILE_NAME = "controller.ftl";
	public static final String TEMPLATE_OUTPUT_SUFFIX = "Controller.java";

	/**
	 * 构造
	 * @author lmiky
	 * @date 2014年9月14日 下午6:54:44
	 * @param moduleGroup
	 * @param clazz
	 * @param author
	 * @param classDesc
	 * @return
	 */
	public static boolean builder(String moduleGroup, Class<?> clazz, String author, String classDesc) {
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
			paramMap.put("author", author);
			paramMap.put("classDesc", classDesc);
			paramMap.put("createData", DateUtils.format(new Date(), "yyyy-MM-dd"));
			paramMap.put("moduleGroup", moduleGroup);
			String classSimpleName = clazz.getSimpleName();
			paramMap.put("className", classSimpleName);
			String firstLetter = classSimpleName.charAt(0) + "";
			//首字母改为小写
			String classAlias = classSimpleName.replaceFirst(firstLetter, firstLetter.toLowerCase());
			paramMap.put("classAlias", classAlias);
			String moduleName = "";
			for(char c : classAlias.toCharArray()) {
				if(c>='A'  &&  c<='Z') {  
					moduleName = moduleName + "_" + (c + "").toLowerCase();
		        } else {
		        	moduleName += c;
		        }
			}
			paramMap.put("module", moduleName);
			Writer writer  = new OutputStreamWriter(new FileOutputStream(outputDirectoryPath + "\\\\" + classSimpleName + TEMPLATE_OUTPUT_SUFFIX),"UTF-8");  
            template.process(paramMap, writer);
			return true;
		} catch (Exception e) {
			LoggerUtils.logException(e);
			return false;
		}
	}

	public static void main(String[] args) {
		//System.out.println(ControllerBuilder.builder("platform", Logger.class, "lmiky", "操作日志"));
	}
}
