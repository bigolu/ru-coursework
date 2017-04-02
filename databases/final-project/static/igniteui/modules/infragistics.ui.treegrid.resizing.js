/*!@license
 * Infragistics.Web.ClientUI Tree Grid 16.2.20162.1035
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *	jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	infragistics.dataSource.js
 *	infragistics.ui.shared.js
 *	infragistics.ui.treegrid.js
 *	infragistics.util.js
 *	infragistics.ui.grid.framework.js
 *	infragistics.ui.grid.resizing.js
 */
(function(factory){if(typeof define==="function"&&define.amd){define(["jquery","jquery-ui","./infragistics.util","./infragistics.ui.treegrid","./infragistics.ui.grid.resizing"],factory)}else{factory(jQuery)}})(function($){$.widget("ui.igTreeGridResizing",$.ui.igGridResizing,{css:{},_create:function(){this.element.data($.ui.igGridResizing.prototype.widgetName,this.element.data($.ui.igTreeGridResizing.prototype.widgetName));$.ui.igGridResizing.prototype._create.apply(this,arguments)},destroy:function(){$.ui.igGridResizing.prototype.destroy.apply(this,arguments);this.element.removeData($.ui.igGridResizing.prototype.widgetName)}});$.extend($.ui.igTreeGridResizing,{version:"16.2.20162.1035"});return $.ui.igTreeGridResizing});