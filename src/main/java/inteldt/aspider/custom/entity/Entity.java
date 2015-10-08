package inteldt.aspider.custom.entity;

import java.util.List;
import java.util.Map;

public abstract class Entity {

	Map<String,String> strFields;
	Map<String,Integer> numFields;
	Map<String,List<String>> listFields;

}
