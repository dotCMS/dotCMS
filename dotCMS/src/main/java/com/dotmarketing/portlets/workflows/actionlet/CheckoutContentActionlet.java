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

/**
 * Lock a {@link Contentlet}
 */
public class CheckoutContentActionlet extends WorkFlowActionlet {

	private static final long serialVersionUID = 1L;

	public String getName() {
		return "Lock content";
	}

	public String getHowTo() {

		return "This actionlet will checkout and lock the content.";
	}

	public void executeAction(final WorkflowProcessor processor,
							  final Map<String,WorkflowActionClassParameter>  params) throws WorkflowActionFailureException {

		try {

			final Contentlet contentlet = processor.getContentlet();
			contentlet.setProperty(Contentlet.DO_REINDEX, Boolean.FALSE);

			APILocator.getContentletAPI().lock(processor.getContentlet(),
					processor.getUser(), true);
		} catch (Exception e) {

			Logger.error(this.getClass(),e.getMessage(),e);
			throw new  WorkflowActionFailureException(e.getMessage(),e);
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
