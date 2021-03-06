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
package org.citydb.config.gui;

import org.citydb.config.gui.components.SQLExportFilterComponent;
import org.citydb.config.gui.window.ConsoleWindow;
import org.citydb.config.gui.window.MainWindow;
import org.citydb.config.gui.window.MapWindow;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(name="GuiType", propOrder={
		"main",
		"console",
		"map",
		"sqlExportFilter",
		"showPreferencesConfirmDialog",
		"showOutdatedDatabaseVersionWarning",
		"showUnsupportedADEWarning",
		"showKmlExportUnsupportedADEWarning",
		"showChangeSridWarning",
		"recentlyUsedProjects"
})
public class Gui {
	private MainWindow main; 
	private ConsoleWindow console;
	private MapWindow map;
	private SQLExportFilterComponent sqlExportFilter;
	private boolean showPreferencesConfirmDialog = true;
	private boolean showOutdatedDatabaseVersionWarning = true;
	private boolean showUnsupportedADEWarning = true;
	private boolean showKmlExportUnsupportedADEWarning = true;
	private boolean showChangeSridWarning = true;
	@XmlElementWrapper(name="recentlyUsedProjects")
	@XmlElement(name="fileName")
	private List<String> recentlyUsedProjects;

	@XmlTransient
	private final int maxLastUsedEntries = 5;

	public Gui() {
		main = new MainWindow();
		console = new ConsoleWindow();
		map = new MapWindow();
		sqlExportFilter = new SQLExportFilterComponent();
		recentlyUsedProjects = new ArrayList<>(maxLastUsedEntries + 1);
	}

	public MainWindow getMainWindow() {
		return main;
	}

	public void setMainWindow(MainWindow main) {
		if (main != null)
			this.main = main;
	}

	public ConsoleWindow getConsoleWindow() {
		return console;
	}

	public void setConsoleWindow(ConsoleWindow console) {
		if (console != null)
			this.console = console;
	}

	public MapWindow getMapWindow() {
		return map;
	}

	public void setMapWindow(MapWindow map) {
		if (map != null)
			this.map = map;
	}

	public void setSQLExportFilterComponent(SQLExportFilterComponent sqlExportFilter) {
		if (sqlExportFilter != null)
			this.sqlExportFilter = sqlExportFilter;
	}

	public SQLExportFilterComponent getSQLExportFilterComponent() {
		return sqlExportFilter;
	}

	public boolean isShowPreferencesConfirmDialog() {
		return showPreferencesConfirmDialog;
	}

	public void setShowPreferencesConfirmDialog(boolean showPreferencesConfirmDialog) {
		this.showPreferencesConfirmDialog = showPreferencesConfirmDialog;
	}

	public boolean isShowOutdatedDatabaseVersionWarning() {
		return showOutdatedDatabaseVersionWarning;
	}

	public void setShowOutdatedDatabaseVersionWarning(boolean showOutdatedDatabaseVersionWarning) {
		this.showOutdatedDatabaseVersionWarning = showOutdatedDatabaseVersionWarning;
	}

	public boolean isShowUnsupportedADEWarning() {
		return showUnsupportedADEWarning;
	}

	public void setShowUnsupportedADEWarning(boolean showUnsupportedADEWarning) {
		this.showUnsupportedADEWarning = showUnsupportedADEWarning;
	}
	
	public boolean isShowKmlExportUnsupportedADEWarning() {
		return showKmlExportUnsupportedADEWarning;
	}

	public void setShowKmlExportUnsupportedADEWarning(boolean showKmlExportUnsupportedADEWarning) {
		this.showKmlExportUnsupportedADEWarning = showKmlExportUnsupportedADEWarning;
	}

	public boolean isShowChangeSridWarning() {
		return showChangeSridWarning;
	}

	public void setShowChangeSridWarning(boolean showChangeSridWarning) {
		this.showChangeSridWarning = showChangeSridWarning;
	}

	public List<String> getRecentlyUsedProjectFiles() {
		return recentlyUsedProjects;
	}

	public void setRecentlyUsedProjectFiles(List<String> recentlyUsedProjects) {
		if (recentlyUsedProjects != null)
			this.recentlyUsedProjects = recentlyUsedProjects;
	}

	public int getMaxLastUsedEntries() {
		return maxLastUsedEntries;
	}

}
