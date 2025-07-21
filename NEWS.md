## 1.5.0 In Progress
* Add support for 11.0 item-storage interface ([MODCR-139](https://folio-org.atlassian.net/browse/MODCR-139))

## 1.4.12 2025-04-23
* Update Java from 17 to 21 ([MODCR-134](https://folio-org.atlassian.net/browse/MODCR-134))
* Update all dependencies for Sunflower (R1-2025) ([MODCR-136](https://folio-org.atlassian.net/browse/MODCR-136))
* Add "apk upgrade" to Dockerfile fixing vulns ([MODCR-137](https://folio-org.atlassian.net/browse/MODCR-137))

## 1.4.11 2024-11-02
* Upgrade `holdings-storage` to 8.0 ([MODCR-128](https://folio-org.atlassian.net/browse/MODCR-128))
* Add module descriptor validation (([MODCR-122](https://folio-org.atlassian.net/browse/MODCR-122))
* Support referential integrity for instructors ([[MODCR-121](https://folio-org.atlassian.net/browse/MODCR-121))

## 1.4.10 2024-03-22
* Correct issue with logging import

## 1.4.9 2024-03-21
* Update RMB to version 35.2.0 ([MODCR-116](https://folio-org.atlassian.net/browse/MODCR-116))

## 1.4.8 2023-10-13
* Update Java version to 17 ([MODCR-111](https://issues.folio.org/browse/MODCR-111))
* Add new sample data ([MODCR-109](https://issues.folio.org/browse/MODCR-109))
* Set item temporary location when reserved ([MODCR-107](https://issues.folio.org/browse/MODCR-107))

## 1.4.7 2023-02-24
* Update to permit use of new inventory-storage interface ([MODCR-103](https://issues.folio.org/projects/MODCR/issues/MODCR-103))
* Update reference data

## 1.4.6 2022-10-28
* Add personal disclosure statement ([MODCR-88](https://issues.folio.org/browse/MODCR-88))
* Update to RMB 35, Vert.x 4.3.4 ([MODCR-100](https://issues.folio.org/browse/MODCR-100))
* Refactor code to use Future rather than Promise ([MODCR-94](https://issues.folio.org/browse/MODCR-94))

## 1.4.5 2022-09-07
* Fix socket leak ([MODCR-95](https://issues.folio.org/browse/MODCR-95))

## 1.4.4 2022-09-06
* Use Log4j2 ([MODCR-91](https://issues.folio.org/browse/MODCR-91))
* Update to RMB 34.1.2, Vert.x 4.3.3 ([MODCR-90](https://issues.folio.org/browse/MODCR-90))
* Support instance-storage 9.0, holdings-storage 6.0, item-storage 10.0 ([MODCR-89](https://issues.folio.org/browse/MODCR-89))

## 1.4.3 2021-12-17
* Update to RMB 32.2.4 to address log4j vulnerability ([MODCR-73](https://issues.folio.org/browse/MODCR-73))

## 1.4.2 2021-11-11
* Expand local date strings in Reserves into full UTC date strings ([UICR-145](https://issues.folio.org/browse/UICR-145))

## 1.4.1 2021-10-08
* Close potential CQL injection vulnerabilities ([MODCR-66](https://issues.folio.org/browse/MODCR-66))

## 1.4.0 2021-07-28
* Add "discoverySuppress" to copiedItem structure in Reserve

## 1.3.0 2021-06-26
* Switch to Testcontainers
* Cleanup Future composition
* Update minor version

## 1.2.3 2021-06-19
* Correct issue with temporaryLocationId removal ([MODCR-58](https://issues.folio.org/browse/MODCR-58))
* Update interface versions for inventory ([MODCR-59](https://issues.folio.org/browse/MODCR-59))
* Correct issue with potential CQL injection ([MODCR-62](https://issues.folio.org/browse/MODCR-62))

## 1.2.2 2021-04-26
* Upgrade to RMB 32.2.2 ([MODCR-58](https://issues.folio.org/browse/MODCR-58))
* Add module permissions to PUT for requests
* Clean up technical debt (MODCR-54, [MODCR-55](https://issues.folio.org/browse/MODCR-55))

## 1.2.1 2021-04-23
* Upgrade to RMB 32.2.1 ([MODCR-57](https://issues.folio.org/browse/MODCR-57))
* Fix issue with updating temporaryLocationId to null value ([MODCR-56](https://issues.folio.org/browse/MODCR-56))

## 1.2.0 2021-03-10
* Upgrade to RMB 32.2.0

## 1.1.2 2021-01-28
* Add ForeignKey entry for Coursetype into Courselistings table ([MODCR-50](https://issues.folio.org/browse/MODCR-50))

## 1.1.1 2020-11-06
* Update to RMB v31.1.5, Vertx 3.9.4 ([MODCR-46](https://issues.folio.org/browse/MODCR-46))

## 1.1.0 2020-10-05
* Update to RMB v31, JDK 11 ([MODCR-43](https://issues.folio.org/browse/MODCR-43))

## 1.0.7 2020-09-15
* Allow tenant init even if load sample fails ([MODCR-41](https://issues.folio.org/browse/MODCR-41))

## 1.0.6 2020-09-10
* Fix bug with tenant init parameters not being parsed ([MODCR-42](https://issues.folio.org/browse/MODCR-42))

## 1.0.5 2020-06-03
* Fix bug with instructorObject cache not getting refreshed on PUT ([MODCR-39](https://issues.folio.org/browse/MODCR-39))

## 1.0.4.2020-06-02
* Update to RMB 30.0.1 ([MODCR-38](https://issues.folio.org/browse/MODCR-38))

## 1.0.3 2020-04-27
* Write changes in temporaryLoanTypeId back to item record on POST/PUT for reserves

## 1.0.2 2020-04-07
* Add missing module permissions for location and servicepoint lookup

## 1.0.1 2020-04-06

* Populate temporary location field from CourseListing (MODCR-17,MODCR-31)
* Properly pull call number from Item/Holdings records (MODCR-29,MODCR-30)
* Update RAML capitalization to work on MacOS (MODCR-2,  [MODCR-25](https://issues.folio.org/browse/MODCR-25))
* Fix bug with deletion of a Reserve with no corresponding Item ([MODCR-22](https://issues.folio.org/browse/MODCR-22))
* Fix parameters to loadSample on tenant init (MODCR-23, [MODCR-35](https://issues.folio.org/browse/MODCR-35))
* Change tests to use shared HttpClient rather than new ([MODCR-26](https://issues.folio.org/browse/MODCR-26))
* Fix population for electronicAccess fields with fallback ([MODCR-28](https://issues.folio.org/browse/MODCR-28))
* Scrub fields marked as dynamic from POST/PUT ([MODCR-34](https://issues.folio.org/browse/MODCR-34))
* Fix issue with field expansion for lists of Reserves ([MODCR-36](https://issues.folio.org/browse/MODCR-36))


## 1.0.0 2020-03-13

* Initial release
