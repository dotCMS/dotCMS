package com.dotmarketing.portlets.workflows.actionlet;

import com.dotmarketing.business.APILocator;
import com.dotmarketing.factories.PublishFactory;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.htmlpageasset.model.HTMLPageAsset;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.portlets.workflows.model.*;
import com.dotmarketing.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Actionlet(publish = true)
public class PublishContentActionlet extends WorkFlowActionlet {

    private static final long serialVersionUID = 1L;

    public String getName() {
        return "Publish content";
    }

    public String getHowTo() {
        return "This actionlet will publish the content.";
    }

    public WorkflowStep getNextStep() {
        return null;
    }

    @Override
    public List<WorkflowActionletParameter> getParameters() {
        return null;
    }

    public void executeAction(WorkflowProcessor processor, Map<String, WorkflowActionClassParameter> params) throws WorkflowActionFailureException {

        try {

            final Contentlet contentlet = processor.getContentlet();
            final int structureType = contentlet.getStructure().getStructureType();
            processor.getContentlet().setProperty(Contentlet.DO_REINDEX, Boolean.FALSE);

            if (processor.getContentlet().isArchived()) {
                APILocator.getContentletAPI().unarchive(processor.getContentlet(), processor.getUser(), false);
            }

            //First verify if we are handling a HTML page
            if (structureType == Structure.STRUCTURE_TYPE_HTMLPAGE) {

                final HTMLPageAsset htmlPageAsset = APILocator.getHTMLPageAssetAPI().fromContentlet(contentlet);

                //Get the un-publish content related to this HTMLPage
                List relatedNotPublished = new ArrayList();
                /*
                Returns the list of unpublished related content for this HTML page where
                the user have permissions to publish that related content.
                 */
                htmlPageAsset.setProperty(Contentlet.WORKFLOW_IN_PROGRESS, Boolean.TRUE);
                htmlPageAsset.setProperty(Contentlet.DO_REINDEX, Boolean.FALSE);
                relatedNotPublished = PublishFactory.getUnpublishedRelatedAssetsForPage(htmlPageAsset, relatedNotPublished, true, processor.getUser(), false);
                //Publish the page and the related content
                htmlPageAsset.setProperty(Contentlet.WORKFLOW_IN_PROGRESS, Boolean.TRUE);
                htmlPageAsset.setProperty(Contentlet.DO_REINDEX, Boolean.FALSE);
                PublishFactory.publishHTMLPage(htmlPageAsset, relatedNotPublished, processor.getUser(),
                        processor.getContentletDependencies() != null
                                && processor.getContentletDependencies().isRespectAnonymousPermissions());

            } else {
                APILocator.getContentletAPI().publish(processor.getContentlet(), processor.getUser(),
                        processor.getContentletDependencies() != null
                                && processor.getContentletDependencies().isRespectAnonymousPermissions());
            }

        } catch (Exception e) {

            Logger.error(PublishContentActionlet.class, e.getMessage(), e);
            throw new WorkflowActionFailureException(e.getMessage(),e);
        }
    }

}