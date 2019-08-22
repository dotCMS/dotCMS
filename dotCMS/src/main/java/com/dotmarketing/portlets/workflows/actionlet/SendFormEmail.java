package com.dotmarketing.portlets.workflows.actionlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.context.Context;

import com.dotcms.api.web.HttpServletRequestThreadLocal;
import com.dotcms.contenttype.model.field.BinaryField;
import com.dotcms.contenttype.model.field.ColumnField;
import com.dotcms.contenttype.model.field.Field;
import com.dotcms.contenttype.model.field.TagField;
import com.dotcms.contenttype.model.type.ContentType;
import com.dotcms.contenttype.model.type.FormContentType;
import com.dotcms.datagen.FileAssetDataGen;
import com.dotcms.mock.request.MockHttpRequest;
import com.dotcms.mock.response.BaseResponse;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.util.ContentletUtil;
import com.dotmarketing.portlets.workflows.model.WorkflowActionClassParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowActionFailureException;
import com.dotmarketing.portlets.workflows.model.WorkflowActionletParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowProcessor;
import com.dotmarketing.util.DNSUtil;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.Mailer;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.VelocityUtil;
import com.google.common.collect.ImmutableSet;
import com.liferay.portal.model.Company;
import com.liferay.portal.util.ContentUtil;

import io.vavr.control.Try;
/**
 * This class is intended to be used to automatically send emails for
 * new form entries.  If the content being passed in is not a Form, then this
 * workflow action is skipped
 *
 */
public class SendFormEmail extends WorkFlowActionlet {

  private static final long serialVersionUID = 1L;
  private static final  String FROM_EMAIL="fromEmail";
  private static final  String FROM_NAME="fromName";
  private static final  String TO_EMAIL="toEmail";
  private static final  String BCC="BCC";
  private static final  String EMAIL_SUBJECT="emailSubject";
  private static final  String CONDITION="condition";
  private static final  String EMAIL_TEMPLATE="emailTemplate";
  
  @Override
  public List<WorkflowActionletParameter> getParameters() {
    

    
    List<WorkflowActionletParameter> params = new ArrayList<WorkflowActionletParameter>();
    params.add(new WorkflowActionletParameter(FROM_EMAIL, "From Email", "$company.emailAddress", true));
    params.add(new WorkflowActionletParameter(FROM_NAME, "From Name", "dotCMS", true));
    params.add(new WorkflowActionletParameter(TO_EMAIL, "To Email", "${content.formEmail}", true));
    params.add(new WorkflowActionletParameter(EMAIL_SUBJECT, "Email Subject", "[dotCMS] new ${contentTypeName}", true));
    params.add(new WorkflowActionletParameter(EMAIL_TEMPLATE, "The email template to parse",
        "#parse('static/form/email-form-entry.vtl')", true));
    
    params.add(new WorkflowActionletParameter(CONDITION, "Condition - email will send unless<br>velocity prints 'false'", "", false));
    params.add(new WorkflowActionletParameter(BCC, "Bcc Email", "", false));

    return params;
  }

  @Override
  public String getName() {
    return "Send Form Email";
  }

  @Override
  public String getHowTo() {
    return "This actionlet takes care of sending form information to the email addresses defined in the form. If the content is not a form, then this actionlet will be skipped and do nothing";
  }

  @Override
  public void executeAction(final WorkflowProcessor processor, Map<String, WorkflowActionClassParameter> params)
      throws WorkflowActionFailureException {

    final Contentlet contentlet = processor.getContentlet();
    final ContentType contentType = contentlet.getContentType();
    if(contentType.fieldMap().get(FormContentType.FORM_EMAIL_FIELD_VAR)==null )  {
      Logger.debug(this.getClass(), contentlet.getTitle() +  "of type " + contentType.variable()  +" does not have a formEmail field, skipping email");
      return;

    }
    final Company company = APILocator.getCompanyAPI().getDefaultCompany();

    final Context context = new VelocityUtil().getWorkflowContext(processor);

    context.put("formMap", getFormMap(processor));
    VelocityEval velocity = new VelocityEval(context);
    
    
    CacheLocator.getVeloctyResourceCache().clearCache();
    final String toEmail = velocity.eval(params.get(TO_EMAIL).getValue());
    final String fromEmail = velocity.eval(params.get(FROM_EMAIL).getOrDefault(company.getEmailAddress()));
    final String fromName = velocity.eval(params.get(FROM_NAME).getOrDefault("dotCMS"));
    final String emailSubject = velocity.eval(params.get(EMAIL_SUBJECT).getValue());
    final String condition = velocity.eval(params.get(CONDITION).getValue());
    final String emailTemplate = velocity.eval(params.get(EMAIL_TEMPLATE).getValue());
    final String bcc = velocity.eval(params.get(BCC).getValue());

    if (UtilMethods.isSet(condition) && condition.indexOf("false") > -1) {
      Logger.info(this.getClass(), processor.getAction().getName() + " email condition contains false, skipping email send");
      return;
    }

    if (UtilMethods.isSet(condition) && condition.trim().indexOf("debug") ==0) {
      try(OutputStream out = new FileOutputStream(new File("./debugging-form-email.html"))){
        IOUtils.write(emailTemplate, out);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return;
    }
    final Mailer mail = new Mailer();
    mail.setToEmail(toEmail);
    mail.setFromEmail(fromEmail);
    mail.setFromName(fromName);
    mail.setSubject(emailSubject);
    mail.setHTMLAndTextBody(emailTemplate);
    if (UtilMethods.isSet(bcc)) {
      mail.setBcc(bcc);
    }
    
    //send the binaries as attachments
    contentType.fields()
      .stream()
      .filter(f -> f instanceof BinaryField)
      .filter(f -> Try.of(()->contentlet.getBinary(f.variable())).getOrNull() !=null)
      .forEach(f -> mail.addAttachment(Try.of(()->contentlet.getBinary(f.variable())).getOrNull()));

    mail.sendMessage();
  }


  
  /**
   * Best efforts to determine the ipAddress of the content 
   * @param contentlet
   * @return
   */
  private String resolveIPAddress(final Contentlet contentlet) {
    return (String) (getInsensitive(contentlet, "ipaddress") != null ? getInsensitive(contentlet, "ipaddress")
        : getInsensitive(contentlet, "ip") != null ? getInsensitive(contentlet, "ip") : "n/a");
  }

  /**
   * gets an HTTPRequest to use for velocity
   * @param contentlet
   * @return
   */
  private HttpServletRequest resolveRequest(final Contentlet contentlet) {

    return (HttpServletRequestThreadLocal.INSTANCE.getRequest() != null) ? HttpServletRequestThreadLocal.INSTANCE.getRequest()
        : new MockHttpRequest(resolveHost(contentlet).getHostname(), null).request();
  }

  /**
   * Best efforts to determine the HOST of the content 
   * @param contentlet
   * @return
   */
  private Host resolveHost(final Contentlet contentlet) {
    return Try
        .of(() -> Host.SYSTEM_HOST.equals(contentlet.getHost()) ? APILocator.getHostAPI().findDefaultHost(APILocator.systemUser(), false)
            : APILocator.getHostAPI().find(contentlet.getHost(), APILocator.systemUser(), false))
        .getOrElse(APILocator.systemHost());
  }

  /**
   * returns the value from the content map regardless of case
   * @param contentlet
   * @param key
   * @return
   */
  private String getInsensitive(final Contentlet contentlet, final String key) {
    Optional<Entry<String, Object>> entry = contentlet.getMap().entrySet().parallelStream().filter(e -> e.getKey().equalsIgnoreCase(key)).findAny();
    return entry.isPresent() ? String.valueOf(entry.get().getValue()) : null;

  }

  private class VelocityEval {
    private final Context context;

    public VelocityEval(Context context) {
      this.context = context;
    }

    String eval(String toEval) {
      return Try.of(() -> VelocityUtil.eval(toEval, this.context)).getOrNull();
    }

  }
  
  private final static Set<String> ignoreFields = ImmutableSet.of(FormContentType.FORM_EMAIL_FIELD_VAR, FormContentType.FORM_RETURN_PAGE_FIELD_VAR,FormContentType.FORM_TITLE_FIELD_VAR);
  
  private Map<String,Object> getFormMap(WorkflowProcessor processor){
    
    final LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    final Contentlet contentlet = processor.getContentlet();
    final ContentType contentType = contentlet.getContentType();
    final Map<String,Object> printableMap = Try.of(()->ContentletUtil.getContentPrintableMap(processor.getUser(), contentlet)).getOrElse(contentlet.getMap());
    final Host host =  resolveHost(contentlet);    
    for(final Field field : contentType.fields()) {
      if(ignoreFields.contains(field.variable()))continue;
      Object obj = contentlet.get(field.variable());
      if(obj==null)continue;
      if(field instanceof BinaryField && UtilMethods.isSet(contentlet.getInode())) {
        map.put(field.name(), "http://" + host.getHostname() + printableMap.get(field.variable() + "Version"));
        continue;
      }
      
      map.put(field.name(), printableMap.get(field.variable()));

    }
    return map;

  }

  
  

}
