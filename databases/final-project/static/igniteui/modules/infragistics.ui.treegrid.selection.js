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
 *	infragistics.ui.grid.selection.js
 */
(function(factory){if(typeof define==="function"&&define.amd){define(["jquery","jquery-ui","./infragistics.util","./infragistics.ui.treegrid","./infragistics.ui.grid.selection"],factory)}else{factory(jQuery)}})(function($){$.widget("ui.igTreeGridSelection",$.ui.igGridSelection,{css:{},options:{},_create:function(){this.element.data($.ui.igGridSelection.prototype.widgetName,this.element.data($.ui.igTreeGridSelection.prototype.widgetName));$.ui.igGridSelection.prototype._create.apply(this,arguments)},_getDataView:function(){return this.grid.dataSource.flatDataView()},destroy:function(){$.ui.igGridSelection.prototype.destroy.apply(this,arguments);this.element.removeData($.ui.igGridSelection.prototype.widgetName)},_keyDown:function(event){var target;if(this.options.mode==="row"){if(this._keyDownRowMode(event)){return}}else if(this.options.mode==="cell"){if(this._keyDownCellMode(event)){return}}target=event&&event.target?$(event.target):null;if(!target.is("[data-expand-button]")){$.ui.igGridSelection.prototype._keyDown.apply(this,arguments)}},_keyDownRowMode:function(event){var tr=$(event.target),keyCode=event.keyCode,state;if(keyCode===$.ui.keyCode.LEFT||keyCode===$.ui.keyCode.RIGHT){if(!tr.is("tr")){return}state=tr.attr("aria-expanded");if(state==="undefined"||state===undefined){return}this.grid._expandCollapseRow(tr,keyCode===$.ui.keyCode.RIGHT,true);return true}else if(keyCode===$.ui.keyCode.HOME){if(this._navigateRow(event,"top")){event.preventDefault();return true}}else if(keyCode===$.ui.keyCode.END){if(this._navigateRow(event,"bottom")){event.preventDefault();return true}}if(tr.is("td.ui-igtreegrid-non-data-column")&&(keyCode===$.ui.keyCode.SPACE||keyCode===$.ui.keyCode.ENTER)){return true}},_keyDownCellMode:function(event){var keyCode=event.keyCode,updating;if(keyCode===$.ui.keyCode.ENTER){updating=this.grid.element.data("igGridUpdating");if(!updating&&this._expandCollapseRowByCell()){event.preventDefault();event.stopPropagation();return true}}else if(event.altKey&&(keyCode===$.ui.keyCode.UP||keyCode===$.ui.keyCode.DOWN)){if(this._expandCollapseRowByCell(null,keyCode===$.ui.keyCode.DOWN)){event.preventDefault();return true}}else if(keyCode===$.ui.keyCode.HOME){if(event.ctrlKey){if(this._navigateCell(event,"topLeft")){event.preventDefault();return true}}if(this._navigateCell(event,"left")){event.preventDefault();return true}}else if(keyCode===$.ui.keyCode.END){if(event.ctrlKey){if(this._navigateCell(event,"bottomRight")){event.preventDefault();return true}}if(this._navigateCell(event,"right")){event.preventDefault();return true}}else if(keyCode===$.ui.keyCode.SPACE&&this.grid.options.renderExpansionIndicatorColumn){if(this._expandCollapseRowByCell()){event.preventDefault();event.stopPropagation();return true}}},_navigateCell:function(event,dir){var $cell=this._getActiveCell(),funcNav,$nextActiveCell,self=this,$tr;if(!$cell||!$cell.length){return}$tr=$cell.closest("tr");if(dir==="left"){$nextActiveCell=$tr.children("td:not([data-skip]):visible").first()}else if(dir==="right"){$nextActiveCell=$tr.children("td:not([data-skip]):visible").last()}if($nextActiveCell){this._storedActiveIndex=null;this._navigateOwn($nextActiveCell,this._selection.activeElement,event.keyCode,false,event.shiftKey);return true}funcNav=function($tbody,e){var $tr,$nextEl;if(e.ctrlKey){this.clearSelection()}if(dir==="topLeft"){$tr=$tbody.children("tr:visible:not([data-skip])").first();$nextEl=$tr.children("td:visible:not([data-skip])").first()}else{$tr=$tbody.children("tr:visible:not([data-skip])").last();$nextEl=$tr.children("td:visible:not([data-skip])").last()}self._storedActiveIndex=null;self._navigateOwn($nextEl,this._selection.activeElement,e.keyCode,false,e.shiftKey)};return this._navigateTo(event,dir,funcNav)},_navigateRow:function(event,dir){var funcNav=function($tbody,ev,direction){var $nextEl,prevActiveElement=this._selection.activeElement;if(direction==="top"){$nextEl=$tbody.children("tr:visible:not([data-skip])").first()}else{$nextEl=$tbody.children("tr:visible:not([data-skip])").last()}if(ev.shiftKey&&this.options.multipleSelection){this._shiftSelectChange($nextEl)}else{this._navigateOwn($nextEl,prevActiveElement,ev.keyCode,false,ev.shiftKey)}};return this._navigateTo(event,dir,funcNav)},_navigateTo:function(event,dir,funcNavigate){var $scrollContainer,go=this.grid.options,self=this,scrTop;if(go.virtualization===true&&go.virtualizationMode==="continuous"){$scrollContainer=this.grid._scrollContainer();if(dir==="topLeft"||dir==="top"){scrTop=0}else{scrTop=$scrollContainer.children(":first-child").height()}$scrollContainer.scrollTop(scrTop);if(self._loadingIndicator===undefined){self._initLoadingIndicator()}self._loadingIndicator.show();setTimeout(function(){if(self._loadingIndicator){self._loadingIndicator.hide()}funcNavigate.call(self,self.grid.element.children("tbody"),event,dir)},300)}else{funcNavigate.call(self,this.grid.element.children("tbody"),event,dir)}return true},_getActiveCell:function(){var activeEl=this._selection.activeElement;if(!activeEl){return}return this._getCellByIdentifier(activeEl)},_expandCollapseRowByCell:function($cell,expand){var $tr;if(!$cell){$cell=this._getActiveCell();if(!$cell){return}}if($cell.length&&$cell.attr("data-expand-cell")&&$cell.find("[data-expand-button]").length){$tr=$cell.closest("tr");if(expand===undefined){this.grid._toggleRow($tr)}else{this.grid._expandCollapseRow($tr,expand,true)}return true}},_initLoadingIndicator:function(){this._loadingIndicator=this.grid.container().length>0?this.grid.container().igLoading().data("igLoading"):this.grid.element.igLoading().data("igLoading").indicator()},_mouseUp:function(event){var target=event&&event.target?$(event.target):null;if(!target.is("[data-expand-button]")&&(!target.is("td[data-expand-cell]")||!this.grid.options.renderExpansionIndicatorColumn)){$.ui.igGridSelection.prototype._mouseUp.apply(this,arguments)}},_virtualRecordsRendered:function(info){var $ae=info.activeElement;if($ae.is("tr")||$ae.is("td")){this._selection.activate(this._identifierForTarget($ae),$ae,true)}},selectRowById:function(id){if(this.options.mode==="cell"){return}if(!this._selection.isSelected(id,this.grid)){if(this._selection.settings.owner!==this.grid){this._selection.changeOwner(this.grid)}this._selection.select(id,true,{element:this._getRowsByIdentifier(id),id:id},true)}},_select:function(info){var element=info.element||this._selection.elementFromIdentifier(info.id);if(element.is("tr")&&this.grid.options.renderExpansionIndicatorColumn){element.children("td.ui-igtreegrid-non-data-column").addClass(this.css.selectedCell)}$.ui.igGridSelection.prototype._select.apply(this,arguments)},_preventDefault:function(event){var target=$(event.target);if(target.is("td")&&(!target.is("td[data-expand-cell]")||!this.grid.options.renderExpansionIndicatorColumn)){event.preventDefault()}else if(target.attr("data-expandcell-indicator")){event.preventDefault();event.stopPropagation()}}});$.extend($.ui.igTreeGridSelection,{version:"16.2.20162.1035"});return $.ui.igTreeGridSelection});