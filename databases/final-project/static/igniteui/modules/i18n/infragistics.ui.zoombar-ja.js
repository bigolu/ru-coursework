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
			zoombarTargetNotSpecified: "igZoombar を有効なターゲットにアタッチする必要があります。",
			zoombarTypeNotSupported: "ズームバーにアタッチするウィジェット タイプはサポートされません。",
			zoombarProviderNotRecognized: "igZoombar が指定したクラスからプロバイダーを初期化できなかったか、渡された値がクラスではありません。",
			optionChangeNotSupported: "igZoombar が作成された後のこのオプションの変更はサポートされません:"
		}
	});

}
})(jQuery);
