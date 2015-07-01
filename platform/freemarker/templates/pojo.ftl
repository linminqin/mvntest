package ${package};

<#list fields as field>
<#if (field.type == 'Date')>
import java.util.Date;
	<#break/>
</#if>
</#list>
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.lmiky.jdp.database.pojo.BasePojo;

/**
 * ${pojoDesc}
 * @author lmiky
 * @date ${createData}
 */
@Entity
@Table(name="${tableName}")
public class ${className} extends BasePojo {
	<#list fields as field>
	private ${field.type} ${field.name};
	</#list>
	
	<#list fields as field>
	/**
	 * @return the ${field.name}
	 */
	@Column(name="${field.name}")
	public ${field.type} get${field.firstUpercaseNme}() {
		return ${field.name};
	}
	/**
	 * @param ${field.name} the ${field.name} to set
	 */
	public void set${field.firstUpercaseNme}(${field.type} ${field.name}) {
		this.${field.name} = ${field.name};
	}
	</#list>

}
