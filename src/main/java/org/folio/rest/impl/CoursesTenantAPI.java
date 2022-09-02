package org.folio.rest.impl;

import io.vertx.core.Context;
import io.vertx.core.Future;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.folio.rest.tools.utils.TenantLoading;

public class CoursesTenantAPI extends TenantAPI {

  public static final String SAMPLE_DATA_COURSELISTING = "c03bcba3-a6a0-4251-b316-0631bb2e6f21";
  public static final Logger logger = LogManager.getLogger(CoursesTenantAPI.class);
  protected static final String PARAMETER_LOAD_SAMPLE = "loadSample";

  @Override
  Future<Integer> loadData(TenantAttributes attributes, String tenantId,
                           Map<String, String> headers, Context vertxContext) {
     return super.loadData(attributes, tenantId, headers, vertxContext).compose(
        num ->
          new TenantLoading()
              .withKey(PARAMETER_LOAD_SAMPLE).withLead("sample-data").withPostOnly()
              .add("terms", "coursereserves/terms")
              .add("copyrightstatuses", "coursereserves/copyrightstatuses")
              .add("departments", "coursereserves/departments")
              .add("processingstatuses", "coursereserves/processingstatuses")
              .add("coursetypes", "coursereserves/coursetypes")
              .add("courselistings", "coursereserves/courselistings")
              .add("courses", "coursereserves/courses")
              .add("instructors", "coursereserves/courselistings/"+SAMPLE_DATA_COURSELISTING+"/instructors")
              .add("reserves", "coursereserves/reserves")
              .perform(attributes, headers, vertxContext, num));
  }
}
