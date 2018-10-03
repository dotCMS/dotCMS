package com.dotcms.contenttype.transform.relationship;

import com.dotcms.repackage.com.google.common.collect.ImmutableList;
import com.dotmarketing.portlets.structure.model.Relationship;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbRelationshipTransformer implements RelationshipTransformer{

	final List<Map<String, Object>> results;

	public DbRelationshipTransformer(List<Map<String, Object>> list){
		this.results = list;
	}

	public DbRelationshipTransformer(Map<String, Object> map){
		this.results = ImmutableList.of(map);
	}

	@Override
	public List<Relationship> asList() {
		List<Relationship> newList = new ArrayList<>();
		for(Map<String,Object> map : results){
			newList.add(fromMap(map));
		}
		return ImmutableList.copyOf(newList);
	}

	@Override
	public Relationship from(){
		return fromMap(results.get(0));
	}

	private static Relationship fromMap(Map<String, Object> map) {

		Relationship var = new Relationship();
		var.setInode((String) map.get("inode"));
		var.setParentStructureInode((String) map.get("parent_structure_inode"));
		var.setChildStructureInode((String) map.get("child_structure_inode"));
		var.setParentRelationName((String) map.get("parent_relation_name"));
		var.setChildRelationName((String) map.get("child_relation_name"));
		var.setRelationTypeValue((String) map.get("relation_type_value"));
		var.setCardinality((Integer) map.get("cardinality"));
		var.setFixed((Boolean) map.get("fixed"));
		var.setParentRequired((Boolean) map.get("parent_required"));
		var.setChildRequired((Boolean) map.get("child_required"));

		return var;

	}
}