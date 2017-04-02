﻿/*!@license
 * Infragistics.Web.ClientUI Sparkline 16.2.20162.1035
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 * jquery.js
 * jquery-ui.js
 * infragistics.util.js
 * infragistics.datasource.js
 * infragistics.templating.js
 * infragistics.ext_core.js
 * infragistics.ext_collections.js
 * infragistics.ext_ui.js
 * infragistics.dv_core.js
 * infragistics.dv_geometry.js
 * infragistics.dv.simple.core.js
 * infragistics.ui.basechart.js
 * infragistics.chart_sparkline.js
 */
(function(factory){if(typeof define==="function"&&define.amd){define(["jquery","jquery-ui","./infragistics.util","./infragistics.datasource","./infragistics.templating","./infragistics.dv.simple.core","./infragistics.datachart_core","./infragistics.chart_sparkline","./infragistics.ui.basechart"],factory)}else{factory(jQuery)}})(function($){var _aNull=function(v,nan){return v===null||v===undefined||nan&&typeof v==="number"&&isNaN(v)};$.widget("ui.igSparkline",$.ui.igBaseChart,{css:{chart:"ui-sparkline ui-corner-all ui-widget-content",tooltip:"ui-sparkline-tooltip ui-widget-content ui-corner-all"},options:{width:null,height:null,brush:null,negativeBrush:null,markerBrush:null,negativeMarkerBrush:null,firstMarkerBrush:null,lastMarkerBrush:null,highMarkerBrush:null,lowMarkerBrush:null,trendLineBrush:null,horizontalAxisBrush:null,verticalAxisBrush:null,normalRangeFill:null,horizontalAxisVisibility:"collapsed",verticalAxisVisibility:"collapsed",markerVisibility:"collapsed",negativeMarkerVisibility:"collapsed",firstMarkerVisibility:"collapsed",lastMarkerVisibility:"collapsed",lowMarkerVisibility:"collapsed",highMarkerVisibility:"collapsed",normalRangeVisibility:"collapsed",displayNormalRangeInFront:true,markerSize:-1,firstMarkerSize:-1,lastMarkerSize:-1,highMarkerSize:-1,lowMarkerSize:-1,negativeMarkerSize:-1,lineThickness:-1,valueMemberPath:null,labelMemberPath:null,trendLineType:"none",trendLinePeriod:7,trendLineThickness:-1,normalRangeMinimum:0,normalRangeMaximum:0,displayType:"line",unknownValuePlotting:"dontPlot",verticalAxisLabel:null,horizontalAxisLabel:null,formatLabel:null,pixelScalingRatio:0},events:{dataBinding:null,dataBound:null},_create:function(){$.ui.igBaseChart.prototype._create.apply(this);var sparkline=this._chart},_set_option:function(sparkline,key,value){if($.ui.igBaseChart.prototype._set_option.apply(this,arguments)){return true}switch(key){case"brush":if(value==null){sparkline.brush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.brush($tempBrush)}return true;case"negativeBrush":if(value==null){sparkline.negativeBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.negativeBrush($tempBrush)}return true;case"markerBrush":if(value==null){sparkline.markerBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.markerBrush($tempBrush)}return true;case"negativeMarkerBrush":if(value==null){sparkline.negativeMarkerBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.negativeMarkerBrush($tempBrush)}return true;case"firstMarkerBrush":if(value==null){sparkline.firstMarkerBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.firstMarkerBrush($tempBrush)}return true;case"lastMarkerBrush":if(value==null){sparkline.lastMarkerBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.lastMarkerBrush($tempBrush)}return true;case"highMarkerBrush":if(value==null){sparkline.highMarkerBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.highMarkerBrush($tempBrush)}return true;case"lowMarkerBrush":if(value==null){sparkline.lowMarkerBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.lowMarkerBrush($tempBrush)}return true;case"trendLineBrush":if(value==null){sparkline.trendLineBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.trendLineBrush($tempBrush)}return true;case"horizontalAxisBrush":if(value==null){sparkline.horizontalAxisBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.horizontalAxisBrush($tempBrush)}return true;case"verticalAxisBrush":if(value==null){sparkline.verticalAxisBrush(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.verticalAxisBrush($tempBrush)}return true;case"normalRangeFill":if(value==null){sparkline.normalRangeFill(null)}else{var $tempBrush=$.ig.Brush.prototype.create(value);sparkline.normalRangeFill($tempBrush)}return true;case"horizontalAxisVisibility":switch(value){case"visible":sparkline.horizontalAxisVisibility(0);break;case"collapsed":sparkline.horizontalAxisVisibility(1);break}return true;case"verticalAxisVisibility":switch(value){case"visible":sparkline.verticalAxisVisibility(0);break;case"collapsed":sparkline.verticalAxisVisibility(1);break}return true;case"markerVisibility":switch(value){case"visible":sparkline.markerVisibility(0);break;case"collapsed":sparkline.markerVisibility(1);break}return true;case"negativeMarkerVisibility":switch(value){case"visible":sparkline.negativeMarkerVisibility(0);break;case"collapsed":sparkline.negativeMarkerVisibility(1);break}return true;case"firstMarkerVisibility":switch(value){case"visible":sparkline.firstMarkerVisibility(0);break;case"collapsed":sparkline.firstMarkerVisibility(1);break}return true;case"lastMarkerVisibility":switch(value){case"visible":sparkline.lastMarkerVisibility(0);break;case"collapsed":sparkline.lastMarkerVisibility(1);break}return true;case"lowMarkerVisibility":switch(value){case"visible":sparkline.lowMarkerVisibility(0);break;case"collapsed":sparkline.lowMarkerVisibility(1);break}return true;case"highMarkerVisibility":switch(value){case"visible":sparkline.highMarkerVisibility(0);break;case"collapsed":sparkline.highMarkerVisibility(1);break}return true;case"normalRangeVisibility":switch(value){case"visible":sparkline.normalRangeVisibility(0);break;case"collapsed":sparkline.normalRangeVisibility(1);break}return true;case"displayNormalRangeInFront":sparkline.displayNormalRangeInFront(value);return true;case"markerSize":sparkline.markerSize(value);return true;case"firstMarkerSize":sparkline.firstMarkerSize(value);return true;case"lastMarkerSize":sparkline.lastMarkerSize(value);return true;case"highMarkerSize":sparkline.highMarkerSize(value);return true;case"lowMarkerSize":sparkline.lowMarkerSize(value);return true;case"negativeMarkerSize":sparkline.negativeMarkerSize(value);return true;case"lineThickness":sparkline.lineThickness(value);return true;case"valueMemberPath":sparkline.valueMemberPath(value);return true;case"labelMemberPath":sparkline.labelMemberPath(value);return true;case"trendLineType":switch(value){case"none":sparkline.trendLineType(0);break;case"linearFit":sparkline.trendLineType(1);break;case"quadraticFit":sparkline.trendLineType(2);break;case"cubicFit":sparkline.trendLineType(3);break;case"quarticFit":sparkline.trendLineType(4);break;case"quinticFit":sparkline.trendLineType(5);break;case"logarithmicFit":sparkline.trendLineType(6);break;case"exponentialFit":sparkline.trendLineType(7);break;case"powerLawFit":sparkline.trendLineType(8);break;case"simpleAverage":sparkline.trendLineType(9);break;case"exponentialAverage":sparkline.trendLineType(10);break;case"modifiedAverage":sparkline.trendLineType(11);break;case"cumulativeAverage":sparkline.trendLineType(12);break;case"weightedAverage":sparkline.trendLineType(13);break}return true;case"trendLinePeriod":sparkline.trendLinePeriod(value);return true;case"trendLineThickness":sparkline.trendLineThickness(value);return true;case"normalRangeMinimum":sparkline.normalRangeMinimum(value);return true;case"normalRangeMaximum":sparkline.normalRangeMaximum(value);return true;case"displayType":switch(value){case"line":sparkline.displayType(0);break;case"area":sparkline.displayType(1);break;case"column":sparkline.displayType(2);break;case"winLoss":sparkline.displayType(3);break}return true;case"unknownValuePlotting":switch(value){case"linearInterpolate":sparkline.unknownValuePlotting(0);break;case"dontPlot":sparkline.unknownValuePlotting(1);break}return true;case"verticalAxisLabel":sparkline.verticalAxisLabel(value);return true;case"horizontalAxisLabel":sparkline.horizontalAxisLabel(value);return true;case"formatLabel":sparkline.formatLabel(value);return true;case"pixelScalingRatio":sparkline.pixelScalingRatio(value);return true}},_setOption:function(key,val){var chart=this._chart,o=this.options;if(o[key]===val){return this}$.Widget.prototype._setOption.apply(this,arguments);this._set_option(chart,key,val);return this},_getValueKeyName:function(){return"valueMemberPath"},_getRemoteDataKeys:function(){return[this.options.valueMemberPath,this.options.labelMemberPath]},_getNotifyResizeName:function(){return"notifyResized"},_createChart:function(){return new $.ig.XamSparkline},_sparkline:function(){return this._chart},destroy:function(){$.ui.igBaseChart.prototype.destroy.apply(this)}});$.extend($.ui.igSparkline,{version:"16.2.20162.1035"});return $.ui.igSparkline});