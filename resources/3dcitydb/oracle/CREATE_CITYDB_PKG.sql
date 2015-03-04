-- CREATE_CITYDB_PKG.sql
--
-- Authors:     Felix Kunde <fkunde@virtualcitysystems.de>
--              Claus Nagel <cnagel@virtualcitysystems.de>
--
-- Copyright:   (c) 2012-2014  Chair of Geoinformatics,
--                             Technische Universit�t M�nchen, Germany
--                             http://www.gis.bv.tum.de
--
-- Copyright:   (c) 2007-2012  Institute for Geodesy and Geoinformation Science,
--                             Technische Universit�t Berlin, Germany
--                             http://www.igg.tu-berlin.de
--
--              This skript is free software under the LGPL Version 2.1.
--              See the GNU Lesser General Public License at
--              http://www.gnu.org/copyleft/lgpl.html
--              for more details.
-------------------------------------------------------------------------------
-- About:
-- Creates subpackages "citydb_*".
-------------------------------------------------------------------------------
--
-- ChangeLog:
--
-- Version | Date       | Description                               | Author
-- 1.0.0     2014-10-10   release version                             FKun
--                                                                    CNag
--
SELECT 'Creating packages ''citydb_util'', ''citydb_idx'', ''citydb_srs'', ''citydb_stat'', ''citydb_delete_by_lineage'', ''citydb_delete'', and corresponding types' as message from DUAL;
@@PL_SQL/CITYDB_PKG/UTIL/UTIL.sql;
@@PL_SQL/CITYDB_PKG/INDEX/IDX.sql;
@@PL_SQL/CITYDB_PKG/SRS/SRS.sql;
@@PL_SQL/CITYDB_PKG/STATISTICS/STAT.sql;
@@PL_SQL/CITYDB_PKG/DELETE/DELETE.sql;
@@PL_SQL/CITYDB_PKG/DELETE/DELETE_BY_LINEAGE;
SELECT 'Packages ''citydb_util'', ''citydb_idx'', ''citydb_srs'', ''citydb_stat'', ''citydb_delete_by_lineage'', and ''citydb_delete'' created' as message from DUAL;
