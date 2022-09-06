
package CourseAPITest;

import java.util.UUID;

import org.folio.rest.impl.CourseAPI;
import org.folio.rest.jaxrs.model.CopyrightStatusObject;
import org.folio.rest.jaxrs.model.CopyrightTracking;
import org.folio.rest.jaxrs.model.CourseListing;
import org.folio.rest.jaxrs.model.LocationObject;
import org.folio.rest.jaxrs.model.Reserve;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public class CourseAPIUnitTest {
  @Test
  public void testScrub() {
    CourseListing courseListing = new CourseListing();
    //courseListing.setServicepointObject(servicepointObject);
    LocationObject locationObject = new LocationObject();
    locationObject.setId(UUID.randomUUID().toString());
    courseListing.setLocationObject(locationObject);
    assertNotNull(courseListing.getLocationObject());
    CourseAPI.scrubDerivedFields(courseListing);
    assertNull(courseListing.getLocationObject());
    Reserve reserve = new Reserve();
    CopyrightTracking copyrightTracking = new CopyrightTracking();
    CopyrightStatusObject copyrightStatusObject = new CopyrightStatusObject();
    copyrightStatusObject.setId(UUID.randomUUID().toString());
    copyrightTracking.setCopyrightStatusObject(copyrightStatusObject);
    reserve.setCopyrightTracking(copyrightTracking);
    assertNotNull(reserve.getCopyrightTracking().getCopyrightStatusObject());
    CourseAPI.scrubDerivedFields(reserve);
    assertNull(reserve.getCopyrightTracking().getCopyrightStatusObject());
  }


}
