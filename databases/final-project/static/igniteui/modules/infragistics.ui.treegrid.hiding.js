﻿/*!@license
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
 *	infragistics.ui.grid.hiding.js
 */
(function(factory){if(typeof define==="function"&&define.amd){define(["jquery","jquery-ui","./infragistics.util","./infragistics.ui.treegrid","./infragistics.ui.grid.hiding"],factory)}else{factory(jQuery)}})(function($){$.widget("ui.igTreeGridHiding",$.ui.igGridHiding,{css:{},options:{},_create:function(){this.element.data($.ui.igGridHiding.prototype.widgetName,this.element.data($.ui.igTreeGridHiding.prototype.widgetName));$.ui.igGridHiding.prototype._create.apply(this,arguments)},destroy:function(){$.ui.igGridHiding.prototype.destroy.apply(this,arguments);this.element.removeData($.ui.igGridHiding.prototype.widgetName)},_injectGrid:function(){$.ui.igGridHiding.prototype._injectGrid.apply(this,arguments)}});$.extend($.ui.igTreeGridHiding,{version:"16.2.20162.1035"});return $.ui.igTreeGridHiding});