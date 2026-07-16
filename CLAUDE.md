# hpitpc

Spring Boot / Vaadin web UI for TPC portfolio data. Reads from the same MariaDB databases as TPCcli (`hlhtxc5_dmOfx`, `hlhtxc5_dbOfx`) but uses JPA / Hibernate for data access rather than plain JDBC.

## Stack
- Spring Boot + Vaadin (web UI)
- Spring Data JPA / Hibernate (ORM)
- MariaDB — same host and databases as TPCcli (`zeus:3306`)
- Lombok

## Package structure
| Package | Contents |
|---|---|
| `com.hpi.tpc.data.entities` | JPA entity models + Mappers for dataMart tables |
| `com.hpi.tpc.data.datamart.entities` | Datamart-specific models (accounts, brokers) |
| `com.hpi.tpc.data.ofx.entities` | OFX-source models |

## System Architecture

hpitpc is the business logic and presentation layer of the TPC system. It does not acquire data or run calculations — that work is done upstream by TPCcli (using TPCCCM as a library). hpitpc reads the results from the shared MariaDB data mart.

```
etradeHarness  →  raw transactions in dmOfx
TPCcli (via TPCCCM)  →  FIFO matching, market values, ActPct, positions
hpitpc  →  portfolio tracking, planning, and coaching UI  ← you are here
```

**TPCcli must run successfully before hpitpc data is current.** Key dependencies:
- Equity holdings grid (`ClientEquityAttributes.ActPct`) — set by `doHPILMktVal()` in TPCcli. If zero for all equities, "Open Only" filter will show an empty grid.
- Sector tracking (`ClientSectorList.MktVal/LMktVal/ActPct`) — set by `doClientSectorListMktLmktUpdate()`.
- Equity info / company names (`EquityInfo`) — populated by TPCcli `--equityInfo`. Must be refreshed within 2 days or the equity holdings query returns no rows (inner join on date filter).

hpitpc does not depend on TPCCCM as a Maven library. It has its own JPA/Hibernate entity implementations of the same tables. Sharing entity code with TPCCCM would require standardizing on one data-access pattern across both apps — deferred indefinitely.

## Status
Active. Forward work gated on TPCcli / etradeHarness pipeline being stable. Last checkpoint: "return to ofx baseline" (2026-06-18).

## Cleanup Project (2026-07-09)
`ClientOpeningStock`/`ClientClosingStock`/`ClientOpeningOptions`/`ClientClosingOptions` were dropped from `hlhtxc5_dmOfx` as part of the TPCcli/TPCCCM dead-code cleanup (nothing writes them). hpitpc's own raw SQL in `ValidateStockTransactionModel.SQL_STRING` and `ValidateOptionTransactionModel.SQL_STRING` (`com.hpi.tpc.data.entities`, called from `TPCDAOImpl.getValidateStockTransactionModels()`/`getValidateOptionTransactionModels()` — the "validate transactions" screen) UNIONed in these same tables and would have broken on the next invocation. Fixed by stripping the `union /* Client... */ (...)` branches from both `SQL_STRING` constants and trimming the corresponding `String.format` arg lists in `TPCDAOImpl.java` (stock: 5 groups of 3 args → 2; options: 5 groups of 3 → 3). Verified: `mvn clean compile` succeeds and both trimmed queries execute against the live DB with no errors.

**Lesson:** hpitpc has its own independent JPA/JDBC SQL and is NOT visible to grep passes scoped to TPCCCM/TPCcli only. Any future table drop/rename in the shared `hlhtxc5_dmOfx`/`hlhtxc5_dbOfx` schema must also grep `hpitpc/src` before executing.

### Test coverage added
`src/test/java/com/hpi/tpc/data/entities/ValidateStockTransactionModelTest.java` and `ValidateOptionTransactionModelTest.java` (JUnit 5/Jupiter, regular `@Test` — run under plain `mvn test`, no failsafe/`-Pit` needed since class names end in `Test` not `IT`). Each asserts: (a) `SQL_STRING` no longer references the dropped Client* tables, (b) exact `%s` placeholder count matches the `TPCDAOImpl` call site (6 for stock, 9 for options), (c) the filled SQL executes against the live DB with dummy values. `src/test/java/com/hpi/tpc/testutil/TestDbConnection.java` opens a direct `DriverManager` connection to `zeus:3306`/`hlhtxc5_dmOfx` (same credentials as `application.properties`), bypassing Spring's pooled datasource which requires a full app context and points at a local tunnel (`127.0.0.1:3306`) not reachable from a standalone test. Mirrors the equivalent pattern added in TPCCCM (`OpeningStockModelTest`, etc.).

Going forward: add unit/integration tests for whatever hpitpc code is touched, incrementally, as part of the same change (confirmed practice, not a separate testing initiative).

### 2026-07-10 — `OpenStockFIFO`/`OpenOptionFIFO` column drops (no hpitpc impact)
TPCCCM/TPCcli dropped 12 dead columns from each of `OpenStockFIFO`/`OpenOptionFIFO` across the Sixth/Seventh/Eighth passes (see TPCCCM/CLAUDE.md), ending with `DateClose`/`PriceClose`/`TotalClose` in the Eighth pass. Confirmed hpitpc has zero references to either table name anywhere in `src` — its JPA layer never reads these two tables directly. No action needed here; noted per the standing rule to check hpitpc before any shared-DB schema change.

### 2026-07-10 — `OpeningStock`/`ClosingStock`/`OpeningOptions`/`ClosingOptions` sided-column drops (no hpitpc impact)
TPCCCM/TPCcli dropped 12 columns (3 per table) in the "Ninth pass" (see TPCCCM/CLAUDE.md) — the last "inactive side" columns each of these 4 tables carried (e.g. `OpeningStock.DateClose/PriceClose/TotalClose`). `ValidateStockTransactionModel`/`ValidateOptionTransactionModel` (hpitpc's own raw SQL, see above) reference these 4 table names but only ever `union`ed columns already stripped in the 2026-07-09 pass — confirmed zero references to any of the 12 dropped columns anywhere in `hpitpc/src`. No action needed.

### 2026-07-11 — `Util_LastDailyStock` eliminated; repointed at `EquityHistory`
Part of the cross-repo removal of `Util_LastDailyStock`/`Util_LastDailyOption` (see TPCCCM/CLAUDE.md "Eleventh pass" and TPCcli/CLAUDE.md "Eleventh cleanup pass" for the full picture). `EquityHistory`/`OptionHistory` are no longer multi-day history tables — TPCcli now prunes each down to one row per ticker/symbol on every daily run, making the separate `Util_Last*` cache tables redundant.

hpitpc had two live references to `Util_LastDailyStock` (none to `Util_LastDailyOption`), both a straightforward table-name + join-key swap (`lds.EquityId`/`ulds.EquityId` → `lds.Ticker`/`ulds.Ticker`; column names `Open`/`High`/`Low`/`Close`/`Volume` are identical on `EquityHistory`):
- `NoteModel.SQL_GET_STRING` (`com.hpi.tpc.data.entities`) — also removed an already-dead commented-out earlier draft of the same query that referenced the old table
- `NotesModel.noteQuoteSql` (`com.hpi.tpc.ui.views.notes`) — a private instance field on a `@UIScope`/`@Component` Vaadin bean, not practical to unit test in isolation; verified directly against the live DB instead of via an automated test

Added `NoteModelTest.sqlGetStringExecutesAgainstDatabase()` (same live-schema-smoke-test pattern as `ValidateStockTransactionModelTest`/`ValidateOptionTransactionModelTest`). `mvn clean compile` and the full test suite pass against the live schema.

### 2026-07-11 — `EquityInfo` pruned to one row per ticker
Part of the same cleanup as TPCCCM's "Twelfth pass" and TPCcli's equivalent entry — `EquityInfo` no longer retains history; TPCcli's `FinVizController4.pruneEquityInfo()` now prunes it down to one row per ticker on every `--equityInfo` run. hpitpc had three independent readers, each of which already self-limited to "latest row" via a redundant `MAX(Date)` filter — removed as dead weight now that the table itself enforces one row per ticker:
- `ClientSectorModel.SQL_SECTORID_FROM_TKR` — dropped the `WHERE Date = (select MAX(Date) ...)` clause. This also fixes a latent pre-existing bug: the old filter used a *global* `MAX(Date)` rather than a per-ticker one, which could return the wrong `Sector` if different tickers were last refreshed on different days. Added `ClientSectorModelTest.sqlSectorIdFromTkrExecutesAgainstDatabase()`.
- `FinVizEquityInfoModel.SQL_GET_LATEST_DATE`/`SQL_GET_LATEST_FILTERED`/`SQL_GET_LATEST_FILTERED_COUNT` (feeds the main equities grid UI) — dropped the same `MAX(Date)` subquery clause from all three. Call-site placeholder counts unchanged (8 for FILTERED, 6 for FILTERED_COUNT). Added `FinVizEquityInfoModelTest` (3 live-schema smoke tests).
- `NoteModel.SQL_GET_STRING` — dropped the inline `(select * from EquityInfo where Date = (select max(Date) ...)) as ei` subquery in favor of joining `EquityInfo` directly. Existing `NoteModelTest.sqlGetStringExecutesAgainstDatabase()` re-verified, no new test needed.

Deliberately left untouched: `ClientEquityModel`'s 2-day staleness filter (the documented "must be refreshed within 2 days" gotcha above) — orthogonal to retention pruning, would need a separate functional decision.

Live `hlhtxc5_dmOfx.EquityInfo` pruned via SSH from 392,580 rows down to 11,598 (one per ticker) — see TPCCCM/CLAUDE.md "Twelfth pass" for the live-DB DDL detail. hpitpc test suite: 11/11 passing against the live schema.

### 2026-07-11 — `EquityHistory` retired in favor of `EquityInfo`, with an easy backout
Part of the cross-repo removal of `EquityHistory` as a data source (see TPCCCM/CLAUDE.md "Thirteenth pass" and TPCcli/CLAUDE.md "Thirteenth cleanup pass" for the full picture). User's call: eliminate use of `EquityHistory` (Schwab quotes) entirely in favor of `EquityInfo.Price` (FinViz), but keep the change trivially reversible — so `EquityHistory` itself and `StockQuotesSchwabController` (TPCcli) are left untouched; only the readers and the writer's call site were changed.

hpitpc had two independent `EquityHistory` readers, both repointed at `EquityInfo`:
- `NoteModel.SQL_GET_STRING` — dropped `Open`/`High`/`Low` columns entirely (no `EquityInfo` substitute exists for them, and grep confirmed `NoteModel.getHigh()`/`getLow()` were never read anywhere in the UI beyond the mapper that set them), changed `Close` to source from `ei.Price` and `Volume` from `ei.Volume`, replaced every internal `lds.\`Close\`` reference in the IPrice/PriceChange/PriceChangePct/Gain/GainPct expressions with `ei.Price`, and dropped `EquityHistory` from the query's `FROM`/`WHERE` clauses entirely. Removed the now-unused `high`/`low` fields from `NoteModel` and their setters from `NoteMapper.mapRow()`. Also removed the 20-arg constructor's `high`/`low` parameters (kept the constructor itself — Lombok's class-level `@Builder` needs an explicit all-fields constructor once the class has any other constructor, so this one is load-bearing for `NoteMapper`'s `NoteModel.builder()...build()` call even though nothing calls the constructor directly by name).
- `NotesModel.noteQuoteSql` — simplified from a 2-table join (`EquityInfo`/`EquityHistory`) down to a single-table `select ... ei.Price as \`Close\` from EquityInfo` query, since `EquityInfo` alone now has everything it needed.

**Backout:** revert these two SQL constants and the `NoteModel`/`NoteMapper` field removals (or discard branch `cleanup/equity-history-retire`) — `EquityHistory` is untouched and still populated by TPCcli's Schwab pipeline once its call site is uncommented (see TPCcli/CLAUDE.md).

hpitpc compiles clean; full test suite passes (existing `NoteModelTest.sqlGetStringExecutesAgainstDatabase()` re-verified against the rewritten SQL, no new test needed since the query shape check was already smoke-test-only).

## Code Review (2026-07-15)

Full read-only review of all 230 `src/main` files (~21k LOC) plus tests/config, done via three parallel agents scoped to `data/`, `ui`+`services`+`charts`+`prefs`+`app`, and `test`+`pom.xml`+`application.properties`. Findings below are the backlog for future cleanup passes; nothing in this section has been fixed yet.

### 🔴 Critical — do this first
**Live DB credentials hardcoded in `src/main/resources/application.properties:26-27`** (`spring.datasource.username=higgsja` / `spring.datasource.password=Jigger01`) and committed to a **public** GitHub repo (`github.com/higgsja/hpitpc`, confirmed via `gh repo view` — `isPrivate: false`), present in all 4 commits of history. **Rotate the `higgsja` MariaDB password** — history rewrite won't undo the exposure since it was already public. `src/test/java/com/hpi/tpc/testutil/TestDbConnection.java` reads the same credentials off the packaged properties file rather than adding a new leak, but will need updating once the password is rotated.

### High
- **SQL injection surface is systemic, not isolated.** Nearly every query in `TPCDAOImpl`, `AccountsModel`, and the `com.hpi.tpc.data.entities` raw-SQL classes (`ValidateStockTransactionModel`, `ValidateOptionTransactionModel`, `NoteModel`, `ClientSectorModel`, `FinVizEquityInfoModel`, `ClientEquityModel`, `EditAccountModel`, `OfxInstitutionModel`) builds SQL via `String.format`/concatenation instead of `JdbcTemplate` `?` binding. Confirmed exploitable: `NotesModel.getTickerInfo()` (`com.hpi.tpc.ui.views.notes.NotesModel.java:39,62`) interpolates a free-text ticker from a Vaadin `TextField` directly into SQL with no escaping; `TPCDAOImpl.saveNote()` (`TPCDAOImpl.java:161`) concatenates the ticker unescaped too. The one correct example, `UserModelService.getByUserName()` (uses `?` params), proves the safe pattern is known but unapplied elsewhere. Most current injection points are low-risk only because interpolated values happen to be internal integers rather than user text — fragile, not by design.
- **Brokerage account passwords stored in plaintext**, flowing from a Vaadin `PasswordField` (`AccountsEditView.java:54`) straight into an unparameterized SQL insert (`AccountsModel.AddAccount()`, lines 132-179).
- **Zero exception handling anywhere in `ui`/`services`/`data`.** No `catch` blocks, no registered Vaadin `ErrorHandler`/`VaadinServiceInitListener`. Any `DataAccessException` from a DB call (including inline calls from click/value-change listeners, e.g. `PositionGridVLBase.java:200-303,612-627`) propagates straight into Vaadin's default error UI — no user-facing message, no logging.

### Medium
- `application.properties:34-46` mixes YAML-style nested indentation (`jpa:`/`hibernate:`/`ddl-auto: validate`) into a flat `.properties` file — Java `Properties` parsing flattens this to garbage keys, so `spring.jpa.hibernate.ddl-auto=validate` and the documented Hikari/isolation overrides silently never take effect.
- `TPCDAOImpl` (singleton `@Repository`) marks ~22 methods `synchronized` — serializes all DB reads app-wide for no real benefit since `JdbcTemplate` is already thread-safe and there's no shared mutable state to protect.
- Nearly every UI class (~95 files) stacks both `@UIScope` and `@VaadinSessionScope` on the same bean (e.g. `PositionGridVLBase.java:18-19`, `AccountsController.java:23-24`) — contradictory scopes, likely leftover from an incomplete refactor; risk of state bleeding across tabs/UIs within a session.
- Placeholder-count coupling between `SQL_STRING` constants and `TPCDAOImpl` call sites is only tested for 5 classes (`ValidateStockTransactionModel`, `ValidateOptionTransactionModel`, `NoteModel`, `ClientSectorModel`, `FinVizEquityInfoModel`). `ClientEquityModel`, `EditAccountModel`, `OfxInstitutionModel`, and all of `DmAccount2Model1` have zero test coverage — a schema change to any of those tables would silently break at runtime with no CI signal (the exact failure mode the 2026-07-09 cleanup already hit once).
- **Dead/stub code reachable from live UI buttons:** `AccountsController.buttonAccountsDelete()` (lines 500-506) is an empty stub wired to a live Delete button; `buttonAccountsCancel()` (lines 604-609) is `int i = 0;` and does not actually revert dirty state despite its doc comment.
- `DmAccount2Model1` is entirely unused dead code (zero references outside its own file) but still carries `@Entity`/`@Id`/`@GeneratedValue` JPA annotations. `UserModel` has the same misleading-annotation pattern — populated purely via manual `RowMapper`, never touched by a `JpaRepository`. Both look like an abandoned migration toward real JPA.
- Test coverage overall: ~297 lines of test code vs ~20.7k lines of main code. Zero coverage of all `ui.views.*`, `TPCDAOImpl` itself, `*Mapper` classes, `app.security`, `services`, `prefs`. The 6 existing tests (5 JUnit smoke tests + 1 Selenium `MainViewIT` from the Vaadin starter scaffold) are SELECT-only against the live DB (confirmed via grep — no insert/update/delete/drop/truncate), so no mutation risk, but they assert only "query doesn't throw," not result correctness. They also make `mvn test` depend on live network reachability to `zeus:3306`.
- Duplicated `JoomlaId`-scoped SQL shape repeated across `ClientSectorModel`, `ClientEquityModel`, `EditAccountModel`, `OfxInstitutionModel` — same `WHERE JoomlaId = '%s'` pattern with only `ORDER BY`/`Active` deltas; could collapse to one parameterized method, low urgency.
- Large duplicated grid-column boilerplate: `PositionGridVLBase.java` (627 lines) has three near-identical `doOpenExpandedGridPOTM`/`doOpenExpandedGridFOTM`/`doClosedExpandedGridPOTM` methods (lines 305-610), same pattern repeated in `GainsPositionGridVL`, `TrackingPositionGridVL`, `EquitiesGridVL`. No shared grid-builder helper.

### Low
- ~500+ lines of commented-out code scattered through `ui`/`data` (e.g. `PositionGridVLBase.java:218-220,230-234,261-268,468-474`; `AccountsController.java:258-272,314,421,98`; `ClientSectorModel.java:103-121` dead `hashCode`/`equals` override superseded by `@EqualsAndHashCode`; stale commented SQL drafts in `NoteModel.java:21` and `ValidateOptionTransactionModel.java:50`).
- Stale/untracked `//todo:` markers: `PositionGridVLBase.java:531,538,604`; `AccountsController.java:496` ("not implemented"); `ValidateStockTransactionModel.java:45` (references a union branch already removed in the 2026-07-09 cleanup — comment should have been deleted with the code).
- Two Vaadin addon deps pinned without a CVE check performed: `com.github.appreciated:apexcharts:24.0.2`, `com.flowingcode.vaadin.addons:twincolgrid:3.0.0` (`pom.xml:58-74`). No other obviously vulnerable/unpinned versions found; `it`/`*IT` vs. `*Test` separation via the failsafe profile (`pom.xml:217-261`) is correctly wired.

**Not a real problem:** no raw JDBC `Connection`/`Statement`/`ResultSet` usage found anywhere — everything goes through `JdbcTemplate`, so manual resource-closing is not a concern. Mixing JPA-style annotations with JDBC row-mapper classes is dead-code confusion, not a runtime cache-inconsistency issue, since the "entities" in question are never actually managed by a persistence context.
