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
