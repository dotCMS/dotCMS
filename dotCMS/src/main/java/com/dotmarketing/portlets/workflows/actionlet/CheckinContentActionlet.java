package com.dotmarketing.portlets.workflows.actionlet;

import java.util.List;
import java.util.Map;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.workflows.model.WorkflowActionClassParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowActionFailureException;
import com.dotmarketing.portlets.workflows.model.WorkflowActionletParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowProcessor;
import com.dotmarketing.portlets.workflows.model.WorkflowStep;
import com.dotmarketing.util.Logger;

public class CheckinContentActionlet extends WorkFlowActionlet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getName() {
		return "Unlock content";
	}

	public String getHowTo() {

		return "This actionlet will checkin and unlock the content.";
	}

	public void executeAction(final WorkflowProcessor processor,
							  final Map<String,WorkflowActionClassParameter>  params) throws WorkflowActionFailureException {

		Object workflowInProgress = null;

		try {

			final Contentlet contentlet = processor.getContentlet();
			workflowInProgress = contentlet.get(Contentlet.WORKFLOW_IN_PROGRESS);
			contentlet.setProperty(Contentlet.WORKFLOW_IN_PROGRESS, Boolean.TRUE);
			processor.getContentlet().setProperty(Contentlet.DO_REINDEX, Boolean.FALSE);

			APILocator.getContentletAPI().unlock(processor.getContentlet(), processor.getUser(),
					processor.getContentletDependencies() != null
							&& processor.getContentletDependencies().isRespectAnonymousPermissions());
		} catch (Exception e) {

			Logger.error(this.getClass(),e.getMessage(),e);
			throw new  WorkflowActionFailureException(e.getMessage(),e);
		} finally {

			if (null != processor.getContentlet()) {
				processor.getContentlet().setProperty(Contentlet.WORKFLOW_IN_PROGRESS, workflowInProgress);
			}
		}

	}

	public WorkflowStep getNextStep() {

		return null;
	}

	@Override
	public  List<WorkflowActionletParameter> getParameters() {

		return null;
	}
}
