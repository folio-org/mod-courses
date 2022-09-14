package org.folio.coursereserves.util;

import org.apache.logging.log4j.Logger;
import org.folio.util.StringUtil;

public class Util {
  public static String logAndSaveError(Throwable err, Logger logger) {
    String message = err.getMessage();
    logger.error(message, err);
    return message;
  }

  /**
   * Query by course listing id with optional user query.
   * @param userQuery any query provided by API/user; null for no query
   * @param listingId
   * @return combined CQL query.
   */
  public static String queryCourseListing(String userQuery, String listingId) {
    String courseQueryClause = String.format("courseListingId == %s", StringUtil.cqlEncode(listingId));
    if (userQuery == null || userQuery.isEmpty()) {
      return courseQueryClause;
    } else {
      return courseQueryClause + " AND (" + userQuery + ")";
    }
  }
}
