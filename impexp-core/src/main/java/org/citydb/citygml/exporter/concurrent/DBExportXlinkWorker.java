/*
 * 3D City Database - The Open Source CityGML Database
 * http://www.3dcitydb.org/
 *
 * Copyright 2013 - 2019
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 *
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 *
 * virtualcitySYSTEMS GmbH, Berlin <http://www.virtualcitysystems.de/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Taufkirchen <http://www.moss.de/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.citydb.citygml.exporter.concurrent;

import org.citydb.citygml.common.database.xlink.DBXlink;
import org.citydb.citygml.common.database.xlink.DBXlinkEnum;
import org.citydb.citygml.common.database.xlink.DBXlinkLibraryObject;
import org.citydb.citygml.common.database.xlink.DBXlinkTextureFile;
import org.citydb.citygml.exporter.database.xlink.DBXlinkExporterEnum;
import org.citydb.citygml.exporter.database.xlink.DBXlinkExporterLibraryObject;
import org.citydb.citygml.exporter.database.xlink.DBXlinkExporterManager;
import org.citydb.citygml.exporter.database.xlink.DBXlinkExporterTextureImage;
import org.citydb.concurrent.Worker;
import org.citydb.config.Config;
import org.citydb.config.project.global.LogLevel;
import org.citydb.database.adapter.AbstractDatabaseAdapter;
import org.citydb.event.Event;
import org.citydb.event.EventDispatcher;
import org.citydb.event.EventHandler;
import org.citydb.event.global.EventType;
import org.citydb.event.global.InterruptEvent;
import org.citydb.file.OutputFile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class DBExportXlinkWorker extends Worker<DBXlink> implements EventHandler {
	private final ReentrantLock runLock = new ReentrantLock();
	private volatile boolean shouldRun = true;
	private volatile boolean shouldWork = true;

	private final Connection connection;
	private final DBXlinkExporterManager xlinkExporterManager;
	private final EventDispatcher eventDispatcher;

	public DBExportXlinkWorker(OutputFile outputFile, Connection connection, AbstractDatabaseAdapter databaseAdapter, Config config, EventDispatcher eventDispatcher) {
		this.connection = connection;
		this.eventDispatcher = eventDispatcher;

		xlinkExporterManager = new DBXlinkExporterManager(outputFile, connection, databaseAdapter, config, eventDispatcher);
		eventDispatcher.addEventHandler(EventType.INTERRUPT, this);
	}

	@Override
	public void interrupt() {
		shouldRun = false;
	}

	@Override
	public void run() {
		try {
			if (firstWork != null) {
				doWork(firstWork);
				firstWork = null;
			}

			while (shouldRun) {
				try {
					DBXlink work = workQueue.take();
					doWork(work);
				} catch (InterruptedException ie) {
					// re-check state
				}
			}

			try {
				xlinkExporterManager.close();
			} catch (SQLException e) {
				//
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				//
			}
		}
	}

	private void doWork(DBXlink work) {
		final ReentrantLock runLock = this.runLock;
		runLock.lock();

		try {
			if (!shouldWork)
				return;
			
			boolean success = false;
			DBXlinkEnum type = work.getXlinkType();

			switch (type) {
			case TEXTURE_FILE:
				DBXlinkTextureFile texFile = (DBXlinkTextureFile)work;
				DBXlinkExporterTextureImage imageExporter = (DBXlinkExporterTextureImage) xlinkExporterManager.getDBXlinkExporter(DBXlinkExporterEnum.TEXTURE_IMAGE);
				if (imageExporter != null)
					success = imageExporter.export(texFile);

				break;
			case LIBRARY_OBJECT:
				DBXlinkLibraryObject libObject = (DBXlinkLibraryObject)work;
				DBXlinkExporterLibraryObject libraryObject = (DBXlinkExporterLibraryObject)xlinkExporterManager.getDBXlinkExporter(DBXlinkExporterEnum.LIBRARY_OBJECT);
				if (libraryObject != null)
					success = libraryObject.export(libObject);

				break;
			default:
				return;
			}

			if (!success)
				; // do sth reasonable

		} catch (SQLException e) {
			eventDispatcher.triggerSyncEvent(new InterruptEvent("Aborting export due to SQL errors.", LogLevel.WARN, e, eventChannel, this));
		} finally {
			runLock.unlock();
		}
	}

	@Override
	public void handleEvent(Event event) throws Exception {
		if (event.getChannel() == eventChannel)
			shouldWork = false;
	}
}
