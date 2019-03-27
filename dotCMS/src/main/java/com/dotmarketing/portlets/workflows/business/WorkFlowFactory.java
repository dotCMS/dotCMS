package com.dotmarketing.portlets.workflows.business;

import com.dotcms.contenttype.model.type.ContentType;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.exception.AlreadyExistException;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.workflows.model.WorkflowAction;
import com.dotmarketing.portlets.workflows.model.WorkflowActionClass;
import com.dotmarketing.portlets.workflows.model.WorkflowActionClassParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowComment;
import com.dotmarketing.portlets.workflows.model.WorkflowHistory;
import com.dotmarketing.portlets.workflows.model.WorkflowScheme;
import com.dotmarketing.portlets.workflows.model.WorkflowSearcher;
import com.dotmarketing.portlets.workflows.model.WorkflowStep;
import com.dotmarketing.portlets.workflows.model.WorkflowTask;
import com.liferay.portal.model.User;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class provides access to all the information related to Workflows in dotCMS. Workflows
 * allow content authors to specify the different phases that a content must go through when being
 * published. This class provides methods to access information about the workflow assigned to a
 * contentlet, and all the data that is generated during its execution.
 *
 * @author root
 * @since Mar 22, 2012.
 */
public interface WorkFlowFactory {

	String SYSTEM_WORKFLOW_ID           = SystemWorkflowConstants.SYSTEM_WORKFLOW_ID;

	public void deleteComment(WorkflowComment comment) throws DotDataException;

	public void deleteWorkflowHistory(WorkflowHistory history) throws DotDataException;

	public void deleteWorkflowTask(WorkflowTask task) throws DotDataException;

	public WorkflowTask findTaskByContentlet(Contentlet contentlet) throws DotDataException;

	public WorkflowTask findWorkFlowTaskById(String id) throws DotDataException;

	public List<WorkflowTask> searchTasks(WorkflowSearcher searcher) throws DotDataException;

	public WorkflowComment findWorkFlowCommentById(String id) throws DotDataException;

	public WorkflowHistory findWorkFlowHistoryById(String id) throws DotDataException;

	public List<WorkflowHistory> findWorkflowHistory(WorkflowTask task) throws DotDataException;

	public List<Contentlet> findWorkflowTaskFilesAsContent(WorkflowTask task, User user) throws DotDataException;

	public void attachFileToTask(WorkflowTask task, String fileInode) throws DotDataException;

	public void removeAttachedFile(WorkflowTask task, String fileInode) throws DotDataException;

	public List<WorkflowComment> findWorkFlowComments(WorkflowTask task) throws DotDataException;

	/**
	 * This method will get the current workflow step of the contentlet.
	 * If the contentlet doesn't have a workflow step associated, then it will
	 * display all the first workflow steps associated to the contentlet Content Type.
	 *
	 * @param contentlet The current contentlet
	 * @return A list of step available for the contentlet
	 * @throws DotDataException
	 */
	public List<WorkflowStep> findStepsByContentlet(Contentlet contentlet, final List<WorkflowScheme> schemes) throws DotDataException;

	/**
	 * Check if the schemeId pass exist n the list of workflow scheme.
	 * @param schemeId WorkflowScheme ID to validate
	 * @param schemes List of WorkflowScheme to compare
	 * @return true if the scheme Id exist false if not
	 */
	public boolean existSchemeIdOnSchemesList(String schemeId, List<WorkflowScheme> schemes);

	public void saveComment(WorkflowComment comment) throws DotDataException;

	public void saveWorkflowHistory(WorkflowHistory history) throws DotDataException;

	/**
	 * Saves a given WorkflowTask, if the task does not exist it will create a new one and if does exist
	 * it will update the existing record.
	 * <br/>
	 * If the record does not exist and the given task have set an id the new record will be created with that id.
	 *
	 * @param task
	 * @throws DotDataException
	 */
	public void saveWorkflowTask(WorkflowTask task) throws DotDataException;

	public List<WorkflowScheme> findSchemes(boolean showArchived) throws DotDataException;

	public List<WorkflowScheme> findArchivedSchemes() throws DotDataException;

	public WorkflowScheme findScheme(String id) throws DotDataException;

	public List<WorkflowScheme> findSchemesForStruct(final String structId) throws DotDataException;

	/**
	 * Do a force for the delete scheme, avoiding some validations
	 * @param contentTypeId
	 * @throws DotDataException
	 */
	public void forceDeleteSchemeForContentType(String contentTypeId) throws DotDataException;

	public void deleteSchemeForStruct(String struc) throws DotDataException;

	/**
	 * Link the workflows with the {@link com.dotcms.contenttype.model.type.ContentType}, before remove all the
	 * 	workflow linked previously with this {@link com.dotcms.contenttype.model.type.ContentType}
	 * @param contentTypeInode
	 * @param schemes
	 * @param workflowTaskConsumer
	 * @throws DotDataException
	 */
	public void saveSchemesForStruct(String contentTypeInode, List<WorkflowScheme> schemes,
									 Consumer<WorkflowTask> workflowTaskConsumer) throws DotDataException;

	/**
	 * Link the workflows with the {@link com.dotcms.contenttype.model.type.ContentType}, before remove all the
	 * workflow linked previously with this {@link com.dotcms.contenttype.model.type.ContentType}
	 *
	 * @param contentTypeInode
	 * @param schemesIds
	 * @param workflowTaskConsumer {@link Consumer} in case you want to do something which the Workflow task, send a Consumer.
	 * @throws DotDataException
	 */
	public void saveSchemeIdsForContentType(String contentTypeInode, Set<String> schemesIds, Consumer<WorkflowTask> workflowTaskConsumer) throws DotDataException;

	public void saveScheme(WorkflowScheme scheme) throws DotDataException, AlreadyExistException;

	public List<WorkflowStep> findSteps(WorkflowScheme scheme) throws DotDataException;

	public void saveStep(WorkflowStep step) throws DotDataException, AlreadyExistException;

	public List<WorkflowAction> findActions(WorkflowStep step) throws DotDataException;

	/**
	 * Finds the Actions associated to the schema
	 * @param scheme {@link WorkflowScheme}
	 * @return List of WorkflowAction
	 */
	List<WorkflowAction> findActions(WorkflowScheme scheme) throws DotDataException;

	public WorkflowAction findAction(String id) throws DotDataException;

	/**
	 * Finds an action associated to a steps
	 * Null if does not exists.
	 * @param actionId actionId
	 * @param stepId   stepID
	 * @return WorkflowAction
	 * @throws DotDataException
	 */
	public WorkflowAction findAction(String actionId, String stepId) throws DotDataException;

	public void saveAction(WorkflowAction action) throws DotDataException, AlreadyExistException;

	/**
	 * Save (associated) the workflowAction to the workflow step with a specific order
	 * pre: both should exists
	 * @param workflowAction WorkflowAction
	 * @param workflowStep   WorkflowStep
	 * @param order          updates the order
	 */
	void saveAction(WorkflowAction workflowAction, WorkflowStep workflowStep, int order)  throws DotDataException,AlreadyExistException;

	/**
	 * Update (associated) the workflowAction to the workflow step with a specific order
	 * pre: both should exists
	 * @param workflowAction WorkflowAction
	 * @param workflowStep   WorkflowStep
	 * @param order			 int
	 */
	void updateOrder(WorkflowAction workflowAction, WorkflowStep workflowStep, int order)  throws DotDataException,AlreadyExistException;
	/**
	 * Save (associated) the workflowAction to the workflow step
	 * the order will be by default zero
	 * pre: both should exists
	 * @param workflowAction WorkflowAction
	 * @param workflowStep   WorkflowStep
	 */
	void saveAction(WorkflowAction workflowAction, WorkflowStep workflowStep)  throws DotDataException,AlreadyExistException;

	public WorkflowStep findStep(String id) throws DotDataException;

	/**
	 * Deletes a single action
	 * Pre: not any relationship must be for the action (for instance all the action references on the steps should be previously removed)
	 * @param action
	 * @throws DotDataException
	 * @throws AlreadyExistException
	 */
	public void deleteAction(WorkflowAction action) throws DotDataException, AlreadyExistException;

	/**
	 * Deletes an action from the step (the relationship)
	 * @param action WorkflowAction
	 * @param step   WorkflowStep
	 */
	void deleteAction(WorkflowAction action, WorkflowStep step) throws DotDataException, AlreadyExistException;

	/**
	 * Deletes the actions related to the step
	 * @param step {@link WorkflowStep}
	 * @throws DotDataException
	 * @throws AlreadyExistException
	 */
	void deleteActions(WorkflowStep step) throws DotDataException, AlreadyExistException;

	/**
	 * Deletes the step and the workflow tasks associated to the step. In case you need to consume (do something) with the {@link WorkflowTask} deleted,
	 * pass a {@link Consumer}
	 * @param step {@link WorkflowStep}
	 * @throws DotDataException
	 * @throws AlreadyExistException
	 */
	public void deleteStep(WorkflowStep step, Consumer<WorkflowTask> workflowTaskConsumer) throws DotDataException, AlreadyExistException;
	
	public int getCountContentletsReferencingStep(WorkflowStep step) throws DotDataException;

	public List<WorkflowActionClass> findActionClasses(WorkflowAction action) throws DotDataException;

	public WorkflowActionClass findActionClass(String id) throws DotDataException;

	public void deleteActionClass(WorkflowActionClass actionClass) throws DotDataException, AlreadyExistException;

	public List<WorkflowStep> findProxiesSteps(final WorkflowAction action) throws DotDataException;

	public void saveActionClass(WorkflowActionClass actionClass) throws DotDataException, AlreadyExistException;

	public Map<String, WorkflowActionClassParameter> findParamsForActionClass(WorkflowActionClass actionClass) throws DotDataException;

	public void saveWorkflowActionClassParameter(WorkflowActionClassParameter param) throws DotDataException, AlreadyExistException;

	public void deleteWorkflowActionClassParameters(WorkflowActionClass actionClass) throws DotDataException, AlreadyExistException;

	public int countTasks(WorkflowSearcher searcher) throws DotDataException;

	public void copyWorkflowActionClassParameter(WorkflowActionClassParameter from, WorkflowActionClass actionClass) throws DotDataException;

	public void copyWorkflowActionClass(WorkflowActionClass from, WorkflowAction action) throws DotDataException;

	public void copyWorkflowAction(WorkflowAction from, WorkflowStep step) throws DotDataException;

	public void copyWorkflowStep(WorkflowStep from, WorkflowScheme scheme) throws DotDataException;

	public List<WorkflowTask> searchAllTasks(WorkflowSearcher searcher) throws DotDataException;

	public WorkflowHistory retrieveLastStepAction(String taskId) throws DotDataException;

    public List<WorkflowTask> findExpiredTasks() throws DotDataException, DotSecurityException;

    public WorkflowScheme findSchemeByName(String schemaName) throws DotDataException;

    public void deleteWorkflowActionClassParameter(WorkflowActionClassParameter param) throws DotDataException, AlreadyExistException;

	/**
	 * Method will replace user references of the given userId in workflow, workflow_ action task
	 * and workflow comments with the replacement user id.
	 *
	 * @param userId                User Identifier
	 * @param userRoleId            The role id of the user
	 * @param replacementUserId     The user id of the replacement user
	 * @param replacementUserRoleId The role Id of the replacemente user
	 *
	 * @throws DotDataException     There is a data inconsistency
	 * @throws DotStateException    There is a data inconsistency
	 * @throws DotSecurityException
	 */
	public void updateUserReferences(String userId, String userRoleId, String replacementUserId, String replacementUserRoleId)throws DotDataException, DotSecurityException;

	/**
	 * Method will replace step references of the given stepId in workflow, workflow_action task and
	 * contentlets with the replacement step id.
	 *
	 * @param stepId            Step Identifier
	 * @param replacementStepId The step id of the replacement step
	 *
	 * @throws DotDataException     There is a data inconsistency
	 * @throws DotStateException    There is a data inconsistency
	 * @throws DotSecurityException
	 */
	public void updateStepReferences(String stepId, String replacementStepId) throws DotDataException, DotSecurityException;

	/**
	 * Return all the workflow tasks that are associated to a workflow step.
	 * @return List of workflows task associated to the step
	 * @throws DotDataException
	 * @throws DotSecurityException
	 */
	public List<WorkflowTask> findTasksByStep(String stepId) throws DotDataException, DotSecurityException;

	/**
	 * Return the list of content types that uses the specified workflow scheme
	 *
	 * @param scheme The workflow scheme
	 * @return List of content Types
	 * @throws DotDataException
	 * @throws DotSecurityException
	 */
	public List<ContentType> findContentTypesByScheme(WorkflowScheme scheme) throws DotDataException, DotSecurityException;

	/**
	 * Delete the scheme from the db and the references with the associated content types
	 * @param scheme The workflow scheme to delete
	 * @throws DotDataException
	 * @throws DotSecurityException
	 */
	public void deleteScheme(WorkflowScheme scheme) throws DotDataException, DotSecurityException;

	/**
	 * finds all contentlets with a null task for a specific Workflow
	 * In other words all contents on which a workflow has been reset or hasn't been kicked off.
	 * @param workflowSchemeId
	 * @return
	 * @throws DotDataException
	 */
	public Set<String> findNullTaskContentletIdentifiersForScheme(final String workflowSchemeId) throws DotDataException;

	/**
	 * Return the system work flow scheme
	 * @return The system workflow scheme
	 * @throws DotDataException
	 */
	public WorkflowScheme findSystemWorkflow() throws DotDataException;
}
