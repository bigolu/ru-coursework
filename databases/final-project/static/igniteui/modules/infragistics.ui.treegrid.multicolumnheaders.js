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
*	infragistics.ui.grid.multicolumnheaders.js
*/
(function(factory){if(typeof define==="function"&&define.amd){define(["jquery","jquery-ui","./infragistics.util","./infragistics.ui.treegrid","./infragistics.ui.grid.mullticolumnheaders"],factory)}else{factory(jQuery)}})(function($){$.widget("ui.igTreeGridMultiColumnHeaders",$.ui.igGridMultiColumnHeaders,{css:{},options:{},_create:function(){this.element.data($.ui.igGridMultiColumnHeaders.prototype.widgetName,this.element.data($.ui.igTreeGridMultiColumnHeaders.prototype.widgetName));$.ui.igGridMultiColumnHeaders.prototype._create.apply(this,arguments)},destroy:function(){$.ui.igGridMultiColumnHeaders.prototype.destroy.apply(this,arguments);this.element.removeData($.ui.igGridMultiColumnHeaders.prototype.widgetName)},_injectGrid:function(){$.ui.igGridMultiColumnHeaders.prototype._injectGrid.apply(this,arguments)}});$.extend($.ui.igTreeGridMultiColumnHeaders,{version:"16.2.20162.1035"});return $.ui.igTreeGridMultiColumnHeaders});