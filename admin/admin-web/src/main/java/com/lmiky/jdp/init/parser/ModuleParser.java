package com.lmiky.jdp.init.parser;

import java.util.List;

import com.lmiky.admin.exception.ParseException;
import com.lmiky.admin.module.pojo.ModuleGroup;

/**
 * 类说明
 * @author lmiky
 * @date 2013-10-13
 */
public interface ModuleParser {
	public List<ModuleGroup> parse() throws ParseException;
}
