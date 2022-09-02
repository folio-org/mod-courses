package org.folio.coursereserves.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.folio.rest.impl.CourseAPI;
import org.folio.rest.jaxrs.model.LocationObject;
import org.folio.rest.jaxrs.model.Reserf;
import org.folio.rest.jaxrs.model.TemporaryLocationObject;
import org.folio.rest.jaxrs.model.Reserve;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class UtilTest {

  public static final Logger logger = LogManager.getLogger(UtilTest.class);

  @Test
  public void testPojoFromJson() throws Exception {
    LocationObject locationObject = new LocationObject();
    String name = "Front Desk";
    String id = UUID.randomUUID().toString();
    JsonObject json = new JsonObject();
    json.put("id", id);
    json.put("name", name);
    json.put("isActive", Boolean.TRUE);
    JsonArray servicePointIds = new JsonArray();
    servicePointIds.add(UUID.randomUUID().toString());
    servicePointIds.add(UUID.randomUUID().toString());
    servicePointIds.add(UUID.randomUUID().toString());
    json.put("servicePointIds", servicePointIds);
    List<PopulateMapping> mapList = new ArrayList<>();
    mapList.add(new PopulateMapping("id"));
    mapList.add(new PopulateMapping("name"));
    mapList.add(new PopulateMapping("isActive", PopulateMapping.ImportType.BOOLEAN));
    mapList.add(new PopulateMapping("servicePointIds", PopulateMapping.ImportType.STRINGLIST));
    CRUtil.populatePojoFromJson(locationObject, json, mapList);
    assertTrue(locationObject.getName().equals(name));
    assertTrue(locationObject.getId().equals(id));
    assertTrue(locationObject.getIsActive());
    assertTrue(locationObject.getServicePointIds().size() == 3);
  }

  @Test
  public void testCopyFields() {
    TemporaryLocationObject tempLocationObject = new TemporaryLocationObject();
    LocationObject locationObject = new LocationObject();
    locationObject.setId(UUID.randomUUID().toString());
    locationObject.setName("Big Library");
    locationObject.setIsActive(Boolean.TRUE);
    CRUtil.copyFields(tempLocationObject, locationObject);
    assertTrue(tempLocationObject.getId().equals(locationObject.getId()));
    assertTrue(tempLocationObject.getName().equals(locationObject.getName()));
    assertTrue(tempLocationObject.getIsActive().equals(locationObject.getIsActive()));
  }

  @Test
  public void testNullCopyFields() {
    TemporaryLocationObject tempLocationObject = new TemporaryLocationObject();
    CRUtil.copyFields(tempLocationObject, null);
    assertNull(tempLocationObject.getId());
  }

  @Test
  public void testReserfListFromReserveList() {
    List<Reserve> reserveList = new ArrayList<>();
    Reserve reserve = new Reserve();
    reserve.setId(UUID.randomUUID().toString());
    reserveList.add(reserve);
    List<Reserf> reserfList = CourseAPI.reserfListFromReserveList(reserveList);
    assertEquals(reserfList.get(0).getId(), reserve.getId());
  }

  @Test
  public void testGetStringValuesFromObjectArrays() {
    JsonArray array = new JsonArray();
    array.add(new JsonObject().put("dog", "woof"));
    assertEquals(CRUtil.getStringValueFromObjectArray("dog", array), "woof");
    assertNull(CRUtil.getStringValueFromObjectArray("cat", array));
  }

  @Test
  public void testLogAndSaveError() {
    String ahhString = "AAAAAAAHHHH";
    try {
      throw new Exception(ahhString);
    } catch(Exception e) {
      String message = Util.logAndSaveError(e, logger);
      assertTrue(message.contains(ahhString));
    }
  }

  @Test public void testMakeCallNumber() {
    String callNumberGood = CRUtil.makeCallNumber("F", "99.99", "COW");
    assertEquals("F99.99COW", callNumberGood);
    String callNumberBad = CRUtil.makeCallNumber("G", null, "PIG");
    assertNull(callNumberBad);
  }


  @Test public void testExpandLocaldate() {
    String localDate = "2001-01-01";
    String expandDate = CRUtil.UTCFromLocalDate(localDate);
    assertEquals(expandDate, "2001-01-01T00:00:00Z");
  }

  @Test public void testExpandNonLocaldate() {
    String localDate = "2001-01-01T13:10:11";
    String expandDate = CRUtil.UTCFromLocalDate(localDate);
    assertEquals(expandDate, localDate);
  }

  @Test
  public void populateReserveCopiedItemFromJson1() {
    Reserve reserve = new Reserve();
    CRUtil.populateReserveCopiedItemFromJson(reserve,
        new JsonObject()
            .put("instance", new JsonObject()
                .put("title", "my title"))
            .put("holdings", new JsonObject().put("id", "1"))
            .put("item", new JsonObject()
                .put("copyNumbers", true))
    );
    assertNull(reserve.getCopiedItem().getCopy());
  }

  @Test
  public void populateReserveCopiedItemFromJson2() {
    Reserve reserve = new Reserve();
    CRUtil.populateReserveCopiedItemFromJson(reserve,
        new JsonObject()
            .put("instance", new JsonObject()
                .put("title", "my title"))
            .put("holdings", new JsonObject().put("id", "1"))
            .put("item", new JsonObject()
                .put("copyNumbers", new JsonArray().add("2"))
                .put("electronicAccess", "1")
            )
    );
    assertEquals("2", reserve.getCopiedItem().getCopy());
  }

}
