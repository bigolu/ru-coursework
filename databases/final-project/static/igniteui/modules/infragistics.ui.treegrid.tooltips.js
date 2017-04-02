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
 *	infragistics.ui.grid.tooltips.js
 */
(function(factory){if(typeof define==="function"&&define.amd){define(["jquery","jquery-ui","./infragistics.util","./infragistics.ui.treegrid","./infragistics.ui.grid.tooltips"],factory)}else{factory(jQuery)}})(function($){$.widget("ui.igTreeGridTooltips",$.ui.igGridTooltips,{_create:function(){this.element.data($.ui.igGridTooltips.prototype.widgetName,this.element.data($.ui.igTreeGridTooltips.prototype.widgetName));$.ui.igGridTooltips.prototype._create.apply(this,arguments)},_getDataView:function(){return this.grid.dataSource.flatDataView()},_getRowIndex:function(element,row){return element.closest("tbody").children("tr:visible:not([data-container='true'],"+"[data-grouprow='true'],"+"[data-new-row='true'])").index(row)},destroy:function(){$.ui.igGridTooltips.prototype.destroy.apply(this,arguments);this.element.removeData($.ui.igGridTooltips.prototype.widgetName)}});$.extend($.ui.igTreeGridTooltips,{version:"16.2.20162.1035"});return $.ui.igTreeGridTooltips});