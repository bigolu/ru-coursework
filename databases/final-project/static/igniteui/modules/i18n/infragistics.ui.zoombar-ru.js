/*!@license
* Infragistics.Web.ClientUI Zoombar localization resources 16.2.20162.1035
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

/*global jQuery */
(function ($) {
$.ig = $.ig || {};

if (!$.ig.Zoombar) {
	$.ig.Zoombar = {};

	$.extend($.ig.Zoombar, {

		locale: {
			zoombarTargetNotSpecified: "Виджету igZoombar необходим целевой объект для отображения!",
			zoombarTypeNotSupported: "Целевой виджет, к которому Zoombar пытается присоединиться, не поддерживается!",
			zoombarProviderNotRecognized: "igZoombar не удалось инициализировать поставщика из указанного класса — возможно, переданное значение не является классом.",
			optionChangeNotSupported: "Изменение этой опции после создания igZoombar не поддерживается:"
		}
	});

}
})(jQuery);
