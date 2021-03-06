﻿/*!@license
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
			zoombarTargetNotSpecified: "igZoombar necesita un destino válido al que adjuntarse.",
			zoombarTypeNotSupported: "El tipo de widget al que la barra de zoom intenta adjuntarse no se admite.",
			zoombarProviderNotRecognized: "igZoombar no ha podido iniciar un proveedor para la clase especificada o el valor que se ha pasado no es una clase.",
			optionChangeNotSupported: "No se admite cambiar la opción siguiente después de que igZoombar se haya creado:"
		}
	});

}
})(jQuery);
