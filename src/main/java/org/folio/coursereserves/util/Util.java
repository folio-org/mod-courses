package org.folio.coursereserves.util;

import org.apache.logging.log4j.Logger;

public class Util {
  public static String logAndSaveError(Throwable err, Logger logger) {
    String message = err.getMessage();
    logger.error(message, err);
    return message;
  }

  /**
   * Combine limit query and optional user query.
   * @param userQuery any query provided by API/user; null for none
   * @param limitQuery simple field=value query
   * @return combined CQL query.
   */
  public static String getQueryWithLimit(String userQuery, String limitQuery) {
    if (userQuery == null || userQuery.isEmpty()) {
      return limitQuery;
    } else {
      return limitQuery + " AND (" + userQuery + ")";
    }
  }
}
