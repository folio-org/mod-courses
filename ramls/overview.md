<!--
$ indir .. ../folio-tools/generate-api-docs/generate_api_docs.py -r mod-courses -l info

By observation, the "Overview" heading generated from the `documentation.title` entry in `courses.raml` is rendered as a level-3 heading in our first docmentation generation and as a level-4 heading in our second. So the only way to safely have headings within this document nested beneath this is to start at level 4.
-->

The Course Reserves API is all about courses, and the items that have been reserved to them. (A reserved item is typically moved from its usual location to a special temporary location, along with other items reserved to the same course. From there, it can be picked up and checked out by students registered to the course in question.)

There are ten types implemented by this module. Of these, six
(`role`,
`term`,
`coursetype`,
`department`,
`processingstatus`
and
`copyrightstatus`)
are simple controlled vocabulaties used by the four more meaningful types. Of these six, `term` has a name, a start-date and and end-date; all the other five consist simple of a name and a description.

The four main types are more complex:

#### course

This represents a course, and has fields like `name`, `description`, `departmentId` (linking to a department within the controlled vocabulary of that name), `courseNumber` and `sectionName`.

In this type and in courselisting records (see below), some linked objects are expanded inline when a record is fetched: these are furnished in fields whose names end with `object`: for example, there is `departmentObject` field corresponding to `departmentId`, containing the `name` and `description` from the controlled vocabulary entry. 

However, some other fields that we might expect to see in a course record (e.g. `registrarId`, `termId`) are not present. This is because of the way we represent cross-listed courses. A courselisting record contains fields common to a set of cross-listed courses: each course belongs to exactly one courselisting and has a `courseListingId` field that specified this. Fetched records also have a corresponding `courseListingObject`, so that the Registrar ID of a course can be found in `courseListingObject.registrarId`.

#### courselisting

This represents the information common to a set of cross-listed courses, and has fields like `registrarId`, `externalId`, `termId` (and associated `termObject`) and `courseTypeId` (and associated `courseTypeObject`). It also has two further fields that link to objects defined in FOLIO's Inventory module:
`servicepointId` and `locationId`.

#### instructor

Instructors do not exist in isolation (unlike the six controlled-vocabulary objects) but only in the context of a specific courselisting: they are accessed at `/coursereserves/courselistings/{listing_id}/instructors`. Each instructor record contains an optional `userId` pointing into FOLIO's Users module, and data fields such as `name` and `barcode` which may be either copied from the User record (when it exists) or manually filled (when it does not).

#### reserve

Similarly, reserves exist in the context of specific courselisting: they are accessed at `/coursereserves/courselistings/{listing_id}/reserves`. Each reserve record contains a mandatory `itemId` pointing into FOLIO's Inventory module, and a `copiedItem` subrecord containing data fields such as `title` and `contributors` which are copied from the Item record to facilitate searching.

In addition to the fields pertaining to the reserved item, this record contains information about the reserve itself, including `processingStatusId` and `startDate` and `endDate`. These last two are inherited from the term associated with the courselisting that the reserve belongs to, but can be overridden.


### Note on location semantics

[Locations in FOLIO are quite complex](https://wiki.folio.org/display/FOLIOtips/Holdings+and+Items+Effective+Location+Logic+in+FOLIO). Items have both a permanent and temporary location. Both these fields also exist in the holdings record that contains an item. The item's effective location is its own temporary location if defined, otherwise its permanent location if defined, otherwise the temporary location of its holdings record if defined, otherwise the permanent location of its holdings record.

When a new reserve is created in Course reserves, the reserve record contains a copy of parts of the reserved item's record, including the item's permanent and temporary locations (but not its effective location, as this is never stored in FOLIO and only calculated when required).

In general, when editing a reserve in the Course Reserves app, _only_ the reserve itself is edited, including for example the copyright information associated with the reserve. But as a special case, when the temporary location is edited in a reserve, the Course Reserves module also modifies the temporary location of the underlying item record in inventory. When the reserve is deleted, the temporary location of the underlying item record is reset.

When reserving an item with no temporary location of its own (such as barcode 10101 in the FOLIO sample records) to a course with no location, the reserve that is created has no temporary location, and editing it will show "(None required)" as the temporary location. Changing fields other than temporary location and saving does not change this: the reserve's temporary location remains as "(None Required)" until it is itself edited. At that point, the temporary location that is set into the reserve gets copied back into the underlying item.

When reserving an item that already has a temporary location of its own (such as barcode 90000 in the FOLIO sample records, with temporary location "Annex") to a course with no location, the reserve that is created has no temporary location _of its own_, but the underlying item's temporary location of course remains unchanged. Editing the reserve will show the item's temporary location as the temporary location to be edited, because that is the effective temporary location, and saving the reserve will set that as the reserve's own temporary location. When the reserve is deleted, the underlying item's own temporary location is unset, on the assumption that the item has moved back to its permanent location.

The only difference when reserving to a course that has a location of its own is that when the reserve is placed, the temporary location of the underlying item is set to the location of the course. As before, when the reserve is deleted, the item's temporary location is cleared.


