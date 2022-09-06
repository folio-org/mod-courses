package org.folio.coursereserves.util;

import org.apache.logging.log4j.Logger;

public class Util {
  public static String logAndSaveError(Throwable err, Logger logger) {
    String message = err.getMessage();
    logger.error(message, err);
    return message;
  }
}
