package com.dotmarketing.portlets.workflows.ajax;

import com.dotcms.repackage.com.fasterxml.jackson.databind.DeserializationFeature;
import com.dotcms.repackage.com.fasterxml.jackson.databind.ObjectMapper;
import com.dotcms.workflow.form.WorkflowActionStepBean;
import com.dotcms.workflow.form.WorkflowStepForm;
import com.dotcms.workflow.helper.WorkflowHelper;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.web.UserWebAPI;
import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.exception.AlreadyExistException;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.portlets.workflows.business.WorkflowAPI;
import com.dotmarketing.portlets.workflows.model.WorkflowScheme;
import com.dotmarketing.portlets.workflows.model.WorkflowStep;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

public class WfStepAjax extends WfBaseAction {

	private final WorkflowHelper workflowHelper = WorkflowHelper.getInstance();
	private final UserWebAPI     userWebAPI     = WebAPILocator.getUserWebAPI();
	private final WorkflowAPI    workflowAPI 	= APILocator.getWorkflowAPI();

	public void action(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{};
	
	/**
	 * Even though this method is named 'reorder'. It really provides an 'update' functionality
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void reorder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String stepId = request.getParameter("stepId");
		final String o = request.getParameter("stepOrder");
		final String stepName = request.getParameter("stepName");
		final boolean enableEscalation = request.getParameter("enableEscalation") != null;
		final String escalationAction = request.getParameter("escalationAction");
		final String escalationTime = request.getParameter("escalationTime");
		final boolean stepResolved = request.getParameter("stepResolved") != null;
		Integer stepOrder = null;
        final User user = this.userWebAPI.getUser(request);
		try {
			stepOrder = Integer.parseInt(o);
		} catch (NumberFormatException nfe) {
			//order param is not present
		}
		try {
			final WorkflowStepForm from = new WorkflowStepForm.Builder().stepName(stepName).enableEscalation(enableEscalation).escalationAction(escalationAction).escalationTime(escalationTime).stepResolved(stepResolved).stepOrder(stepOrder).build();
			final WorkflowStep step = workflowAPI.findStep(stepId);
            workflowHelper.updateStep(step, from, getUser());
		} catch (Exception e) {
			Logger.error(this.getClass(), e.getMessage());
			Logger.debug(this.getClass(), e.getMessage(), e);
			writeError(response, e.getMessage());
		}

	}

	public void delete(final HttpServletRequest request,
					   final HttpServletResponse response) throws ServletException, IOException {

		final String stepId = request.getParameter("stepId");

		try {

			this.workflowHelper.deleteStep (stepId);
		} catch (Exception e) {
			Logger.error(this.getClass(),e.getMessage());
			Logger.debug(this.getClass(),e.getMessage(),e);
			writeError(response, "</br> Delete Failed : </br>"+  e.getMessage());
		}
	} // delete.

	/**
	 * Associated an existing action to the step.
	 * If the action or step does not exists, returns fail.
	 * If the action is already associated to the step returns fail.
	 * If the user does not have permission to do the action returns fail.
	 * @param request   HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addActionToStep(final HttpServletRequest request,
								final HttpServletResponse response) throws ServletException, IOException {

		final String stepId   = request.getParameter("stepId");
		final String actionId = request.getParameter("actionId");
        final int order= (request.getParameter("order")!=null) ? Integer.valueOf(request.getParameter("order")): 0;
		try {

			final User user   = this.userWebAPI.getUser(request);

			Logger.debug(this, "Adding the action: " + actionId +
							", to the step: " + stepId);
			
			
			
			this.workflowHelper.saveActionToStep (
					new WorkflowActionStepBean.Builder()
							.stepId(stepId)
							.actionId(actionId)
							.order(order)
							.build(), user);

			writeSuccess(response, stepId );
		} catch (Exception e) {
 			Logger.error(this.getClass(),e.getMessage());
			Logger.debug(this.getClass(),e.getMessage(),e);
			writeError(response, e.getMessage());
		}
	}

	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String stepName = URLDecoder.decode(request.getParameter("stepName"), "UTF-8");
		String schemeId = request.getParameter("schemeId");
		boolean stepResolved = request.getParameter("stepResolved") != null;
		WorkflowAPI wapi = APILocator.getWorkflowAPI();
		
		try {

			List<WorkflowStep> steps =wapi.findSteps(wapi.findScheme(schemeId));
			int maxOrder = 0;
			for(WorkflowStep step : steps){
				if(step.getMyOrder() > maxOrder){
					maxOrder = step.getMyOrder() ;
				}
			}
			WorkflowStep step = new WorkflowStep();
			if(steps.size() != 0)
				maxOrder = maxOrder + 1;
			step.setMyOrder(maxOrder);
			step.setName(stepName);
			step.setSchemeId(schemeId);
			step.setResolved(stepResolved);
			wapi.saveStep(step, this.userWebAPI.getUser(request));
			
		} catch (DotDataException e) {
			Logger.error(this.getClass(),e.getMessage());
			Logger.debug(this.getClass(),e.getMessage(),e);
			writeError(response, e.getMessage());
		}catch (AlreadyExistException e) {
			Logger.error(this.getClass(),e.getMessage());
			Logger.debug(this.getClass(),e.getMessage(),e);
			writeError(response, e.getMessage());
		}
		
		
	}

	
	public void listByScheme(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String schemeId = request.getParameter("schemeId");
		if(schemeId ==null || schemeId.length() < 1){
			return;
		}
		WorkflowAPI wapi = APILocator.getWorkflowAPI();

		try {
			WorkflowScheme scheme = wapi.findScheme(schemeId);
			List<WorkflowStep> steps =  wapi.findSteps(scheme);
			
			
            response.getWriter().write(stepsToJson(steps));

			
		}
		catch(Exception e){
			Logger.error(this.getClass(),e.getMessage());
			Logger.debug(this.getClass(),e.getMessage(),e);
			writeError(response, e.getMessage());
			return;
		}
			
	}
	
	
	private String stepsToJson(List<WorkflowStep> steps) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Map<String,Object> m = new LinkedHashMap<String, Object>();
        
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(WorkflowStep step : steps){

        	Map<String,Object> map = new HashMap<String,Object>();
        	map.put("name", step.getName()   );
        	map.put("id", step.getId());
    		list.add(map);
        }
        

        m.put("identifier", "id");
        m.put("label", "name");
        m.put("items", list);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(m);
	}
	
	
	
}
