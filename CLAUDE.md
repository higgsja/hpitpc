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
