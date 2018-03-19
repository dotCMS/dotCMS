package com.dotmarketing.portlets.workflows.model;

import java.io.Serializable;
import java.util.Date;


import com.dotcms.repackage.com.fasterxml.jackson.annotation.JsonIgnore;
import com.dotcms.repackage.com.fasterxml.jackson.annotation.JsonProperty;
import com.dotmarketing.portlets.workflows.business.WorkflowAPI;
import com.dotmarketing.util.UtilMethods;

public class WorkflowScheme implements Serializable {

	private static final long serialVersionUID = 1L;

	String id;
	Date creationDate = new Date();
	String name;
	String description;
	boolean archived;
	boolean mandatory;
	boolean defaultScheme;
	private Date modDate = new Date();
	

	public boolean isDefaultScheme() {
		return defaultScheme;
	}

	public void setDefaultScheme(boolean defaultScheme) {
		this.defaultScheme = defaultScheme;
	}
	String entryActionId;

	@Deprecated
	public String getEntryActionId() {
		return entryActionId;
	}

	@Deprecated
	public void setEntryActionId(String entryActionId) {
		this.entryActionId = entryActionId;
	}

	@Override
	public String toString() {
		return "WorkflowScheme [id=" + id + ", name=" + name + ", archived=" + archived + ", mandatory=" + mandatory + "]";
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	@Deprecated
	public boolean isMandatory() {
		return mandatory;
	}

	@Deprecated
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	@JsonIgnore
	public boolean isNew(){
		return !UtilMethods.isSet(id);

	}

	/**
	 * Returns true if this scheme is the system workflow.
	 * @return boolean
	 */
	@JsonProperty("system")
	public boolean isSystem () {

		return WorkflowAPI.SYSTEM_WORKFLOW_ID.equals(this.getId());
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

}
